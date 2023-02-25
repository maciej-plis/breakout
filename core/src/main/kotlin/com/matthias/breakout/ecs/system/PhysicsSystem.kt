package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.World
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class PhysicsSystem(private val world: World) : IteratingSystem(allOf(BodyComponent::class, TransformComponent::class).get()) {

    override fun update(delta: Float) {
        world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS)
        super.update(delta)
    }

    override fun processEntity(entity: Entity, delta: Float) {
        val transformC = entity[TransformComponent.mapper]!!
        val bodyC = entity[BodyComponent.mapper]!!

        transformC.position.set(bodyC.body.position, transformC.position.z)
    }

    companion object {
        private const val VELOCITY_ITERATIONS = 8
        private const val POSITION_ITERATIONS = 3
    }
}