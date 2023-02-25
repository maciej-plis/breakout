package com.matthias.breakout.event

sealed interface GameEvent {
    object GameOverEvent : GameEvent {
        override fun toString() = "${javaClass.simpleName}()"
    }
}