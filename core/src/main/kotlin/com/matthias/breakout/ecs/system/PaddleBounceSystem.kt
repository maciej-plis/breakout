package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils.degreesToRadians
import com.matthias.breakout.common.halfWidth
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.*
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallPaddleHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.log.logger

private val LOG = logger<PaddleBounceSystem>()

private val ballFamily = allOf(BallComponent::class, BodyComponent::class).exclude(RemoveComponent::class, StickyComponent::class).get()
private val paddleFamily = allOf(PaddleComponent::class).exclude(StickyComponent::class).get()

/**
 * **update**: for each event of type [BallPaddleHit] reflect ball angle
 */
class PaddleBounceSystem(private val eventManager: GameEventManager<GameEvent>) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.getEventsOfType<BallPaddleHit>()
            .filter { ballFamily.matches(it.ballEntity) }
            .filter { paddleFamily.matches(it.paddleEntity) }
            .forEach { event ->
                val bodyC = event.ballEntity[BodyComponent.mapper]!!
                val ballC = event.ballEntity[BallComponent.mapper]!!

                val paddleTransformC = event.paddleEntity[TransformComponent.mapper]!!
                val paddleContactPoint = event.paddleContactPoint

                val percent = paddleContactPoint.x / paddleTransformC.size.halfWidth
                val angle = ((-60 * percent) + 90)

                LOG.debug { "Ball hit paddle, reflected on angle: $angle" }
                bodyC.body.let { ball ->
                    ball.linearVelocity = velocityOnAngle(ballC.velocity, angle * degreesToRadians)
                    ball.setTransform(ball.position, ball.linearVelocity.angleRad())
                }
            }
    }
}