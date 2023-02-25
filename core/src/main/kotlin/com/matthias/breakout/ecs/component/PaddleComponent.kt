package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class PaddleComponent : Component {
    var speed: Float = 0.0f

    companion object {
        val mapper = mapperFor<PaddleComponent>()
    }
}