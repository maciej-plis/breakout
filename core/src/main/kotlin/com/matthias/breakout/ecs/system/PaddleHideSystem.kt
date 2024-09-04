package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.common.toMeters
import com.matthias.breakout.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.plusAssign
import ktx.ashley.remove
import ktx.log.logger
import kotlin.math.max

private val LOG = logger<PaddleHideSystem>()
private val FAMILY = allOf(PaddleComponent::class, BodyComponent::class, HideComponent::class).get()

private val TOP_POSITION = 1.5f.toMeters()
private val BOTTOM_POSITION = (-3f).toMeters()
private val TRANSITION_SPEED = 0.25f.toMeters()

/**
 * System responsible for hiding paddle.
 *
 * --
 *
 * **Family**:
 * - allOf: [[PaddleComponent], [BodyComponent], [HideComponent]]
 */
class PaddleHideSystem : IteratingSystem(FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()

        val newY = bodyC.y - TRANSITION_SPEED
        bodyC.setPosition(bodyC.x, max(newY, BOTTOM_POSITION))

        if (bodyC.y == BOTTOM_POSITION) {
            LOG.debug { "Paddle fully hidden" }
            entity.remove<HideComponent>()
            entity += HiddenComponent()
        }
    }
}
