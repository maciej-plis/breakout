package com.matthias.breakout.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.matthias.breakout.assets.AtlasAsset.LOADING_ATLAS
import com.matthias.breakout.assets.AtlasAsset.MENU_ATLAS
import ktx.assets.async.AssetStorage
import ktx.freetype.freeTypeFontParameters


enum class TiledMapAsset(
    fileName: String,
    directory: String = "tiled-map",
    val descriptor: AssetDescriptor<TiledMap> = AssetDescriptor("$directory/$fileName")
) {
    LEVEL_1("level-1.tmx"),
    LEVEL_2("level-2.tmx"),
    LEVEL_3("level-3.tmx"),
    LEVEL_4("level-4.tmx"),
}

enum class AtlasAsset(
    fileName: String,
    directory: String = "atlas",
    val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName")
) {
    LOADING_ATLAS("loading.atlas"),
    MENU_ATLAS("menu.atlas"),
    BREAKOUT_ATLAS("breakout.atlas"),
}

enum class BitmapFontAsset(
    fileName: String,
    atlas: AtlasAsset,
    directory: String = "font",
    val descriptor: AssetDescriptor<BitmapFont> = AssetDescriptor("$directory/$fileName", BitmapFontParameter(atlas.descriptor.fileName))
) {
    LOADING_FONT("loading.fnt", LOADING_ATLAS),
    ABADDON_BOLD_21_WHITE("abaddon-bold-21-white.fnt", MENU_ATLAS),
    ABADDON_BOLD_36_WHITE("abaddon-bold-36-white.fnt", MENU_ATLAS)
}

enum class FreeTypeFontAsset(
    fileName: String,
    setup: FreeTypeFontParameter.() -> Unit,
    directory: String = "font",
    val descriptor: AssetDescriptor<BitmapFont> = AssetDescriptor("$directory/$fileName", freeTypeFontParameters("$directory/$fileName", setup))
) {
    ABADDON_BOLD_48_WHITE_OUTLINE("abaddon-bold.ttf", {
        size = 48
        color = WHITE
        borderColor = BLACK
        borderWidth = 2f
    })

}

enum class SoundAsset(
    fileName: String,
    directory: String = "sfx",
    val descriptor: AssetDescriptor<Sound> = AssetDescriptor("$directory/$fileName")
) {
    PADDLE_HIT("paddle-hit.wav"),
    WALL_HIT("wall-hit.wav"),
    REINFORCED_BLOCK_HIT("reinforced-block-hit.wav"),
    BLOCK_DESTROYED("block-destroyed.wav"),
    BALL_DROP("ball-drop.wav"),
    DEATH("death.wav"),
    GAME_OVER("game-over.wav"),
    LEVEL_COMPLETED("level-completed.wav")
}

enum class MusicAsset(
    fileName: String,
    directory: String = "music",
    val descriptor: AssetDescriptor<Music> = AssetDescriptor("$directory/$fileName")
) {
}

enum class SkinAsset(
    fileName: String,
    atlas: AtlasAsset,
    directory: String = "skin",
    val descriptor: AssetDescriptor<Skin> = AssetDescriptor("$directory/$fileName", SkinParameter(atlas.descriptor.fileName))
) {
    LOADING_SKIN("loading-skin.json", LOADING_ATLAS),
    MENU_SKIN("menu-skin.json", MENU_ATLAS)
}

enum class ShaderProgramAsset(
    vertexFileName: String,
    fragmentFileName: String,
    directory: String = "shader",
    val descriptor: AssetDescriptor<ShaderProgram> = AssetDescriptor(
        "$directory/$vertexFileName/$fragmentFileName",
        ShaderProgramLoader.ShaderProgramParameter().apply {
            vertexFile = "$directory/$vertexFileName"
            fragmentFile = "$directory/$fragmentFileName"
        }
    )
)

fun AssetStorage.loadAsync(assets: List<AssetDescriptor<*>>) = assets.map { loadAsync(it) }
fun AssetStorage.loadSync(assets: List<AssetDescriptor<*>>) = assets.map { loadSync(it) }

private fun BitmapFontParameter(textureAtlasPath: String) = BitmapFontParameter().apply { atlasName = textureAtlasPath }

private inline fun <reified T> AssetDescriptor(fileName: String, params: AssetLoaderParameters<T>? = null) = AssetDescriptor(fileName, T::class.java, params)
