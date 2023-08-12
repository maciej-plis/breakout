package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

class AttachComponent : Component {
    var targetEntity: Entity? = null
    var offset: Vector2 = Vector2()
}