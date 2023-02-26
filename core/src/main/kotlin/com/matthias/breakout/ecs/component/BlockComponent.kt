package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class BlockComponent : Component {

    var lives = 1

    companion object {
        val mapper = mapperFor<BlockComponent>()
    }
}