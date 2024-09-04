package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.NUMPAD_1
import com.matthias.breakout.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.has
import ktx.ashley.plusAssign
import ktx.ashley.remove
import ktx.log.logger

private val LOG = logger<DebugSystem>()
private val PADDLE_FAMILY = allOf(PaddleComponent::class, BodyComponent::class).get()

/**
 * System responsible for adding debug functionalities like spawning balls etc.
 */

/**
 * System responsible for adding debug functionalities like spawning balls etc.
 */
class DebugSystem(val createBallCallback: () -> Unit) : EntitySystem() {

    override fun update(delta: Float) {
        if (Gdx.input.isKeyJustPressed(NUMPAD_1)) {
            LOG.info { "[COMMAND] Respawn ball triggered" }
            createBallCallback()
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            LOG.info { "[COMMAND] Show paddle triggered" }
            engine.getEntitiesFor(PADDLE_FAMILY).forEach {
                it.remove<HiddenComponent>()
                it.remove<HideComponent>()
                it += ShowComponent()
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            LOG.info { "[COMMAND] Hide paddle triggered" }
            engine.getEntitiesFor(PADDLE_FAMILY).forEach {
                it.remove<ShownComponent>()
                it.remove<ShowComponent>()
                it += HideComponent()
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
            LOG.info { "[COMMAND] Sticky paddle toggled" }
            engine.getEntitiesFor(PADDLE_FAMILY).forEach {
                if (it.has(getMapperFor<StickyComponent>())) {
                    it.remove<StickyComponent>()
                } else {
                    it += StickyComponent()
                }
            }
        }
    }
}
