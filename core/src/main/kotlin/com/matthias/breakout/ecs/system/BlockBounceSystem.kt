package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.reflect
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallBlockHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.get

class BlockBounceSystem(
    private val eventManager: GameEventManager<GameEvent>
) : IteratingSystem(allOf(BallComponent::class, BodyComponent::class).get()) {

    override fun processEntity(entity: Entity, delta: Float) {
        eventManager.forEvent<BallBlockHit> { event ->
            val ballC = entity[BallComponent.mapper]!!
            val bodyC = entity[BodyComponent.mapper]!!

            val reflectedAngle = event.ballContactVelocity.reflect(event.contactNormal).angleRad()

            bodyC.body.let { ball ->
                ball.setTransform(ball.position, reflectedAngle)
                ball.linearVelocity = velocityOnAngle(ballC.velocity, reflectedAngle)
            }
        }
    }
}