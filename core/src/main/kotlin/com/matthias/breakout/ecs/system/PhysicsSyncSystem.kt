package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger

private val LOG = logger<PhysicsSystem>()

private val family = allOf(BodyComponent::class, TransformComponent::class).get()

/**
 * **Family**: allOf([BodyComponent], [TransformComponent])
 *
 * **processEntity**: Map body properties to transform component
 */
class PhysicsSyncSystem : IteratingSystem(family) {

    override fun processEntity(entity: Entity, delta: Float) {
        val transformC = entity[TransformComponent.mapper]!!
        val bodyC = entity[BodyComponent.mapper]!!

        transformC.position.set(bodyC.body.position, transformC.position.z)
        transformC.rotationDeg = bodyC.body.angle * radiansToDegrees
    }
}