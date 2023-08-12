package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body
import com.matthias.breakout.common.x
import com.matthias.breakout.common.y

class BodyComponent(val body: Body) : Component {
    val x: Float get() = body.x
    val y: Float get() = body.y
    val angle: Float get() = body.angle
}