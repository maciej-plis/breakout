package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.half
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.ecs.component.*
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.GameOverEvent
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.plusAssign
import ktx.log.logger

private val LOG = logger<GameOverSystem>()
private val BALL_FAMILY = allOf(BallComponent::class, TransformComponent::class, BodyComponent::class).exclude(RemoveComponent::class, AttachComponent::class).get()
private val PADDLE_FAMILY = allOf(PaddleComponent::class).get()

/**
 * System responsible for handling balls that fall under paddle
 *
 * --
 *
 * **Family**:
 * - allOf: [[BallComponent], [TransformComponent], [BodyComponent]]
 * - exclude: [[RemoveComponent], [AttachComponent]]
 */
class GameOverSystem(
    private val eventManager: GameEventManager<GameEvent>,
    private val bottomBoundary: Float
) : IteratingSystem(BALL_FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()
        val transformC = entity[TransformComponent::class] ?: return LOG.missingComponent<TransformComponent>()

        val halfHeight = transformC.height.half

        if (bodyC.y + halfHeight <= bottomBoundary) {
            LOG.debug { "Removing ball" }
            entity += RemoveComponent()
            eventManager.addEvent(GameOverEvent)

            engine.getEntitiesFor(BALL_FAMILY).forEach { println(it) }
            println(entity)

            if (engine.getEntitiesFor(BALL_FAMILY).filterNot { it == entity }.none()) {
                engine.getEntitiesFor(PADDLE_FAMILY).forEach { it += HideComponent() }
            }
        }
    }
}
