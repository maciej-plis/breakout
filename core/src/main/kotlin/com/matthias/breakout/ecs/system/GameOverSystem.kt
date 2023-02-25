package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Signal
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.halfHeight
import com.matthias.breakout.common.y
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.RemoveComponent
import com.matthias.breakout.ecs.component.TransformComponent
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.GameOverEvent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.plusAssign

class GameOverSystem(
    private val eventManager: Signal<GameEvent>,
    private val bottomBoundary: Float
) : IteratingSystem(allOf(BallComponent::class, TransformComponent::class, BodyComponent::class).get()) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent.mapper]!!
        val transformC = entity[TransformComponent.mapper]!!

        val ball = bodyC.body
        val halfHeight = transformC.size.halfHeight

        if (ball.y + halfHeight <= bottomBoundary) {
            entity += RemoveComponent()
            eventManager.dispatch(GameOverEvent)
        }
    }
}