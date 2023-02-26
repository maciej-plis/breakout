package com.matthias.breakout.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

private val emptyEntity = Entity()

sealed interface GameEvent {

    object GameOverEvent : GameEvent {
        override fun toString() = "${javaClass.simpleName}()"
    }

    object BallPaddleHit : GameEvent {
        val paddleContactPoint = Vector2(0f, 0f)
        override fun toString() = "${javaClass.simpleName}()"
    }

    object BallWallHit : GameEvent {
        val contactNormal = Vector2(0f, 0f)
        var ballContactVelocity = Vector2(0f, 0f)
        override fun toString() = "${javaClass.simpleName}()"
    }

    object BallBlockHit : GameEvent {
        var blockEntity: Entity = emptyEntity
        val contactNormal = Vector2(0f, 0f)
        var ballContactVelocity = Vector2(0f, 0f)
        override fun toString() = "${javaClass.simpleName}()"
    }
}

class GameEventManager<T> {

    val eventQueue = HashSet<T>()

    inline fun <reified U : T> forEvent(block: (U) -> Unit) {
        eventQueue.find { it is U }?.let { block(it as U) }
    }

    fun addEvent(event: T) {
        eventQueue.add(event)
    }

    fun clear() {
        eventQueue.clear()
    }
}