package com.matthias.breakout.common

import com.badlogic.gdx.Screen
import ktx.app.KtxGame

inline fun <reified T : Screen> KtxGame<T>.setScreen(screen: T) {
    addScreen(screen)
    setScreen<T>()
}