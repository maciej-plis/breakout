package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.PaddleComponent
import com.matthias.breakout.ecs.component.get
import ktx.ashley.allOf

class PaddleMouseMovementSystem(
    private val camera: Camera
) : IteratingSystem(allOf(PaddleComponent::class, BodyComponent::class).get()) {

    private var lastMousePos = getMousePos()

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class]!!

        val paddle = bodyC.body
        val mousePos = getMousePos()

        if (mousePos != lastMousePos) {
            val coords = camera.unproject(Vector3(mousePos.x, mousePos.y, 0f))
            paddle.setTransform(coords.x, paddle.position.y, 0f)
            lastMousePos = mousePos
        }
    }

    private fun getMousePos() = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
}