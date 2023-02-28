package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.matthias.breakout.common.reflect
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallBlockHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.get

class BlockBounceSystem(private val eventManager: GameEventManager<GameEvent>) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.forEventsOfType<BallBlockHit> { event ->
            val ballC = event.ballEntity[BallComponent.mapper]!!
            val bodyC = event.ballEntity[BodyComponent.mapper]!!

            val reflectedAngle = event.ballContactVelocity.reflect(event.contactNormal).angleRad()

            bodyC.body.let { ball ->
                ball.setTransform(ball.position, reflectedAngle)
                ball.linearVelocity = velocityOnAngle(ballC.velocity, reflectedAngle)
            }
        }
    }
}