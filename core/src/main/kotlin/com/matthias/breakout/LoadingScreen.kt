package com.matthias.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder
import ktx.app.KtxScreen

class LoadingScreen(game: BreakoutGame) : KtxScreen {

    private val stage = Stage(game.uiViewport)
    private val atlas = TextureAtlas(Gdx.files.internal("atlas/loading.atlas"))
    private val skin = Skin(Gdx.files.internal("skin/loading-skin.json"), atlas)

    override fun show() {
        Gdx.input.inputProcessor = stage
        SceneComposerStageBuilder().build(stage, skin, Gdx.files.internal("scene/loading-scene.json"))
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        stage.act()
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
        skin.dispose()
    }
}