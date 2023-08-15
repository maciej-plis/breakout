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
import ktx.ashley.plusAssign
import ktx.log.logger

private val LOG = logger<GameOverSystem>()
private val FAMILY = allOf(BallComponent::class, TransformComponent::class, BodyComponent::class).get()

/**
 * System responsible for handling balls that fall under paddle
 *
 * --
 *
 * **Family**:
 * - allOf: [[BallComponent], [TransformComponent], [BodyComponent]]
 */
class GameOverSystem(
    private val eventManager: GameEventManager<GameEvent>,
    private val bottomBoundary: Float
) : IteratingSystem(FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()
        val transformC = entity[TransformComponent::class] ?: return LOG.missingComponent<TransformComponent>()

        val halfHeight = transformC.height.half

        if (bodyC.y + halfHeight <= bottomBoundary) {
            entity += RemoveComponent()
            eventManager.addEvent(GameOverEvent)
        }
    }
}