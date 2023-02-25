package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class BallComponent : Component {

    var speed: Float = 0.0f

    companion object {
        val mapper = mapperFor<BallComponent>()
    }
}