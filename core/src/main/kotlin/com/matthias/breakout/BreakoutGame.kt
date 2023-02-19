package com.matthias.breakout

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.graphics.color
import ktx.log.logger

private val logger = logger<BreakoutGame>()

class BreakoutGame : KtxGame<KtxScreen>(clearScreen = false) {

    val uiViewport = ScreenViewport()
    val batch by lazy { SpriteBatch() }

    override fun create() {
        logger.info { "Initializing game" }

        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        batch.dispose()
    }
}