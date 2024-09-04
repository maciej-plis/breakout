package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils.clamp
import com.matthias.breakout.common.*
import com.matthias.breakout.ecs.component.*
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallPaddleHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.log.logger

private val LOG = logger<PaddleBounceSystem>()
private val BALL_FAMILY = allOf(BallComponent::class, BodyComponent::class).get()
private val PADDLE_FAMILY = allOf(PaddleComponent::class).exclude(StickyComponent::class).get()

private val BOUNCE_DEGREES_RANGE = 90f..150f

/**
 * System responsible for reflecting ball on angle based on paddle contact point.
 * It listens for events of type [BallPaddleHit].
 *
 * --
 *
 * **ballFamily**:
 * - allOf: [[BallComponent], [BodyComponent]]
 *
 * **paddleFamily**:
 * - allOf: [[PaddleComponent]]
 * - exclude: [[StickyComponent]]
 */
class PaddleBounceSystem(private val eventManager: GameEventManager<GameEvent>) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.getEventsOfType<BallPaddleHit>()
            .filter { BALL_FAMILY.matches(it.ballEntity) }
            .filter { PADDLE_FAMILY.matches(it.paddleEntity) }
            .forEach { event ->
                val ballC = event.ballEntity[BallComponent::class] ?: return LOG.missingComponent<BallComponent>()
                val ballBodyC = event.ballEntity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()
                val paddleTransformC = event.paddleEntity[TransformComponent::class] ?: return LOG.missingComponent<TransformComponent>()

                val percentageFromBottom = event.paddleContactPoint.y / paddleTransformC.height
                if (percentageFromBottom < 0.25f) {
                    LOG.debug { "Ball hit paddle too low, ignored" }
                    return
                }

                val percentageFromCenter = clamp(event.paddleContactPoint.x / paddleTransformC.width.half, -1f, 1f)
                val angle = BOUNCE_DEGREES_RANGE.byPercentage(-1 * percentageFromCenter).toRadians()

                LOG.debug { "Ball hit paddle, reflected on angle: ${angle.toDegrees()}" }
                ballBodyC.setAngle(angle)
                ballBodyC.setVelocity(velocityOnAngle(ballC.velocity, angle))
            }
    }
}
