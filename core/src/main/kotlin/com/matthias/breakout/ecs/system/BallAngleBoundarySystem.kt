package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.degreesToRadians
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.log.logger

private val LOG = logger<BallAngleBoundarySystem>()

private val family = allOf(BallComponent::class, BodyComponent::class).exclude(RemoveComponent::class, StickyComponent::class).get()

class BallAngleBoundarySystem : IteratingSystem(family) {

    override fun processEntity(entity: Entity, delta: Float) {
        val ballC = entity[BallComponent::class]!!
        val bodyC = entity[BodyComponent::class]!!

        val ball = bodyC.body
        val angle = ball.angle * radiansToDegrees

        ball.linearVelocity = velocityOnAngle(ballC.velocity, ball.angle)

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