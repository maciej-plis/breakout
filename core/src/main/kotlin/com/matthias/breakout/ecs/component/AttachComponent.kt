package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import ktx.ashley.mapperFor

class AttachComponent : Component {
    var attachedToEntity: Entity? = null
    var offset: Vector2 = Vector2()

    companion object {
        val mapper = mapperFor<AttachComponent>()
    }
}