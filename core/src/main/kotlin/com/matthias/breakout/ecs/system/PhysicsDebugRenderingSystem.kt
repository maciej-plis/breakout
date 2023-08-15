package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World

/**
 * System responsible for debug rendering of box2D
 */
class PhysicsDebugRenderingSystem(private val world: World, private val camera: Camera) : EntitySystem() {

    private val box2DDebugRenderer = Box2DDebugRenderer()

    override fun update(delta: Float) {
        box2DDebugRenderer.render(world, camera.combined)
    }
}