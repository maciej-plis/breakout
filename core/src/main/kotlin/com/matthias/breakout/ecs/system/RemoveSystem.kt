package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.World
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.RemoveComponent
import com.matthias.breakout.ecs.component.get
import ktx.ashley.allOf

class RemoveSystem(private val world: World) : IteratingSystem(allOf(RemoveComponent::class).get()) {

    override fun processEntity(entity: Entity, delta: Float) {
        val removeC = entity[RemoveComponent::class]!!
        val bodyC = entity[BodyComponent::class]

        removeC.delay -= delta
        if (removeC.delay <= 0f) {
            engine.removeEntity(entity)
            bodyC?.let { world.destroyBody(it.body) }
        }
    }
}