package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.matthias.breakout.ecs.component.BlockComponent
import com.matthias.breakout.ecs.component.RemoveComponent
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallBlockHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.get
import ktx.ashley.plusAssign
import ktx.log.logger

private val LOG = logger<BlockDestroySystem>()

class BlockDestroySystem(private val eventManager: GameEventManager<GameEvent>) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.forEachEventOfType<BallBlockHit> { event ->
            val blockC = event.blockEntity[BlockComponent.mapper]!!

            if (--blockC.lives == 0) {
                LOG.debug { "Block destroyed" }

                event.blockEntity += RemoveComponent().apply {
                    delay = 0.05f
                }
            }
        }
    }
}