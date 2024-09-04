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
import kotlin.math.min

private val LOG = logger<PaddleShowSystem>()
private val FAMILY = allOf(PaddleComponent::class, BodyComponent::class, ShowComponent::class).get()

private val TOP_POSITION = 1.5f.toMeters()
private val BOTTOM_POSITION = (-3f).toMeters()
private val TRANSITION_SPEED = 0.25f.toMeters()

/**
 * System responsible for showing paddle.
 *
 * --
 *
 * **Family**:
 * - allOf: [[PaddleComponent], [BodyComponent], [ShowComponent]]
 */
class PaddleShowSystem : IteratingSystem(FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()

        val newY = bodyC.y + TRANSITION_SPEED
        bodyC.setPosition(bodyC.x, min(newY, TOP_POSITION))

        if (bodyC.y == TOP_POSITION) {
            LOG.debug { "Paddle fully shown" }
            entity.remove<ShowComponent>()
            entity += ShownComponent()
        }
    }
}
