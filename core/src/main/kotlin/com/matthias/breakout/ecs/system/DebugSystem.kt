package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.NUMPAD_1
import ktx.log.logger

private val LOG = logger<DebugSystem>()

class DebugSystem(val createBallCallback: () -> Unit) : EntitySystem() {

    override fun update(delta: Float) {
        if (Gdx.input.isKeyJustPressed(NUMPAD_1)) {
            LOG.debug { "[COMMAND] Respawn ball triggered" }
            createBallCallback()
        }
    }
}