package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class StickyComponent : Component {

    companion object {
        val mapper = mapperFor<StickyComponent>()
    }
}