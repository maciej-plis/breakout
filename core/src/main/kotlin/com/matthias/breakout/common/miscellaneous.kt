package com.matthias.breakout.common

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.GdxRuntimeException
import com.matthias.breakout.PPM
import ktx.log.Logger
import ktx.math.div

val Int.half get() = this / 2
fun Int.toPixels() = this * PPM
fun Int.toMeters() = this / PPM

val Float.half get() = this / 2
fun Float.toPixels() = this * PPM
fun Float.toMeters() = this / PPM
fun Float.toRadians() = this * degreesToRadians
fun Float.toDegrees() = this * radiansToDegrees

fun Vector2.toMeters() = scl(1 / PPM)
fun Vector2.toPixels() = scl(PPM)

// @formatter:off
var Vector2.width get() = x; set(value) { x = value }
val Vector2.halfWidth get() = width / 2
var Vector2.height get() = y; set(value) { y = value }
val Vector2.halfHeight get() = height / 2
fun List<Vector2>.average() = fold(Vector2()) { acc, vector -> acc.add(vector) }.div(size)
// @formatter:on

val Body.x get() = position.x
val Body.y get() = position.y

fun ClosedRange<Float>.byPercentage(percentage: Float) = start + percentage * (endInclusive - start)
operator fun Float.compareTo(range: ClosedRange<Float>) = if (this in range) 0 else if (this < range.start) -1 else 1

fun velocityOnAngle(velocity: Float, angle: Float) = Vector2(cos(angle) * velocity, sin(angle) * velocity)
fun Vector2.reflect(normal: Vector2) = sub(normal.scl(2 * dot(normal)))

inline fun <T : Actor> T.onMove(crossinline listener: T.() -> Boolean?): InputListener {
    val clickListener = object : ClickListener() {
        override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
            return listener() ?: super.mouseMoved(event, x, y)
        }
    }
    addListener(clickListener)
    return clickListener
}

operator fun TextureAtlas.get(name: String) = findRegion(name) ?: throw GdxRuntimeException("Region '$name' not found in atlas")

inline fun <reified T> Logger.missingComponent() = this.error { "Illegal state occurred. Expected entity to have ${T::class.simpleName}." }