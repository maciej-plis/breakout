package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.matthias.breakout.common.half
import com.matthias.breakout.common.height
import com.matthias.breakout.common.width

class TransformComponent : Component, Comparable<TransformComponent> {

    val position = Vector2()
    val size = Vector2(1f, 1f)
    val scale = Vector2(1f, 1f)
    var layer = 0
    var angleDeg = 0f

    var x by position::x
    var y by position::y
    var width by size::width
    var height by size::height

    // @formatter:off
    var bottomLeftX: Float get() = x - width.half; set(value) { x = value + width.half }
    var bottomLeftY: Float get() = y - height.half; set(value) { y = value + height.half }
    // @formatter:on

    fun setPosition(x: Float, y: Float) = position.set(x, y)
    fun setSize(width: Float, height: Float) = size.set(width, height)

    override fun compareTo(other: TransformComponent): Int {
        val layerDiff = layer.compareTo(other.layer)
        return if (layerDiff != 0) layerDiff else y.compareTo(other.y)
    }
}

