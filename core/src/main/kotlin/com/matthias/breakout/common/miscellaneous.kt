package com.matthias.breakout.common

import com.badlogic.gdx.Screen
import com.badlogic.gdx.math.MathUtils.cos
import com.badlogic.gdx.math.MathUtils.sin
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.matthias.breakout.PPM
import ktx.app.KtxGame

inline fun <reified ScreenType : T, T : Screen> KtxGame<T>.setScreen(screen: ScreenType) {
    addScreen(screen)
    setScreen<ScreenType>()
}

val Int.half get() = this / 2
val Float.half get() = this / 2

fun Int.toMeters() = this / PPM
fun Float.toMeters() = this / PPM

fun Int.toPixels() = this * PPM
fun Float.toPixels() = this * PPM

fun Vector2.toMeters() = scl(1 / PPM)
fun Vector2.toPixels() = scl(PPM)

val Vector2.width get() = x
val Vector2.height get() = y

val Vector2.halfWidth get() = width / 2
val Vector2.halfHeight get() = height / 2

val Body.x get() = position.x
val Body.y get() = position.y

fun velocityOnAngle(velocity: Float, angle: Float) = Vector2(cos(angle) * velocity, sin(angle) * velocity)
fun Vector2.reflect(normal: Vector2) = sub(normal.scl(2 * dot(normal)))