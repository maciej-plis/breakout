package com.matthias.breakout.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation.bounceOut
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable.enabled
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.matthias.breakout.BreakoutGame
import com.matthias.breakout.assets.SkinAsset.MENU_SKIN
import com.matthias.breakout.common.onMove
import com.matthias.breakout.common.pushScreen
import de.golfgl.gdx.controllers.ControllerMenuStage
import ktx.actors.onClick
import ktx.collections.gdxArrayOf
import ktx.log.logger

private val LOG = logger<MenuScreen>()

class MenuScreen(game: BreakoutGame) : StageScreenBase(game) {

    private val playBtn by lazy { findActor<TextButton>("play-button") }
    private val optionsBtn by lazy { findActor<TextButton>("options-button") }
    private val helpBtn by lazy { findActor<TextButton>("help-button") }
    private val quitBtn by lazy { findActor<TextButton>("quit-button") }

    override val stage = ControllerMenuStage(viewport, batch)

    override fun create() {
        super.create()
        setupMenuStage()
    }

    private fun setupMenuStage() {
        game.stageBuilder.build(stage, assets[MENU_SKIN.descriptor], Gdx.files.internal("scene/menu-scene.json"))

        stage.addFocusableActors(gdxArrayOf(playBtn, optionsBtn, helpBtn, quitBtn))
        stage.setFocusedActor(playBtn)

        stage.run {
            focusableActors.forEach { it.onMove { setFocusedActor(it) } }
        }

        playBtn.onClick { proceedToGameScreen() }
        optionsBtn.onClick { }
        helpBtn.onClick { }
        quitBtn.onClick { Gdx.app.exit() }

        setupInitialTransition()
    }

    private fun setupInitialTransition() {
        stage.draw() // Single stage draw to compute actor positions
        val progressBarContainer = findActor<Table>("progress-bar-container-before").apply { localToStageCoordinates(Vector2(0f, 0f)) }
        val targetPos = findActor<Table>("progress-bar-container-after").localToStageCoordinates(Vector2(0f, 0f))

        progressBarContainer.addAction(sequence(
            moveTo(targetPos.x, targetPos.y, .8f, bounceOut),
            Actions.run { setupMenuFadeInTransition(progressBarContainer) }
        ))
    }

    private fun setupMenuFadeInTransition(progressBarContainer: Actor) {
        findActor<Table>("menu").let { menu ->
            menu.addAction(sequence(
                fadeIn(.5f),
                Actions.run { menu.touchable = enabled },
                Actions.run { progressBarContainer.isVisible = false }
            ))
        }
    }

    private fun proceedToGameScreen() {
        game.pushScreen<GameScreen>()
    }
}