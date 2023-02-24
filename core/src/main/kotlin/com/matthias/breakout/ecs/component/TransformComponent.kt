package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class TransformComponent : Component, Comparable<TransformComponent> {

    val position = Vector3()
    val previousPosition = Vector3()
    val interpolatedPosition = Vector3()

    val size = Vector2(1f, 1f)
    val scale = Vector2(1f, 1f)

    val rotationDeg = 0f

    fun setInitialPosition(x: Float, y: Float, z: Float) {
        position.set(x, y, z)
        previousPosition.set(x, y, z)
        interpolatedPosition.set(x, y, z)
    }

    override fun compareTo(other: TransformComponent): Int {
        val zDiff = position.z.compareTo(other.position.z)
        return if (zDiff != 0) zDiff else position.y.compareTo(other.position.y)
    }
}