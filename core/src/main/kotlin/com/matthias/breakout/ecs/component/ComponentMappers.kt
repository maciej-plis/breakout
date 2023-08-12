package com.matthias.breakout.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.ObjectMap
import ktx.ashley.mapperFor
import ktx.collections.set
import kotlin.reflect.KClass

val mappers = ObjectMap<KClass<out Component>, ComponentMapper<out Component>>();

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Component> getMapperFor(): ComponentMapper<T> = (mappers[T::class] ?: registerMapper<T>()) as ComponentMapper<T>

inline fun <reified T : Component> registerMapper(): ComponentMapper<T> = mapperFor<T>().apply { mappers[T::class] = this }

inline operator fun <reified T : Component> Entity.get(clazz: KClass<T>): T? = getMapperFor<T>().get(this)