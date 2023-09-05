package com.matthias.breakout.screen

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.viewport.Viewport
import com.matthias.breakout.BreakoutGame
import ktx.collections.gdxArrayOf

abstract class StageScreenBase(game: BreakoutGame) : ScreenBase(game) {

    open val stage = Stage(game.uiViewport, batch)

    override val inputProcessors by lazy { gdxArrayOf<InputProcessor>(stage) }
    override val viewport: Viewport = game.uiViewport

    override fun render(delta: Float) {
        super.render(delta)
        stage.run {
            viewport.apply(true)
            act(delta)
            draw()
        }
    }

    override fun dispose() {
        super.dispose()
        stage.dispose()
    }

    fun <T : Actor> findActor(name: String): T = stage.root.findActor(name) ?: throw GdxRuntimeException("Actor with name '$name' was not found")
}