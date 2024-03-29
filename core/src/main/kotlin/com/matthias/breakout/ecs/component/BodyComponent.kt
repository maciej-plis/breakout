package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.matthias.breakout.common.toDegrees
import com.matthias.breakout.common.x
import com.matthias.breakout.common.y

class BodyComponent(val body: Body) : Component {

    val x: Float get() = body.x
    val y: Float get() = body.y
    val angleRad: Float get() = body.angle
    val angleDeg: Float get() = body.angle.toDegrees()

    fun setPosition(x: Float, y: Float) = body.setTransform(x, y, angleRad)
    fun setPosition(position: Vector2) = body.setTransform(position, angleRad)
    fun setAngle(angleRad: Float) = body.setTransform(x, y, angleRad)
    fun setVelocity(velX: Float, velY: Float) = body.setLinearVelocity(velX, velY)
    fun setVelocity(velocity: Vector2) = body.setLinearVelocity(velocity)
}