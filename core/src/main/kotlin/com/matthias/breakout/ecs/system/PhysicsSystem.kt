package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.badlogic.gdx.physics.box2d.World
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger

private val LOG = logger<PhysicsSystem>()

private const val VELOCITY_ITERATIONS = 8
private const val POSITION_ITERATIONS = 3

private val family = allOf(BodyComponent::class, TransformComponent::class).get()

/**
 * **Family**: allOf([BodyComponent], [TransformComponent])
 *
 * **update**: Run physics step
 *
 * **processEntity**: Map body properties to transform component
 */
class PhysicsSystem(private val world: World) : IteratingSystem(family) {

    override fun update(delta: Float) {
        world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS)
        super.update(delta)
    }

    override fun processEntity(entity: Entity, delta: Float) {
        val transformC = entity[TransformComponent.mapper]!!
        val bodyC = entity[BodyComponent.mapper]!!

        transformC.position.set(bodyC.body.position, transformC.position.z)
        transformC.rotationDeg = bodyC.body.angle * radiansToDegrees
    }
}