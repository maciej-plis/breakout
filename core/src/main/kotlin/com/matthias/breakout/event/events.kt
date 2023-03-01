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

    inline fun <reified U : T> forEventsOfType(block: (U) -> Unit) {
        eventQueue.filterIsInstance<U>().forEach { block(it) }
    }

    fun addEvent(event: T) {
        eventQueue.add(event)
    }

    fun clear() {
        eventQueue.clear()
    }
}