package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class RemoveComponent : Component {

    var delay: Float = 0f

    companion object {
        val mapper = mapperFor<RemoveComponent>()
    }
}