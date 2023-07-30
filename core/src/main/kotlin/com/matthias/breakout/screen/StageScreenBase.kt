package com.matthias.breakout.screen

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.matthias.breakout.BreakoutGame
import ktx.collections.gdxArrayOf

open class StageScreenBase(game: BreakoutGame) : ScreenBase(game) {

    val stage = Stage(game.uiViewport, batch)

    override val inputProcessors = gdxArrayOf<InputProcessor>(stage)
    override val viewport = stage.viewport

    override fun render(delta: Float) {
        super.render(delta)
        stage.run {
            viewport.apply()
            act(delta)
            draw()
        }
    }

    override fun dispose() {
        super.dispose()
        stage.dispose()
    }
}