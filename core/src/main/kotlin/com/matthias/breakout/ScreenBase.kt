package com.matthias.breakout

import com.badlogic.gdx.graphics.Camera
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage

open class ScreenBase(val game: BreakoutGame, val assetStorage: AssetStorage = game.assets, val camera: Camera = game.camera) : KtxScreen