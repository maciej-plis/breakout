package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.matthias.breakout.common.reflect
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.*
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallWallHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.log.logger

private val LOG = logger<WallBounceSystem>()

private val ballFamily = allOf(BallComponent::class, BodyComponent::class).exclude(RemoveComponent::class, StickyComponent::class).get()
private val wallFamily = allOf(WallComponent::class).get()

class WallBounceSystem(private val eventManager: GameEventManager<GameEvent>) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.getEventsOfType<BallWallHit>()
            .filter { ballFamily.matches(it.ballEntity) }
            .filter { wallFamily.matches(it.wallEntity) }
            .forEach { event ->
                val ballC = event.ballEntity[BallComponent.mapper]!!
                val bodyC = event.ballEntity[BodyComponent.mapper]!!

                val reflectedAngle = event.ballContactVelocity.reflect(event.contactNormal).angleRad()

                LOG.debug { "Ball hit wall, reflected on angle ${reflectedAngle * radiansToDegrees}" }
                bodyC.body.let { ball ->
                    ball.linearVelocity = velocityOnAngle(ballC.velocity, reflectedAngle)
                    ball.setTransform(ball.position, reflectedAngle)
                }
            }
    }
}