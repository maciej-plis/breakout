package com.matthias.breakout

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.forever
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.matthias.breakout.common.textSequence
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder
import ktx.app.KtxScreen
import ktx.app.clearScreen

class LoadingScreen(val game: BreakoutGame) : KtxScreen {

    private val stage = Stage(game.uiViewport, game.batch).also { Gdx.input.inputProcessor = it; it.isDebugAll = false }
    private val atlas = TextureAtlas(Gdx.files.internal("atlas/loading.atlas"))
    private val skin = Skin(Gdx.files.internal("skin/loading-skin.json"), atlas)

    override fun show() {
        SceneComposerStageBuilder().build(stage, skin, Gdx.files.internal("scene/loading-scene.json"))
        setupProgressBarLabelAnimation()

        stage.root.findActor<ProgressBar>("progress-bar")?.let { progressBar ->
            println(progressBar.value)
            println(progressBar.minValue)
            println(progressBar.maxValue)
            progressBar.addAction(forever(sequence(
                Actions.run { progressBar.value += 0.01f },
                Actions.delay(.01f)
            )))
        }
    }

    override fun render(delta: Float) {
        clearScreen(.40784314f, .40784314f, .5294118f)
        stage.run {
            viewport.apply()
            act(delta)
            draw()
        }

        stage.root.findActor<ProgressBar>("progress-bar")?.let {
            if(it.value >= 1f) {
                game.addScreen(MenuScreen(game))
                game.setScreen<MenuScreen>()
                game.removeScreen<LoadingScreen>()
                dispose()
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.dispose()
        atlas.dispose()
        skin.dispose()
    }

    private fun setupProgressBarLabelAnimation() {
        stage.root.findActor<Label>("progress-bar-label")?.let { label ->
            label.addAction(forever(textSequence(.8f, "Loading.", "Loading..", "Loading...")))
            (label.parent as? Table)?.getCell(label)?.minWidth(label.width) // Keep fixed width of label
        }
    }
}