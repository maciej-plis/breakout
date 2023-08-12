package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.matthias.breakout.common.x
import com.matthias.breakout.common.y
import com.matthias.breakout.ecs.component.AttachComponent
import com.matthias.breakout.ecs.component.BodyComponent
import ktx.ashley.allOf
import ktx.ashley.get

private val family = allOf(AttachComponent::class, BodyComponent::class).get()

class AttachSystem : IteratingSystem(family) {

    override fun processEntity(entity: Entity, delta: Float) {
        val attachC = entity[AttachComponent.mapper]!!
        val bodyC = entity[BodyComponent.mapper]!!

        val body = bodyC.body
        val attachedToBody = attachC.attachedToEntity!![BodyComponent.mapper]!!.body

        val position = Vector2(attachedToBody.x + attachC.offset.x, attachedToBody.y + attachC.offset.y)

        body.setTransform(position, body.angle)

    }
}