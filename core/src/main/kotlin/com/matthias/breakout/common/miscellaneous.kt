package com.matthias.breakout.common

import com.badlogic.gdx.Screen
import ktx.app.KtxGame

inline fun <reified ScreenType : T, T : Screen> KtxGame<T>.setScreen(screen: ScreenType) {
    addScreen(screen)
    setScreen<ScreenType>()
}