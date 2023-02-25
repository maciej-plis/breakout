package com.matthias.breakout.common

import com.badlogic.gdx.Screen
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
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

val Vector2.width get() = x
val Vector2.height get() = y

val Vector2.halfWidth get() = x / 2
val Vector2.halfHeight get() = y / 2

val Body.x get() = position.x
val Body.y get() = position.y