/*
 * Copyright (C) 2023 Rick Busarow
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rickbusarow.kgx

import org.gradle.api.Action
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * shorthand for `fileTree().from(baseDir)`
 *
 * @since 0.1.0
 */
fun ObjectFactory.fileTree(baseDir: Any): ConfigurableFileTree = fileTree().from(baseDir)

/**
 * shorthand for `fileTree().from(baseDir).also(configureAction::execute)`
 *
 * @since 0.1.0
 */
fun ObjectFactory.fileTree(
  baseDir: Any,
  configureAction: Action<in ConfigurableFileTree>
): ConfigurableFileTree {
  return fileTree().from(baseDir).also(configureAction::execute)
}

/**
 * A reified version of [ObjectFactory.newInstance].
 *
 * @param parameters any constructor parameters that cannot be injected by Gradle
 * @since 0.1.8
 * @throws org.gradle.api.reflect.ObjectInstantiationException if the object cannot be instantiated.
 */
inline fun <reified T : Any> ObjectFactory.newInstance(vararg parameters: Any?): T {
  return newInstance(T::class.java, *parameters)
}

/**
 * alias for `ReadOnlyProperty<Any?, T>`
 *
 * @see [ReadOnlyProperty]
 * @since 0.1.8
 */
typealias LazyGradleProperty<T> = ReadOnlyProperty<Any?, T>

/**
 * Lazy initializer delegate for a [ListProperty<T>][ListProperty].
 *
 * ```
 * open class MyGradleType(objects: ObjectFactory) {
 *
 *   val myProperty: ListProperty<String> by objects.listPropertyDelegate()
 * }
 * ```
 *
 * @since 0.1.8
 */
@JvmOverloads
inline fun <reified T> ObjectFactory.listPropertyDelegate(
  crossinline init: ListProperty<T>.() -> ListProperty<T> = { this }
): ReadOnlyProperty<Any?, ListProperty<T>> = propertyDelegateInternal { listProperty(init) }

/**
 * Just a reified version of [ObjectFactory.listProperty].
 *
 * @since 0.1.8
 */
@JvmOverloads
inline fun <reified T> ObjectFactory.listProperty(
  crossinline init: ListProperty<T>.() -> ListProperty<T> = { this }
): ListProperty<T> = listProperty(T::class.java).init()

/**
 * Lazy initializer delegate for a [SetProperty<T>][SetProperty].
 *
 * ```
 * open class MyGradleType(objects: ObjectFactory) {
 *
 *   val myProperty: SetProperty<String> by objects.setPropertyDelegate()
 * }
 * ```
 *
 * @since 0.1.8
 */
@JvmOverloads
inline fun <reified T> ObjectFactory.setPropertyDelegate(
  crossinline init: SetProperty<T>.() -> SetProperty<T> = { this }
): LazyGradleProperty<SetProperty<T>> = propertyDelegateInternal { setProperty(init) }

/**
 * Just a reified version of [ObjectFactory.setProperty].
 *
 * @since 0.1.8
 */
@JvmOverloads
inline fun <reified T> ObjectFactory.setProperty(
  crossinline init: SetProperty<T>.() -> SetProperty<T> = { this }
): SetProperty<T> = setProperty(T::class.java).init()

/**
 * Lazy initializer delegate for a [MapProperty<K, V>][MapProperty].
 *
 * ```
 * open class MyGradleType(objects: ObjectFactory) {
 *   val myProperty: MapProperty<String, String> by objects.mapPropertyDelegate()
 * }
 * ```
 *
 * @since 0.1.8
 */
@JvmOverloads
inline fun <reified K, reified V> ObjectFactory.mapPropertyDelegate(
  crossinline init: MapProperty<K, V>.() -> MapProperty<K, V> = { this }
): LazyGradleProperty<MapProperty<K, V>> = propertyDelegateInternal { mapProperty(init) }

/**
 * Just a reified version of [ObjectFactory.mapProperty].
 *
 * @since 0.1.8
 */
@JvmOverloads
inline fun <reified K, reified V> ObjectFactory.mapProperty(
  crossinline init: MapProperty<K, V>.() -> MapProperty<K, V> = { this }
): MapProperty<K, V> = mapProperty(K::class.java, V::class.java).init()

/**
 * Just a reified version of [ObjectFactory.property].
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.property(): Property<T> = property(T::class.java)

/**
 * Lazy initializer delegate for a [Property<T>][Property].
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.propertyDelegate(): LazyGradleProperty<Property<T>> {
  return propertyDelegateInternal { property(T::class.java) }
}

/**
 * Just a reified version of [ObjectFactory.property].
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.propertyOf(value: T): Property<T> {
  return property(T::class.java).convention(value)
}

/**
 * Lazy initializer delegate for a [Property<T>][Property].
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.propertyDelegate(
  crossinline convention: () -> T
): LazyGradleProperty<Property<T>> {
  return propertyDelegateInternal { property(convention) }
}

/**
 * Just a reified version of [ObjectFactory.property].
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.property(crossinline convention: () -> T): Property<T> =
  property(T::class.java).convention(convention())

/**
 * Lazy initializer delegate for a [Property<T>][Property].
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.propertyDelegate(
  convention: Provider<out T>
): LazyGradleProperty<Property<T>> {
  return propertyDelegateInternal { property(T::class.java).convention(convention) }
}

/**
 * Just a reified version of [ObjectFactory.property].
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.property(convention: Provider<out T>): Property<T> =
  property(T::class.java).convention(convention)

@PublishedApi
internal fun <T> propertyDelegateInternal(
  defaultValueProvider: () -> T
): ReadOnlyProperty<Any?, T> = PropertyDelegateInternal(defaultValueProvider)

@PublishedApi
internal class PropertyDelegateInternal<T>(
  private val defaultValueProvider: () -> T
) : ReadOnlyProperty<Any?, T> {

  private var backing: T? = null

  override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
    return backing ?: defaultValueProvider().also { backing = it }
  }
}
