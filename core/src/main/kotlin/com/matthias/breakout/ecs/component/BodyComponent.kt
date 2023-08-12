package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.matthias.breakout.common.x
import com.matthias.breakout.common.y

class BodyComponent(val body: Body) : Component {

    val x: Float get() = body.x
    val y: Float get() = body.y
    val angleRad: Float get() = body.angle
    val angleDeg: Float get() = body.angle * radiansToDegrees

    fun setPosition(x: Float, y: Float) = body.setTransform(x, y, angleRad)
    fun setPosition(position: Vector2) = body.setTransform(position, angleRad)
    fun setAngle(angleRad: Float) = body.setTransform(x, y, angleRad)
}