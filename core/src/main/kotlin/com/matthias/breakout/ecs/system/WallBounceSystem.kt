package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.reflect
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallWallHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.get

class WallBounceSystem(
    private val eventManager: GameEventManager<GameEvent>
) : IteratingSystem(allOf(BallComponent::class, BodyComponent::class).get()) {

    override fun processEntity(entity: Entity, delta: Float) {
        eventManager.forEvent<BallWallHit> { event ->
            val bodyC = entity[BodyComponent.mapper]!!

            bodyC.body.let { ball ->
                ball.linearVelocity = event.ballContactVelocity.reflect(event.contactNormal)
                ball.setTransform(ball.position, ball.linearVelocity.angleRad())
            }
        }
    }
}