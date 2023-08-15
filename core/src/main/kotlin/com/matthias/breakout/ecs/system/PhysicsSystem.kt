package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.box2d.World
import ktx.log.logger

private val LOG = logger<PhysicsSystem>()

private const val VELOCITY_ITERATIONS = 8
private const val POSITION_ITERATIONS = 3

/**
 * System responsible for running box2D physics iterations
 */
class PhysicsSystem(private val world: World) : EntitySystem() {

    override fun update(delta: Float) {
        world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS)
        super.update(delta)
    }
}