package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.matthias.breakout.assets.SoundAsset.PADDLE_HIT
import com.matthias.breakout.audio.AudioService
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.common.reflect
import com.matthias.breakout.common.toDegrees
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.WallComponent
import com.matthias.breakout.ecs.component.get
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallWallHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.log.logger

private val LOG = logger<WallBounceSystem>()

private val BALL_FAMILY = allOf(BallComponent::class, BodyComponent::class).get()
private val WALL_FAMILY = allOf(WallComponent::class).get()


/**
 * System responsible for reflecting ball angle based on wall contact point.
 * It listens for events of type [BallWallHit].
 *
 * --
 *
 * **ballFamily**:
 * - allOf: [[BallComponent], [BodyComponent]]
 *
 * **wallFamily**:
 * - allOf: [[WallComponent]]
 */
class WallBounceSystem(
    private val eventManager: GameEventManager<GameEvent>,
    private val audio: AudioService
) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.getEventsOfType<BallWallHit>()
            .filter { BALL_FAMILY.matches(it.ballEntity) }
            .filter { WALL_FAMILY.matches(it.wallEntity) }
            .forEach { event ->
                val ballC = event.ballEntity[BallComponent::class] ?: return LOG.missingComponent<BallComponent>()
                val bodyC = event.ballEntity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()

                val reflectedAngle = event.ballContactVelocity.reflect(event.contactNormal).angleRad()

                LOG.debug { "Ball hit wall, reflected on angle ${reflectedAngle.toDegrees()}" }

                audio.play(PADDLE_HIT)
                bodyC.setAngle(reflectedAngle)
                bodyC.setVelocity(velocityOnAngle(ballC.velocity, reflectedAngle))
            }
    }
}
