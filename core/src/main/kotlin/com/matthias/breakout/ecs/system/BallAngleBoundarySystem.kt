package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.degreesToRadians
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BodyComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger

private val LOG = logger<BallAngleBoundarySystem>()

class BallAngleBoundarySystem : IteratingSystem(allOf(BallComponent::class, BodyComponent::class).get()) {

    override fun processEntity(entity: Entity, delta: Float) {
        val ballC = entity[BallComponent.mapper]!!
        val bodyC = entity[BodyComponent.mapper]!!

        val ball = bodyC.body
        val angle = ball.angle * radiansToDegrees

        val correctedAngle = when {
            angle >= 0f && angle < 30f -> 30f
            angle <= 0f && angle > -30f -> -30f
            angle > 150f && angle <= 180f -> 150f
            angle < -150f && angle >= -180f -> -150f
            else -> null
        }

        correctedAngle?.let {
            LOG.debug { "Ball incorrect angle $angle corrected to $correctedAngle" }
            ball.setTransform(ball.position, correctedAngle * degreesToRadians)
            ball.linearVelocity = velocityOnAngle(ballC.velocity, correctedAngle * degreesToRadians)
        }
    }
}