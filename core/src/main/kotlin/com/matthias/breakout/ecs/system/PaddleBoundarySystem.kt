package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.half
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.PaddleComponent
import com.matthias.breakout.ecs.component.TransformComponent
import com.matthias.breakout.ecs.component.get
import ktx.ashley.allOf
import ktx.log.logger

private val LOG = logger<PaddleBoundarySystem>()
private val FAMILY = allOf(PaddleComponent::class, TransformComponent::class, BodyComponent::class).get()

/**
 * System responsible for restricting paddle from going outside boundaries.
 *
 * --
 *
 * **Family**:
 * - allOf: [[PaddleComponent], [TransformComponent], [BodyComponent]]
 */
class PaddleBoundarySystem(
    private val leftBoundary: Float,
    private val rightBoundary: Float
) : IteratingSystem(FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()
        val transformC = entity[TransformComponent::class] ?: return LOG.missingComponent<TransformComponent>()

        val halfWidth = transformC.width.half

        if (bodyC.x - halfWidth <= leftBoundary) {
            bodyC.setPosition(leftBoundary + halfWidth, bodyC.y)
        } else if (bodyC.x + halfWidth >= rightBoundary) {
            bodyC.setPosition(rightBoundary - halfWidth, bodyC.y)
        }
    }
}