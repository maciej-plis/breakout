package com.matthias.breakout.ecs.system


import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.ecs.component.GraphicComponent
import com.matthias.breakout.ecs.component.TransformComponent
import com.matthias.breakout.ecs.component.get
import ktx.ashley.allOf
import ktx.graphics.use
import ktx.log.logger

private val LOG = logger<RenderSystem>()

private val FAMILY = allOf(TransformComponent::class, GraphicComponent::class).get()
private val FAMILY_COMPARATOR = compareBy<Entity> { entity -> entity[TransformComponent::class] }

/**
 * System responsible for drawing entity textures.
 * Entities are rendered in order using [TransformComponent.layer].
 *
 * --
 *
 * **Family**:
 * - allOf: [[TransformComponent], [GraphicComponent]]
 */
class RenderSystem(private val batch: Batch, private val gameViewport: Viewport) : SortedIteratingSystem(FAMILY, FAMILY_COMPARATOR) {

    override fun update(delta: Float) {
        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined) {
            super.update(delta)
        }
    }

    override fun processEntity(entity: Entity, delta: Float) {
        val transformC = entity[TransformComponent::class] ?: return LOG.missingComponent<TransformComponent>()
        val graphicC = entity[GraphicComponent::class] ?: return LOG.missingComponent<GraphicComponent>()

        graphicC.sprite.texture ?: return LOG.error { "Entity has no texture to render." }

        graphicC.sprite.run {
            setBounds(transformC.bottomLeftX, transformC.bottomLeftY, transformC.width, transformC.height)
            draw(batch)
        }
    }
}