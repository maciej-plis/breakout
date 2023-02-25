package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Input.Keys.LEFT
import com.badlogic.gdx.Input.Keys.RIGHT
import com.matthias.breakout.common.x
import com.matthias.breakout.common.y
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.PaddleComponent
import ktx.ashley.allOf
import ktx.ashley.get

class PaddleMovementSystem : IteratingSystem(allOf(PaddleComponent::class, BodyComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val paddleC = entity[PaddleComponent.mapper]!!
        val bodyC = entity[BodyComponent.mapper]!!

        val paddle = bodyC.body

        if (paddle.userData == LEFT) {
            paddle.setTransform(paddle.x - paddleC.speed, paddle.y, 0f)
        } else if (paddle.userData == RIGHT) {
            paddle.setTransform(paddle.x + paddleC.speed, paddle.y, 0f)
        }
    }
}