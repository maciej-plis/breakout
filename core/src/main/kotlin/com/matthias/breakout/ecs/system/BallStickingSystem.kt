package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Buttons.LEFT
import com.badlogic.gdx.math.MathUtils.degreesToRadians
import com.badlogic.gdx.math.Vector2
import com.matthias.breakout.common.halfWidth
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.remove
import ktx.log.logger

private val LOG = logger<BallStickingSystem>()

private val family = allOf(BallComponent::class, BodyComponent::class, StickyComponent::class, AttachComponent::class).get()

class BallStickingSystem : IteratingSystem(family) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class]!!
        val ballC = entity[BallComponent::class]!!
        val attachC = entity[AttachComponent::class]!!
        val body = bodyC.body

        if (Gdx.input.isButtonPressed(LEFT)) {
            entity.remove<AttachComponent>()
            entity.remove<StickyComponent>()

            val paddleContactPoint = attachC.offset
            val paddleTransformC = attachC.attachedToEntity!![TransformComponent::class]!!

            val percent = paddleContactPoint.x / paddleTransformC.size.halfWidth
            val angle = ((-60 * percent) + 90)

            LOG.debug { "Ball hit paddle, reflected on angle: $angle" }
            bodyC.body.let { ball ->
                ball.linearVelocity = velocityOnAngle(ballC.velocity, angle * degreesToRadians)
                ball.setTransform(ball.position, ball.linearVelocity.angleRad())
            }

            return
        }

        body.linearVelocity = Vector2(0f, 0f)
    }
}