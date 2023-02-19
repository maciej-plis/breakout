package com.matthias.breakout

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.matthias.breakout.common.setScreen
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.logger

private val LOG = logger<BreakoutGame>()

class BreakoutGame : KtxGame<KtxScreen>(clearScreen = false) {

    val assets: AssetStorage by lazy { AssetStorage() }

    val stageBuilder = SceneComposerStageBuilder()
    val uiViewport = ScreenViewport()
    val batch by lazy { SpriteBatch() }

    init {
        KtxAsync.initiate()
    }

    override fun create() {
        LOG.info { "Creating ${javaClass.simpleName}" }
        setScreen(LoadingScreen(this))
    }

    override fun dispose() {
        LOG.info { "Disposing ${javaClass.simpleName}" }
        assets.dispose()
        batch.dispose()
    }
}