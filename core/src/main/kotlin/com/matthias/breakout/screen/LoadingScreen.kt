package com.matthias.breakout.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions.forever
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.matthias.breakout.BreakoutGame
import com.matthias.breakout.assets.AtlasAsset.LOADING_ATLAS
import com.matthias.breakout.assets.AtlasAsset.MENU_ATLAS
import com.matthias.breakout.assets.SkinAsset.LOADING_SKIN
import com.matthias.breakout.assets.SkinAsset.MENU_SKIN
import com.matthias.breakout.assets.loadAsync
import com.matthias.breakout.assets.loadSync
import com.matthias.breakout.common.textSequence
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.log.logger

private val LOG = logger<LoadingScreen>()

private val LOADING_SCREEN_ASSETS = listOf(LOADING_ATLAS.descriptor, LOADING_SKIN.descriptor)
private val REQUIRED_GAME_ASSETS = listOf(MENU_ATLAS.descriptor, MENU_SKIN.descriptor)

class LoadingScreen(game: BreakoutGame) : StageScreenBase(game) {

    private val progressBar: ProgressBar by lazy { stage.root.findActor("progress-bar") }

    override fun create() {
        super.create()
        assets.loadSync(LOADING_SCREEN_ASSETS)
        setupLoadingScreenStage()
    }

    override fun show() {
        super.show()
        KtxAsync.launch {
            assets.loadAsync(REQUIRED_GAME_ASSETS).joinAll()
            LOG.info { "Assets loaded successfully: ${assets.progress.loaded}" }
            LOG.info { "Assets loaded unsuccessfully: ${assets.progress.failed}" }
            proceedToMenuScreen()
        }
    }

    override fun update(delta: Float) {
        super.update(delta)
        updateProgressBar()
    }

    private fun setupLoadingScreenStage() {
        game.stageBuilder.build(stage, assets[LOADING_SKIN.descriptor], Gdx.files.internal("scene/loading-scene.json"))
        setupProgressBarLabelAnimation()
    }

    private fun setupProgressBarLabelAnimation() {
        stage.root.findActor<Label>("progress-bar-label")?.addAction(
            forever(textSequence(.8f, "Loading.", "Loading..", "Loading..."))
        )
    }

    private fun proceedToMenuScreen() {
        game.screenManager.pushScreen("MenuScreen", null)
    }

    private fun updateProgressBar() {
        progressBar.value = assets.progress.percent
    }
}