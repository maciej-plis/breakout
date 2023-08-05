package com.matthias.breakout

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Interpolation.pow2In
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.matthias.breakout.common.addScreen
import com.matthias.breakout.common.addScreenTransition
import com.matthias.breakout.common.pushScreen
import com.matthias.breakout.common.toMeters
import com.matthias.breakout.screen.GameScreen
import com.matthias.breakout.screen.LoadingScreen
import com.matthias.breakout.screen.MenuScreen
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder
import de.eskalon.commons.core.ManagedGame
import de.eskalon.commons.screen.ManagedScreen
import de.eskalon.commons.screen.transition.ScreenTransition
import de.eskalon.commons.screen.transition.impl.BlendingTransition
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.logger

private val LOG = logger<BreakoutGame>()

private const val V_WIDTH = 48f
private const val V_HEIGHT = 36f
const val PPM = 16f

class BreakoutGame : ManagedGame<ManagedScreen, ScreenTransition>() {

    val camera = OrthographicCamera()
    val uiViewport = ScreenViewport(camera)
    val gameViewport = ExtendViewport(V_WIDTH.toMeters(), V_HEIGHT.toMeters(), camera)

    val stageBuilder = SceneComposerStageBuilder()

    val assets: AssetStorage by lazy {
        AssetStorage().apply {
            setLoader { TmxMapLoader() }
        }
    }

    val batch by lazy { SpriteBatch() }

    override fun create() {
        LOG.info { "Creating ${javaClass.simpleName}" }
        super.create();

        Gdx.app.logLevel = Application.LOG_DEBUG

        KtxAsync.initiate()

        addScreen(LoadingScreen(this))
        addScreen(MenuScreen(this))
        addScreen(GameScreen(this))

        addScreenTransition(BlendingTransition(batch, 0.15f, pow2In))

        pushScreen<LoadingScreen>()
    }

    override fun dispose() {
        LOG.info { "Disposing ${javaClass.simpleName}" }
        super.dispose()

        assets.dispose()
        batch.dispose()
    }
}