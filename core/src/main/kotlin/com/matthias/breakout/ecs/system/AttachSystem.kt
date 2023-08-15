package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.ecs.component.AttachComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.get
import ktx.ashley.allOf
import ktx.log.logger

private val LOG = logger<AttachSystem>()
private val FAMILY = allOf(AttachComponent::class, BodyComponent::class).get()

/**
 * System responsible for keeping entity position in specified offset from another entity.
 * Basically gluing them together.
 *
 * --
 *
 * **Family**:
 * - allOf: [[AttachComponent], [BodyComponent]]
 */
class AttachSystem : IteratingSystem(FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        val attachC = entity[AttachComponent::class] ?: return LOG.missingComponent<AttachComponent>()
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()

        attachC.targetEntity?.get(BodyComponent::class)?.let { targetBody ->
            bodyC.setPosition(
                targetBody.x + attachC.offset.x,
                targetBody.y + attachC.offset.y
            )
        }
    }
}