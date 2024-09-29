package com.matthias.breakout.screen

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import com.matthias.breakout.BreakoutGame
import com.matthias.breakout.audio.AudioService
import de.eskalon.commons.screen.ManagedScreen
import ktx.assets.async.AssetStorage
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.log.logger

private val LOG = logger<ScreenBase>()

private val BG_COLOR = Color(.40784314f, .40784314f, .5294118f, 1f)

abstract class ScreenBase(
    val game: BreakoutGame,
    val batch: Batch = game.batch,
    val assets: AssetStorage = game.assets,
    val camera: Camera = game.camera,
    val audio: AudioService = game.audio
) : ManagedScreen() {

    internal open val clearColor: Color = BG_COLOR
    internal open val inputProcessors: GdxArray<InputProcessor> = gdxArrayOf()
    internal open val viewport: Viewport? = null

    override fun create() {
        LOG.debug { "Creating ${javaClass.simpleName}" }
    }

    override fun show() {
        LOG.debug { "Showing ${javaClass.simpleName}" }
        super.show()
    }

    override fun hide() {
        LOG.debug { "Hiding ${javaClass.simpleName}" }
    }

    open fun update(delta: Float) = Unit

    override fun render(delta: Float) {
        update(delta)
    }

    override fun resize(width: Int, height: Int) {
        LOG.debug { "Resizing ${javaClass.simpleName}" }
        viewport?.update(width, height, true)
    }

    override fun dispose() {
        LOG.debug { "Disposing ${javaClass.simpleName}" }
        javaClass.declaredFields.forEach {
            it.isAccessible = true
            var fieldValue = it.get(this)
            fieldValue = (fieldValue as? Lazy<*>)?.value

            if (fieldValue is Disposable) {
                LOG.debug { "Disposing ${fieldValue.javaClass.simpleName} from ${javaClass.simpleName}" }
                fieldValue.dispose()
            }
        }
    }

    override fun getClearColor() = clearColor

    override fun getInputProcessors() = inputProcessors
}
