package com.matthias.breakout.event

import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.GdxSet
import ktx.collections.getOrPut
import kotlin.reflect.KClass

typealias GameEventType = KClass<out GameEvent>

sealed interface GameEvent {
    object GameOverEvent : GameEvent {
        override fun toString() = "${javaClass.simpleName}()"
    }
}

interface GameEventListener {
    fun onEvent(event: GameEvent)
}

class GameEventManager {

    private val listeners = ObjectMap<GameEventType, GdxSet<GameEventListener>>()

    fun addListener(type: GameEventType, listener: GameEventListener) {
        listeners.getOrPut(type, { GdxSet() }).add(listener)
    }

    fun removeListener(type: GameEventType, listener: GameEventListener) {
        listeners[type]?.remove(listener)
    }

    fun removeListener(listener: GameEventListener) {
        listeners.values().forEach { it.remove(listener) }
    }

    fun dispatchEvent(event: GameEvent) {
        listeners[event::class].forEach { it.onEvent(event) }
    }
}