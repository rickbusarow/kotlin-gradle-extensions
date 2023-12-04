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
 * A reified version of [ObjectFactory.newInstance].
 *
 * @param parameters any constructor parameters that cannot be injected by Gradle
 * @param threadSafetyMode the thread-safety threadSafetyMode
 *   for initializing the value of the lazy property.
 * @throws org.gradle.api.reflect.ObjectInstantiationException if the object cannot be instantiated.
 */
inline fun <reified T : Any> ObjectFactory.newInstanceLazy(
  vararg parameters: Any?,
  threadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE
): Lazy<T> = newInstanceLazy(T::class.java, *parameters, threadSafetyMode = threadSafetyMode)

/**
 * A reified version of [ObjectFactory.newInstance].
 *
 * @param clazz the type to instantiate
 * @param parameters any constructor parameters that cannot be injected by Gradle
 * @param threadSafetyMode the thread-safety threadSafetyMode
 *   for initializing the value of the lazy property.
 * @throws org.gradle.api.reflect.ObjectInstantiationException if the object cannot be instantiated.
 */
fun <T> ObjectFactory.newInstanceLazy(
  clazz: Class<out T>,
  vararg parameters: Any?,
  threadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE
): Lazy<T> = gradleLazy(threadSafetyMode) { newInstance(clazz, *parameters) }

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
 *   val myProperty: ListProperty<String> by objects.listPropertyLazy()
 * }
 * ```
 *
 * @since 0.1.8
 */
@JvmOverloads
inline fun <reified T> ObjectFactory.listPropertyLazy(
  threadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
  crossinline init: ListProperty<T>.() -> ListProperty<T> = { this }
): GradleLazy<ListProperty<T>> = gradleLazy(threadSafetyMode) { listProperty(init) }

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
 *   val myProperty: SetProperty<String> by objects.setPropertyLazy()
 * }
 * ```
 *
 * @since 0.1.8
 */
@JvmOverloads
inline fun <reified T> ObjectFactory.setPropertyLazy(
  threadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
  crossinline init: SetProperty<T>.() -> SetProperty<T> = { this }
): Lazy<SetProperty<T>> = gradleLazy(threadSafetyMode) { setProperty(init) }

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
 *   val myProperty: MapProperty<String, String> by objects.mapPropertyLazy()
 * }
 * ```
 *
 * @since 0.1.8
 */
@JvmOverloads
inline fun <reified K, reified V> ObjectFactory.mapPropertyLazy(
  threadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
  crossinline init: MapProperty<K, V>.() -> MapProperty<K, V> = { this }
): Lazy<MapProperty<K, V>> = gradleLazy(threadSafetyMode) { mapProperty(init) }

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
inline fun <reified T : Any> ObjectFactory.propertyLazy(
  threadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE
): Lazy<Property<T>> = gradleLazy(threadSafetyMode) {
  property(T::class.java)
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
inline fun <reified T : Any> ObjectFactory.propertyLazy(
  crossinline convention: () -> T
): Lazy<Property<T>> = gradleLazy { property(convention) }

/**
 * Just a reified version of [ObjectFactory.property].
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.property(crossinline convention: () -> T): Property<T> =
  property(T::class.java).convention(convention())

/**
 * Just a reified version of [ObjectFactory.property].
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.property(convention: T): Property<T> =
  property(T::class.java).convention(convention)

/**
 * Lazy initializer delegate for a [Property<T>][Property].
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.propertyLazy(
  threadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
  convention: Provider<out T>
): Lazy<Property<T>> = gradleLazy(threadSafetyMode) {
  property(T::class.java).convention(convention)
}

/**
 * A reified version of `property(T::class.java).convention(convention)`
 *
 * @since 0.1.8
 */
inline fun <reified T : Any> ObjectFactory.property(convention: Provider<out T>): Property<T> =
  property(T::class.java).convention(convention)
