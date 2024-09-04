package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.LEFT
import com.badlogic.gdx.Input.Keys.RIGHT
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.common.toMeters
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.PaddleComponent
import com.matthias.breakout.ecs.component.ShownComponent
import com.matthias.breakout.ecs.component.get
import ktx.ashley.allOf
import ktx.log.logger

private val LOG = logger<PaddleKeyboardMovementSystem>()
private val FAMILY = allOf(PaddleComponent::class, BodyComponent::class, ShownComponent::class).get()

private val PADDLE_SLIDE_SPEED = 0.5f.toMeters()

/**
 * System responsible for controlling paddle with keyboard.
 *
 * --
 *
 * **Family**:
 * - allOf: [[PaddleComponent], [BodyComponent], [ShownComponent]]
 */
class PaddleKeyboardMovementSystem : IteratingSystem(FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()

        if (Gdx.input.isKeyPressed(LEFT)) {
            bodyC.setPosition(bodyC.x - PADDLE_SLIDE_SPEED, bodyC.y)
        } else if (Gdx.input.isKeyPressed(RIGHT)) {
            bodyC.setPosition(bodyC.x + PADDLE_SLIDE_SPEED, bodyC.y)
        }
    }
}
