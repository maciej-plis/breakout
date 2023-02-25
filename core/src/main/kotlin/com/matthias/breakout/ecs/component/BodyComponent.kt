package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body
import ktx.ashley.mapperFor

class BodyComponent(val body: Body) : Component {

    companion object {
        val mapper = mapperFor<BodyComponent>()
    }
}