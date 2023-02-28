package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.NUMPAD_1

class DebugSystem(val createBallCallback: () -> Unit) : EntitySystem() {

    override fun update(delta: Float) {
        if (Gdx.input.isKeyJustPressed(NUMPAD_1)) {
            createBallCallback()
        }
    }
}