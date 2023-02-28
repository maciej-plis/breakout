package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.degreesToRadians
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.matthias.breakout.common.contains
import com.matthias.breakout.common.open
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BodyComponent
import ktx.ashley.allOf
import ktx.ashley.get

class BallAngleBoundarySystem : IteratingSystem(allOf(BallComponent::class, BodyComponent::class).get()) {

    override fun processEntity(entity: Entity, delta: Float) {
        val ballC = entity[BallComponent.mapper]!!
        val bodyC = entity[BodyComponent.mapper]!!

        val ball = bodyC.body
        val angle = ball.angle * radiansToDegrees

        if (angle in -30f open 30f) {
            val correctedAngle = (if (angle >= 0) 30f else -30f) * degreesToRadians
            ball.setTransform(ball.position, correctedAngle)
            ball.linearVelocity = velocityOnAngle(ballC.velocity, correctedAngle)
        } else if (angle !in -150f..150f) {
            val correctedAngle = (if (angle > 150f) 150f else -150f) * degreesToRadians
            ball.setTransform(ball.position, correctedAngle)
            ball.linearVelocity = velocityOnAngle(ballC.velocity, correctedAngle)
        }
    }
}