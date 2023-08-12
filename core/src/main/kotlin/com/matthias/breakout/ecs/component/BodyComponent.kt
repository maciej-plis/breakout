package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body

class BodyComponent(val body: Body) : Component {
}