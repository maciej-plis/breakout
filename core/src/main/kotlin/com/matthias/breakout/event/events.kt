package com.matthias.breakout.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

sealed class GameEvent {

    var handled = false

    data object GameOverEvent : GameEvent()

    data class BallPaddleHit(
        val ballEntity: Entity,
        val paddleEntity: Entity,
        val paddleContactPoint: Vector2
    ) : GameEvent()

    data class BallWallHit(
        val ballEntity: Entity,
        val wallEntity: Entity,
        val contactNormal: Vector2,
        val ballContactVelocity: Vector2
    ) : GameEvent()

    data class BallBlockHit(
        val ballEntity: Entity,
        val blockEntity: Entity,
        val contactNormal: Vector2,
        val ballContactVelocity: Vector2
    ) : GameEvent()
}

class GameEventManager<T : GameEvent> {

    val eventQueue = ArrayList<T>()

    inline fun <reified U : T> getEventsOfType(): Sequence<U> = eventQueue
        .asSequence()
        .filterIsInstance<U>()
        .filter { !it.handled }

    inline fun <reified U : T> forEachEventOfType(block: (U) -> Unit) {
        getEventsOfType<U>().forEach { block(it) }
    }

    inline fun <reified U : T> forEventsOfType(block: (List<U>) -> Unit) {
        val events = getEventsOfType<U>().toList()
        if (events.isNotEmpty()) {
            block(events)
        }
    }

    fun addEvent(event: T) {
        eventQueue.add(event)
    }

    fun clear() {
        eventQueue.clear()
    }
}
