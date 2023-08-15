package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.TransformComponent
import com.matthias.breakout.ecs.component.get
import ktx.ashley.allOf
import ktx.log.logger

private val LOG = logger<PhysicsSystem>()

private val family = allOf(BodyComponent::class, TransformComponent::class).get()

/**
 * System responsible for mapping box2D state from [BodyComponent] to [TransformComponent].
 *
 * --
 *
 * **Family**:
 * - allOf: [[BodyComponent], [TransformComponent]]
 */
class PhysicsSyncSystem : IteratingSystem(family) {

    override fun processEntity(entity: Entity, delta: Float) {
        val transformC = entity[TransformComponent::class] ?: return LOG.missingComponent<TransformComponent>()
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()

        transformC.setPosition(bodyC.x, bodyC.y)
        transformC.angleDeg = bodyC.angleRad * radiansToDegrees
    }
}