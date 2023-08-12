package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.matthias.breakout.common.half
import com.matthias.breakout.common.height
import com.matthias.breakout.common.width

class TransformComponent : Component, Comparable<TransformComponent> {

    val position = Vector3()
    val size = Vector2(1f, 1f)
    val scale = Vector2(1f, 1f)
    var angleDeg = 0f

    // @formatter:off
    var x: Float get() = position.x; set(value) { position.x = value }
    var y: Float get() = position.y; set(value) { position.y = value }
    var layer: Float get() = position.z; set(value) { position.z = value }
    var width: Float get() = size.width; set(value) { size.width = value }
    var height: Float get() = size.height; set(value) { size.height = value }
    // @formatter:on

    val rect = Rectangle(x, y, width, height)
    val bottomLeftX: Float get() = x - width.half
    val bottomLeftY: Float get() = y - height.half

    fun setInitialPosition(x: Float, y: Float, layer: Float) {
        position.set(x, y, layer)
        rect.setPosition(x, y)
    }

    fun setPosition(x: Float, y: Float) {
        position.set(x, y, layer)
        rect.setPosition(x, y)
    }

    fun setSize(width: Float, height: Float) {
        size.set(width, height)
        rect.setSize(width, height)
    }

    override fun compareTo(other: TransformComponent): Int {
        val layerDiff = layer.compareTo(other.layer)
        return if (layerDiff != 0) layerDiff else y.compareTo(other.y)
    }
}

