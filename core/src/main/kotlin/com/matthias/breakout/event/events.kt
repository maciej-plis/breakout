package com.matthias.breakout.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

sealed interface GameEvent {

    object GameOverEvent : GameEvent

    data class BallPaddleHit(
        val ballEntity: Entity,
        val paddleEntity: Entity,
        val paddleContactPoint: Vector2
    ) : GameEvent

    data class BallWallHit(
        val ballEntity: Entity,
        val wallEntity: Entity,
        val contactNormal: Vector2,
        val ballContactVelocity: Vector2
    ) : GameEvent

    data class BallBlockHit(
        val ballEntity: Entity,
        val blockEntity: Entity,
        val contactNormal: Vector2,
        val ballContactVelocity: Vector2
    ) : GameEvent
}

class GameEventManager<T> {

    val eventQueue = HashSet<T>()

    inline fun <reified U : T> forEachEventOfType(block: (U) -> Unit) {
        eventQueue.filterIsInstance<U>().forEach { block(it) }
    }

    inline fun <reified U : T> getEventsOfType(): List<U> = eventQueue.filterIsInstance<U>()

    inline fun <reified U : T> forEventsOfType(block: (List<U>) -> Unit) {
        val events = getEventsOfType<U>()
        if (events.isNotEmpty()) block(events)
    }

    inline fun <reified U : T> forFirstEventOfType(block: (U) -> Unit) {
        eventQueue.filterIsInstance<U>().firstOrNull()?.let { block(it) }
    }

    fun addEvent(event: T) {
        eventQueue.add(event)
    }

    fun clear() {
        eventQueue.clear()
    }
}