package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.PaddleComponent
import com.matthias.breakout.ecs.component.ShownComponent
import com.matthias.breakout.ecs.component.get
import ktx.ashley.allOf
import ktx.log.logger

private val LOG = logger<PaddleMouseMovementSystem>()
private val FAMILY = allOf(PaddleComponent::class, BodyComponent::class, ShownComponent::class).get()

/**
 * System responsible for controlling paddle with mouse.
 *
 * --
 *
 * **Family**:
 * - allOf: [[PaddleComponent], [BodyComponent], [ShownComponent]]
 */
class PaddleMouseMovementSystem(private val viewport: Viewport) : IteratingSystem(FAMILY) {

    private val lastMousePosition = Vector3()
    private val mousePosition = Vector3()
        get() = field.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)

    override fun processEntity(entity: Entity, delta: Float) {
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()

        viewport.apply(true)
        mousePosition.let { position ->
            if (position == lastMousePosition) return
            lastMousePosition.set(position)
            val onScreenPosition = viewport.camera.unproject(position)
            bodyC.setPosition(onScreenPosition.x, bodyC.y)
        }
    }
}
