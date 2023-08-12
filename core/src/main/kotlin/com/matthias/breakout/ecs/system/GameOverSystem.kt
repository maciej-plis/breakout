package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.halfHeight
import com.matthias.breakout.common.y
import com.matthias.breakout.ecs.component.*
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.GameOverEvent
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.plusAssign

class GameOverSystem(
    private val eventManager: GameEventManager<GameEvent>,
    private val bottomBoundary: Float
) : IteratingSystem(allOf(BallComponent::class, TransformComponent::class, BodyComponent::class).get()) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class]!!
        val transformC = entity[TransformComponent::class]!!

        val ball = bodyC.body
        val halfHeight = transformC.size.halfHeight

        if (ball.y + halfHeight <= bottomBoundary) {
            entity += RemoveComponent()
            eventManager.addEvent(GameOverEvent)
        }
    }
}