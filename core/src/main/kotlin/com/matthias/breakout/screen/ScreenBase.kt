package com.matthias.breakout.screen

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.matthias.breakout.BreakoutGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage

open class ScreenBase(
    val game: BreakoutGame,
    val batch: Batch = game.batch,
    val assets: AssetStorage = game.assets,
    val camera: Camera = game.camera
) : KtxScreen