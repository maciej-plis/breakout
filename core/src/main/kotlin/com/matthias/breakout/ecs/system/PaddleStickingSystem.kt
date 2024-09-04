package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils.clamp
import com.badlogic.gdx.math.Vector2
import com.matthias.breakout.common.halfHeight
import com.matthias.breakout.common.halfWidth
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.common.x
import com.matthias.breakout.ecs.component.*
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallPaddleHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.plusAssign
import ktx.log.logger


private val LOG = logger<PaddleStickingSystem>()

private val ballFamily = allOf(BallComponent::class).exclude(AttachComponent::class).get()
private val paddleFamily = allOf(PaddleComponent::class, StickyComponent::class).get()

/**
 * System responsible for attaching ball to paddle when hit.
 *
 *  --
 *
 *  **ballFamily**:
 *  - allOf: [[BallComponent]]
 *  - exclude: [[AttachComponent]]
 *
 *  **paddleFamily**:
 *  - allOf: [[PaddleComponent], [StickyComponent]]
 */
class PaddleStickingSystem(private val eventManager: GameEventManager<GameEvent>) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.getEventsOfType<BallPaddleHit>()
            .filter { ballFamily.matches(it.ballEntity) }
            .filter { paddleFamily.matches(it.paddleEntity) }
            .forEach { event ->
                val paddleC = event.paddleEntity[PaddleComponent::class] ?: return LOG.missingComponent<PaddleComponent>()
                paddleC.stickingBalls.add(event.ballEntity)
                event.ballEntity += AttachComponent().apply {
                    targetEntity = event.paddleEntity
                    offset = calculateOffset(event.ballEntity, event.paddleEntity)
                }
            }
    }

    private fun calculateOffset(entity1: Entity, entity2: Entity): Vector2 {
        val body1 = entity1[BodyComponent::class]!!.body
        val transform1 = entity1[TransformComponent::class]!!

        val body2 = entity2[BodyComponent::class]!!.body
        val transform2 = entity2[TransformComponent::class]!!

        val x = clamp(body1.x, body2.x - transform2.size.halfWidth + transform1.size.halfWidth, body2.x + transform2.size.halfWidth - transform1.size.halfWidth)

        return Vector2(x - body2.x, transform1.size.halfHeight + transform2.size.halfHeight)
    }
}
