package com.matthias.breakout.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

private val emptyEntity = Entity()

sealed interface GameEvent {

    object GameOverEvent : GameEvent {
        override fun toString() = "${javaClass.simpleName}()"
    }

    class BallPaddleHit : GameEvent {
        var ballEntity: Entity = emptyEntity
        var paddleEntity: Entity = emptyEntity
        val paddleContactPoint = Vector2(0f, 0f)
        override fun toString() = "${javaClass.simpleName}()"
    }

    class BallWallHit : GameEvent {
        var ballEntity: Entity = emptyEntity
        var wallEntity: Entity = emptyEntity
        val contactNormal = Vector2(0f, 0f)
        var ballContactVelocity = Vector2(0f, 0f)
        override fun toString() = "${javaClass.simpleName}()"
    }

    class BallBlockHit : GameEvent {
        var ballEntity: Entity = emptyEntity
        var blockEntity: Entity = emptyEntity
        val contactNormal = Vector2(0f, 0f)
        var ballContactVelocity = Vector2(0f, 0f)
        override fun toString() = "${javaClass.simpleName}()"
    }
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