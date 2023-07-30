package com.matthias.breakout.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.matthias.breakout.assets.AtlasAsset.LOADING_ATLAS
import com.matthias.breakout.assets.AtlasAsset.MENU_ATLAS
import ktx.assets.async.AssetStorage


enum class AtlasAsset(
    fileName: String,
    directory: String = "atlas",
    val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName")
) {
    LOADING_ATLAS("loading.atlas"),
    MENU_ATLAS("menu.atlas")
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

enum class SkinAsset(
    fileName: String,
    atlas: AtlasAsset,
    directory: String = "skin",
    val descriptor: AssetDescriptor<Skin> = AssetDescriptor("$directory/$fileName", SkinParameter(atlas.descriptor.fileName))
) {
    LOADING_SKIN("loading-skin.json", LOADING_ATLAS),
    MENU_SKIN("menu-skin.json", MENU_ATLAS)
}

suspend fun AssetStorage.load(assets: List<AssetDescriptor<*>>) = assets.map { load(it) }
suspend fun AssetStorage.unload(assets: List<AssetDescriptor<*>>) = assets.map { unload(it) }
fun AssetStorage.loadAsync(assets: List<AssetDescriptor<*>>) = assets.map { loadAsync(it) }
fun AssetStorage.loadSync(assets: List<AssetDescriptor<*>>) = assets.map { loadSync(it) }

private fun BitmapFontParameter(textureAtlasPath: String) = BitmapFontParameter().apply { atlasName = textureAtlasPath }

private inline fun <reified T> AssetDescriptor(fileName: String, params: AssetLoaderParameters<T>? = null) = AssetDescriptor(fileName, T::class.java, params)