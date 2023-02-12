package com.matthias.breakout

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.logger

private val logger = logger<BreakoutGame>()

class BreakoutGame : KtxGame<KtxScreen>() {

    private val batch by lazy { SpriteBatch() }

    override fun create() {
        logger.info { "Initializing game" }
    }

    override fun dispose() {
        batch.dispose()
    }
}