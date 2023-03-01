package com.matthias.breakout.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.math.Interpolation.bounceOut
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type.touchDown
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type.touchUp
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable.enabled
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.matthias.breakout.BreakoutGame
import com.matthias.breakout.assets.SkinAsset.MENU_SKIN
import com.matthias.breakout.common.setScreen
import ktx.actors.onKeyDown
import ktx.actors.onKeyUp
import ktx.log.logger
import kotlin.math.max
import kotlin.math.min

private val LOG = logger<MenuScreen>()

class MenuScreen(game: BreakoutGame) : ScreenBase(game) {

    private val stage = Stage(game.uiViewport, batch)

    override fun show() {
        LOG.info { "Showing ${javaClass.simpleName}" }
        Gdx.input.inputProcessor = stage
        setupMenuStage()
    }

    override fun render(delta: Float) {
        stage.run {
            viewport.apply()
            act(delta)
            draw()
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        LOG.info { "Disposing ${javaClass.simpleName}" }
        stage.dispose()
    }

    private fun setupMenuStage() {
        game.stageBuilder.build(stage, assets[MENU_SKIN.descriptor], Gdx.files.internal("scene/menu-scene.json"))

        val playBtn = stage.root.findActor<TextButton>("play-button")?.also { stage.keyboardFocus = it }
        val optionsBtn = stage.root.findActor<TextButton>("options-button")
        val helpBtn = stage.root.findActor<TextButton>("help-button")
        val quitBtn = stage.root.findActor<TextButton>("quit-button")

        val buttons = listOf(playBtn, optionsBtn, helpBtn, quitBtn)

        stage.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
                when (keycode) {
                    UP -> moveFocusUp(buttons)
                    DOWN -> moveFocusDown(buttons)
                }
                return super.keyDown(event, keycode)
            }
        })

        playBtn?.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                proceedToGameScreen()
                return super.clicked(event, x, y)
            }
        })

        quitBtn?.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Gdx.app.exit()
                return super.clicked(event, x, y)
            }
        })

        buttons.forEach { button ->
            button?.addListener(object : ClickListener() {
                override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
                    if (isOver) stage.keyboardFocus = button
                    return super.mouseMoved(event, x, y)
                }
            })
            button?.onKeyDown { keyCode ->
                if (keyCode == ENTER) {
                    stage.keyboardFocus?.fire(InputEvent().apply { type = touchDown })
                }
            }
            button?.onKeyUp { keyCode ->
                if (keyCode == ENTER) {
                    stage.keyboardFocus?.fire(InputEvent().apply { type = touchUp })
                }
            }
        }

        stage.draw() // Single stage draw to compute actors position
        setupInitialTransition()
    }

    private fun moveFocusUp(buttons: List<Button?>) {
        val focused = stage.keyboardFocus
        val index = buttons.indexOf(focused)
        stage.keyboardFocus = buttons[max(index - 1, 0)]
    }

    private fun moveFocusDown(buttons: List<Button?>) {
        val focused = stage.keyboardFocus
        val index = buttons.indexOf(focused)
        stage.keyboardFocus = buttons[min(index + 1, buttons.lastIndex)]
    }

    private fun setupInitialTransition() {
        val progressBarContainer = stage.root.findActor<Table>("progress-bar-container-before").apply { localToStageCoordinates(Vector2(0f, 0f)) }
        val targetPos = stage.root.findActor<Table>("progress-bar-container-after")?.localToStageCoordinates(Vector2(0f, 0f))

        if (progressBarContainer != null && targetPos != null) {
            progressBarContainer.addAction(sequence(
                moveTo(targetPos.x, targetPos.y, .8f, bounceOut),
                Actions.run { setupMenuFadeInTransition(progressBarContainer) }
            ))
        }
    }

    private fun setupMenuFadeInTransition(progressBarContainer: Actor) {
        stage.root.findActor<Table>("menu")?.let { menu ->
            menu.addAction(sequence(
                fadeIn(.5f),
                Actions.run { menu.touchable = enabled },
                Actions.run { progressBarContainer.isVisible = false }
            ))
        }
    }

    private fun proceedToGameScreen() {
        stage.root.isVisible = false
        game.setScreen(GameScreen(game))
    }
}