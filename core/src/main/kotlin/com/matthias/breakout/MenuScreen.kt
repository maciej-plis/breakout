package com.matthias.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Interpolation.bounceOut
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.Touchable.enabled
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder
import ktx.actors.alpha
import ktx.actors.centerPosition
import ktx.app.KtxScreen
import ktx.app.clearScreen

class MenuScreen(game: BreakoutGame) : KtxScreen {

    private val stage = Stage(game.uiViewport, game.batch).also { Gdx.input.inputProcessor = it; it.isDebugAll = false }
    private val atlas = TextureAtlas(Gdx.files.internal("atlas/menu.atlas"))
    private val skin = Skin(Gdx.files.internal("skin/menu-skin.json"), atlas)

    private var transition = false

    override fun show() {
        SceneComposerStageBuilder().build(stage, skin, Gdx.files.internal("scene/menu-scene.json"))

        val playBtn = stage.root.findActor<TextButton>("play-button").also { it.addListener(DebugTestListener("Play Button has been clicked!")) }
        val optionsBtn = stage.root.findActor<TextButton>("options-button").also { it.addListener(DebugTestListener("Options Button has been clicked!")) }
        val helpBtn = stage.root.findActor<TextButton>("help-button").also { it.addListener(DebugTestListener("Help Button has been clicked!")) }
        val quitBtn = stage.root.findActor<TextButton>("quit-button").also { it.addListener(DebugTestListener("Quit Button has been clicked!")) }
    }

    override fun render(delta: Float) {
        clearScreen(.40784314f, .40784314f, .5294118f)
        stage.run {
            viewport.apply()
            act()
            draw()
        }

        if (!transition) {
            val before = stage.root.findActor<Table>("progress-bar-container-before")
            val after = stage.root.findActor<Table>("progress-bar-container-after").run {
                localToStageCoordinates(Vector2(0f, 0f))
            }

            before.addAction(Actions.sequence(
                Actions.moveTo(after.x, after.y, .8f, bounceOut),
                Actions.run { stage.root.findActor<Table>("menu").addAction(Actions.sequence(
                    fadeIn(.5f),
                    Actions.run { stage.root.findActor<Table>("menu").touchable = enabled }
                )) }
            ))

            transition = true
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.dispose()
        atlas.dispose()
        skin.dispose()
    }
}

class DebugTestListener(private val text: String) : ClickListener() {
    override fun clicked(event: InputEvent?, x: Float, y: Float) {
        println(text)
    }
}