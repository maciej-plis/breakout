package com.matthias.breakout.common

import com.badlogic.gdx.Screen
import com.matthias.breakout.PPM
import ktx.app.KtxGame

inline fun <reified ScreenType : T, T : Screen> KtxGame<T>.setScreen(screen: ScreenType) {
    addScreen(screen)
    setScreen<ScreenType>()
}

fun Int.toMeters() = this / PPM
fun Float.toMeters() = this / PPM

fun Int.toPixels() = this * PPM
fun Float.toPixels() = this * PPM