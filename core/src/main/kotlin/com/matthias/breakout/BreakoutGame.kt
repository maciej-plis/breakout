package com.matthias.breakout

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.matthias.breakout.common.setScreen
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.logger

private val LOG = logger<BreakoutGame>()

private const val V_WIDTH = 512f
private const val V_HEIGHT = 288f
const val PPM = 16f

class BreakoutGame : KtxGame<KtxScreen>(clearScreen = false) {

    val assets: AssetStorage by lazy { AssetStorage() }

    val stageBuilder = SceneComposerStageBuilder()

    val uiViewport = ScreenViewport()
    val gameViewport = ExtendViewport(V_WIDTH / PPM, V_HEIGHT / PPM)

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
        super.dispose()
        assets.dispose()
        batch.dispose()
    }
}