package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.Vector2
import com.matthias.breakout.common.average
import com.matthias.breakout.common.reflect
import com.matthias.breakout.common.toDegrees
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.*
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallBlockHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.log.logger
import kotlin.math.absoluteValue
import kotlin.math.sign

private val LOG = logger<BlockBounceSystem>()

private val BALL_FAMILY = allOf(BallComponent::class, BodyComponent::class).get()
private val BLOCK_FAMILY = allOf(BlockComponent::class).exclude(RemoveComponent::class).get()

/**
 * System responsible for reflecting ball angle based on block contact point.
 * It listens for events of type [BallBlockHit].
 *
 *  --
 *
 * **ballFamily**:
 * - allOf: [[BallComponent], [BodyComponent]]
 *
 * **blockFamily**:
 * - allOf: [[BlockComponent]]
 * - exclude: [[RemoveComponent]]
 */
class BlockBounceSystem(private val eventManager: GameEventManager<GameEvent>) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.getEventsOfType<BallBlockHit>()
            .filter { BALL_FAMILY.matches(it.ballEntity) }
            .filter { BLOCK_FAMILY.matches(it.blockEntity) }
            .groupBy { it.ballEntity }
            .forEach { (ballEntity, events) ->
                val ballC = ballEntity[BallComponent::class]!!
                val bodyC = ballEntity[BodyComponent::class]!!

                val ballVelocity = events.first().ballContactVelocity
                val avgNormal = events.map { it.contactNormal }.average()
                val normal = verticalOrHorizontal(avgNormal)
                val reflectedAngle = ballVelocity.reflect(normal).angleRad()

                LOG.debug { "Ball hit block(s), reflected on angle ${reflectedAngle.toDegrees()}" }
                bodyC.setAngle(reflectedAngle)
                bodyC.setVelocity(velocityOnAngle(ballC.velocity, reflectedAngle))
            }
    }

    private fun verticalOrHorizontal(normal: Vector2): Vector2 {
        val (x, y) = normal.run { x.absoluteValue to y.absoluteValue }
        return if (x > y) Vector2(normal.x.sign, 0f) else Vector2(0f, normal.y.sign)
    }
}