package com.matthias.breakout.ecs.system


import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.matthias.breakout.common.half
import com.matthias.breakout.ecs.component.GraphicComponent
import com.matthias.breakout.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.logger

private val LOG = logger<RenderSystem>()

private val family = allOf(TransformComponent::class, GraphicComponent::class).get()
private val familyComparator = compareBy<Entity> { entity -> entity[TransformComponent.mapper] }

class RenderSystem(private val batch: Batch, private val gameViewport: Viewport) : SortedIteratingSystem(family, familyComparator) {

    override fun update(delta: Float) {
        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined) {
            super.update(delta)
        }
    }

    override fun processEntity(entity: Entity, delta: Float) {
        val transform = entity[TransformComponent.mapper]!!
        val graphic = entity[GraphicComponent.mapper]!!

        if (graphic.sprite.texture == null) {
            return LOG.error { "Entity has no texture for rendering. |$entity|" }
        }

        graphic.sprite.run {
            setBounds(
                transform.position.x - transform.size.x.half,
                transform.position.y - transform.size.y.half,
                transform.size.x,
                transform.size.y
            )
            draw(batch)
        }
    }
}