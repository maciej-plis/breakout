package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.matthias.breakout.common.toMeters
import ktx.ashley.mapperFor

class GraphicComponent : Component {

    val sprite = Sprite()

    fun setSpriteRegion(region: TextureRegion) {
        sprite.run {
            setRegion(region)
            setSize(texture.width.toMeters(), texture.height.toMeters())
            setOriginCenter()
        }
    }

    companion object {
        val mapper = mapperFor<GraphicComponent>()
    }
}