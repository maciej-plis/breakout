package com.matthias.breakout.screen

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.matthias.breakout.BG_COLOR
import com.matthias.breakout.BreakoutGame
import de.eskalon.commons.screen.ManagedScreen
import ktx.assets.async.AssetStorage

abstract class ScreenBase(
    val game: BreakoutGame,
    val batch: Batch = game.batch,
    val assets: AssetStorage = game.assets,
    val camera: Camera = game.camera
) : ManagedScreen() {

    override fun create() = Unit

    override fun hide() = Unit

    override fun render(delta: Float) = Unit

    override fun resize(width: Int, height: Int) = Unit

    override fun dispose() = Unit

    override fun getClearColor(): Color = BG_COLOR
}