package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.common.toMeters
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.HideComponent
import com.matthias.breakout.ecs.component.PaddleComponent
import com.matthias.breakout.ecs.component.get
import ktx.ashley.allOf
import ktx.log.logger
import kotlin.math.max
import kotlin.math.min

private val LOG = logger<PaddleBoundarySystem>()
private val FAMILY = allOf(PaddleComponent::class, BodyComponent::class).get()

private val TOP_POSITION = 1.5f.toMeters()
private val BOTTOM_POSITION = (-3f).toMeters()
private val TRANSITION_SPEED = 0.25f.toMeters()

/**
 * System responsible for lifting and lowering paddle when hidden.
 *
 * --
 *
 * **Family**:
 * - allOf: [[PaddleComponent], [BodyComponent]]
 */
class PaddleLiftSystem(

) : IteratingSystem(FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()
        val isHidden = entity[HideComponent::class] != null

        if (isHidden) {
            val newY = bodyC.y - TRANSITION_SPEED
            bodyC.setPosition(bodyC.x, max(newY, BOTTOM_POSITION))
        } else {
            val newY = bodyC.y + TRANSITION_SPEED
            bodyC.setPosition(bodyC.x, min(newY, TOP_POSITION))
        }
    }
}
