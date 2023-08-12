package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.badlogic.gdx.math.Vector2
import com.matthias.breakout.common.average
import com.matthias.breakout.common.reflect
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

private val ballFamily = allOf(BallComponent::class, BodyComponent::class).exclude(RemoveComponent::class, StickyComponent::class).get()
private val blockFamily = allOf(BlockComponent::class).exclude(RemoveComponent::class).get()

class BlockBounceSystem(private val eventManager: GameEventManager<GameEvent>) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.getEventsOfType<BallBlockHit>()
            .filter { ballFamily.matches(it.ballEntity) }
            .filter { blockFamily.matches(it.blockEntity) }
            .groupBy { it.ballEntity }
            .forEach { (ballEntity, events) ->
                val ballC = ballEntity[BallComponent::class]!!
                val bodyC = ballEntity[BodyComponent::class]!!

                val ballVelocity = events.first().ballContactVelocity
                val avgNormal = events.map { it.contactNormal }.average()
                val normal = verticalOrHorizontal(avgNormal)
                val reflectedAngle = ballVelocity.reflect(normal).angleRad()

                LOG.debug { "Ball hit block(s), reflected on angle ${reflectedAngle * radiansToDegrees}" }
                bodyC.body.let { ball ->
                    ball.linearVelocity = velocityOnAngle(ballC.velocity, reflectedAngle)
                    ball.setTransform(ball.position, reflectedAngle)
                }
            }
    }

    private fun verticalOrHorizontal(normal: Vector2): Vector2 {
        val (x, y) = normal.run { x.absoluteValue to y.absoluteValue }
        return if (x > y) Vector2(normal.x.sign, 0f) else Vector2(0f, normal.y.sign)
    }
}