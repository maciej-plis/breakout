package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Buttons.LEFT
import com.badlogic.gdx.Input.Keys.SPACE
import com.matthias.breakout.common.*
import com.matthias.breakout.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.remove
import ktx.log.logger

private val LOG = logger<BallReleaseSystem>()
private val FAMILY = allOf(PaddleComponent::class, BodyComponent::class, ShownComponent::class).get()

/**
 * System responsible for releasing balls attached to paddle.
 *
 * --
 *
 * **Family**:
 * - allOf: [[PaddleComponent], [BodyComponent], [ShownComponent]]
 */
class BallReleaseSystem : IteratingSystem(FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        if (!(Gdx.input.isButtonPressed(LEFT) or Gdx.input.isKeyPressed(SPACE))) return

        val paddleC = entity[PaddleComponent::class] ?: return LOG.missingComponent<PaddleComponent>()

        paddleC.stickingBalls.forEach { ballEntity ->
            val bodyC = ballEntity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()
            val ballC = ballEntity[BallComponent::class] ?: return LOG.missingComponent<BallComponent>()
            val attachC = ballEntity[AttachComponent::class] ?: return LOG.missingComponent<AttachComponent>()

            ballEntity.remove<AttachComponent>()

            val paddleContactPoint = attachC.offset
            val paddleTransformC = attachC.targetEntity!![TransformComponent::class]!!

            val percent = paddleContactPoint.x / paddleTransformC.size.halfWidth
            val angle = ((-60 * percent) + 90).toRadians()

            LOG.debug { "Ball released on angle: ${angle.toDegrees()}" }
            bodyC.setAngle(angle)
            bodyC.setVelocity(velocityOnAngle(ballC.velocity, angle))
        }

        paddleC.stickingBalls.clear()
    }
}
