package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.collections.gdxArrayOf

class PaddleComponent : Component {
    val stickingBalls = gdxArrayOf<Entity>()
}
