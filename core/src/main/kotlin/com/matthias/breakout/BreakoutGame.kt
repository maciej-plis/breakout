package com.matthias.breakout

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.graphics.color
import ktx.log.logger

private val logger = logger<BreakoutGame>()

class BreakoutGame : KtxGame<KtxScreen>(clearScreen = false) {

    val assets: AssetStorage by lazy { AssetStorage() }

    val stageBuilder = SceneComposerStageBuilder()
    val uiViewport = ScreenViewport()
    val batch by lazy { SpriteBatch() }

    init {
        KtxAsync.initiate()
    }

    override fun create() {
        logger.info { "Initializing game" }

        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        batch.dispose()
    }
}