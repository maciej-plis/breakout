package com.matthias.breakout.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.forever
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.matthias.breakout.BreakoutGame
import com.matthias.breakout.assets.AtlasAsset.LOADING_ATLAS
import com.matthias.breakout.assets.AtlasAsset.MENU_ATLAS
import com.matthias.breakout.assets.SkinAsset.LOADING_SKIN
import com.matthias.breakout.assets.SkinAsset.MENU_SKIN
import com.matthias.breakout.assets.loadAsync
import com.matthias.breakout.common.setScreen
import com.matthias.breakout.common.textSequence
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.log.logger

private val LOG = logger<LoadingScreen>()

class LoadingScreen(game: BreakoutGame) : ScreenBase(game) {

    private val stage = Stage(game.uiViewport, game.batch)
    private var progressBar: ProgressBar? = null

    override fun show() {
        LOG.info { "Showing ${javaClass.simpleName}" }
        Gdx.input.inputProcessor = stage

        KtxAsync.initiate()
        KtxAsync.launch {
            assets.loadAsync(LOADING_SCREEN_ASSETS).joinAll()
            setupLoadingScreenStage()
            assets.loadAsync(REQUIRED_GAME_ASSETS).joinAll()
            LOG.info { "Assets loaded successfully: ${assets.progress.loaded}" }
            LOG.info { "Assets loaded unsuccessfully: ${assets.progress.failed}" }
            proceedToMenuScreen()
        }
    }

    override fun render(delta: Float) {
        stage.run {
            viewport.apply()
            act(delta)
            draw()
        }

        updateProgressBar()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        LOG.info { "Disposing ${javaClass.simpleName}" }
        stage.dispose()
        KtxAsync.launch { LOADING_SCREEN_ASSETS.forEach { assets.unload(it) } }
    }

    private fun setupLoadingScreenStage() {
        game.stageBuilder.build(stage, assets[LOADING_SKIN.descriptor], Gdx.files.internal("scene/loading-scene.json"))
        progressBar = stage.root.findActor("progress-bar")
        setupProgressBarLabelAnimation()
    }

    private fun setupProgressBarLabelAnimation() {
        stage.root.findActor<Label>("progress-bar-label")?.let { label ->
            label.addAction(forever(textSequence(.8f, "Loading.", "Loading..", "Loading...")))
            (label.parent as? Table)?.getCell(label)?.minWidth(label.width) // Keep fixed width of label
        }
    }

    private fun proceedToMenuScreen() {
        game.setScreen(MenuScreen(game))
        game.removeScreen<LoadingScreen>()?.dispose()
    }

    private fun updateProgressBar() {
        progressBar?.value = assets.progress.percent
    }

    companion object {
        private val LOADING_SCREEN_ASSETS = listOf(LOADING_ATLAS.descriptor, LOADING_SKIN.descriptor)
        private val REQUIRED_GAME_ASSETS = listOf(MENU_ATLAS.descriptor, MENU_SKIN.descriptor)
    }
}