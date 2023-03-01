package com.matthias.breakout

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.matthias.breakout.common.clearScreen
import com.matthias.breakout.common.setScreen
import com.matthias.breakout.common.toMeters
import com.matthias.breakout.screen.LoadingScreen
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.log.logger

private val LOG = logger<BreakoutGame>()

private const val V_WIDTH = 18f
private const val V_HEIGHT = 32f
const val PPM = 16f

val BG_COLOR = Color(.40784314f, .40784314f, .5294118f, 1f)

class BreakoutGame : KtxGame<KtxScreen>(clearScreen = false) {

    val camera = OrthographicCamera()
    val uiViewport = ScreenViewport(camera)
    val gameViewport = ExtendViewport(V_WIDTH.toMeters(), V_HEIGHT.toMeters(), camera)

    val stageBuilder = SceneComposerStageBuilder()

    val assets: AssetStorage by lazy { AssetStorage() }

    val batch by lazy { SpriteBatch() }

    override fun create() {
        LOG.info { "Creating ${javaClass.simpleName}" }
        setScreen(LoadingScreen(this))
    }

    override fun render() {
        clearScreen(BG_COLOR)
        super.render()
    }

    override fun dispose() {
        LOG.info { "Disposing ${javaClass.simpleName}" }
        super.dispose()
        assets.dispose()
        batch.dispose()
    }
}