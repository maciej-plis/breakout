package com.matthias.breakout

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    Lwjgl3Application(BreakoutGame(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Breakout")
        setForegroundFPS(60)
        setWindowedMode(800, 800)
        setWindowIcon("icons/libgdx128.png", "icons/libgdx64.png", "icons/libgdx32.png", "icons/libgdx16.png")
        setResizable(false)
    })
}
