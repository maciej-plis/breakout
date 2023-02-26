package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.ecs.component.BlockComponent
import com.matthias.breakout.ecs.component.RemoveComponent
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallBlockHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.plusAssign

class BlockDestroySystem(
    private val eventManager: GameEventManager<GameEvent>
) : IteratingSystem(allOf(BlockComponent::class).get()) {

    override fun processEntity(entity: Entity, delta: Float) {
        eventManager.forEvent<BallBlockHit> { event ->
            val blockC = entity[BlockComponent.mapper]!!

            if (--blockC.lives <= 0) {
                event.blockEntity += RemoveComponent()
            }
        }
    }
}