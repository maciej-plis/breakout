package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.World
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.RemoveComponent
import com.matthias.breakout.ecs.component.get
import ktx.ashley.allOf
import ktx.log.logger

private val LOG = logger<RemoveSystem>()
private val FAMILY = allOf(RemoveComponent::class).get()

/**
 * System responsible for removing entities that have been marked with [RemoveComponent].
 * When [RemoveComponent.delay] has passed entity is removed from engine.
 * Additionally, if entity contains [BodyComponent] its body will be destroyed.
 *
 * --
 *
 * **Family**:
 * - allOf: [[RemoveComponent]]
 */
class RemoveSystem(private val world: World) : IteratingSystem(FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        val removeC = entity[RemoveComponent::class] ?: return LOG.missingComponent<RemoveComponent>()

        removeC.delay -= delta

        if (removeC.delay <= 0f) {
            engine.removeEntity(entity)
            entity[BodyComponent::class]?.let { world.destroyBody(it.body) }
        }
    }
}