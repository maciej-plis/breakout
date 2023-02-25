package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.halfWidth
import com.matthias.breakout.common.x
import com.matthias.breakout.common.y
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.PaddleComponent
import com.matthias.breakout.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class PaddleBoundarySystem(
    private val leftBoundary: Float,
    private val rightBoundary: Float
) : IteratingSystem(allOf(PaddleComponent::class, TransformComponent::class, BodyComponent::class).get()) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent.mapper]!!
        val transformC = entity[TransformComponent.mapper]!!

        val paddle = bodyC.body
        val halfWidth = transformC.size.halfWidth

        if (paddle.x - halfWidth <= leftBoundary) {
            paddle.setTransform(leftBoundary + halfWidth, paddle.y, 0f)
        } else if (paddle.x + halfWidth >= rightBoundary) {
            paddle.setTransform(rightBoundary - halfWidth, paddle.y, 0f)
        }
    }
}