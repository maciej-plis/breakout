package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.matthias.breakout.assets.SoundAsset.BLOCK_DESTROYED
import com.matthias.breakout.assets.SoundAsset.REINFORCED_BLOCK_HIT
import com.matthias.breakout.audio.AudioService
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BlockComponent
import com.matthias.breakout.ecs.component.RemoveComponent
import com.matthias.breakout.ecs.component.get
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallBlockHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.plusAssign
import ktx.log.logger

private val LOG = logger<BlockDestroySystem>()

private val BALL_FAMILY = allOf(BallComponent::class).get()
private val BLOCK_FAMILY = allOf(BlockComponent::class).exclude(RemoveComponent::class).get()

/**
 * System responsible for decreasing block lives and destroying it when hit by ball.
 *
 * --
 *
 * **ballFamily**:
 * - allOf: [[BallComponent]]
 *
 * **blockFamily**:
 * - allOf: [[BlockComponent]]
 * - exclude: [[RemoveComponent]]
 */
class BlockDestroySystem(
    private val eventManager: GameEventManager<GameEvent>,
    private val audio: AudioService
) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.getEventsOfType<BallBlockHit>()
            .filter { BALL_FAMILY.matches(it.ballEntity) }
            .filter { BLOCK_FAMILY.matches(it.blockEntity) }
            .forEach { event ->
                val blockC = event.blockEntity[BlockComponent::class]!!

                if (--blockC.lives == 0) {
                    LOG.debug { "Block destroyed" }

                    audio.play(BLOCK_DESTROYED)
                    event.blockEntity += RemoveComponent().apply {
                        delay = 0.05f
                    }
                } else {
                    audio.play(REINFORCED_BLOCK_HIT)
                }
            }
    }
}
