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
import org.gradle.api.DomainObjectCollection
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.UnknownDomainObjectException
import org.gradle.internal.Actions

/**
 * Shorthand for `getByName(myDomainObjectName.value)`.
 *
 * @param name The name of the object to find.
 * @return The object.
 * @see [NamedDomainObjectCollection.getByName]
 * @throws UnknownDomainObjectException if the object is not found.
 */
inline fun <reified S : Any> NamedDomainObjectCollection<S>.getByName(
  name: DomainObjectName<S>
): S = getByName(name.value)

/**
 * Shorthand for `getByName(myDomainObjectName.value) { ... }`.
 *
 * @param name The name of the object to find.
 * @param configurationAction The configuration action.
 * @return The object.
 * @see [NamedDomainObjectCollection.getByName]
 * @throws UnknownDomainObjectException if the object is not found.
 */
inline fun <reified S : Any> NamedDomainObjectCollection<S>.getByName(
  name: DomainObjectName<S>,
  configurationAction: Action<in S>
): S = getByName(name.value, configurationAction)

/**
 * Shorthand for `findByName(myDomainObjectName.value)`.
 *
 * @param name The name of the object to find.
 * @return The object, or `null` if it is not found.
 * @see [NamedDomainObjectCollection.findByName]
 */
inline fun <reified S : Any> NamedDomainObjectCollection<S>.findByName(
  name: DomainObjectName<S>
): S? = findByName(name.value)

/**
 * Shorthand for `named(myDomainObjectName.value) { ... }`.
 *
 * @param name The name of the object to retrieve and configure.
 * @param configurationAction The configuration action.
 * @see [NamedDomainObjectCollection.named]
 * @throws UnknownDomainObjectException if the object is not found.
 */
inline fun <reified S : Any> NamedDomainObjectCollection<S>.named(
  name: DomainObjectName<S>,
  configurationAction: Action<in S> = Actions.doNothing()
): NamedDomainObjectProvider<S> = named(name.value, configurationAction)

/**
 * Shorthand for `named(myDomainObjectName.value, S::class.java) { ... }`.
 *
 * @param name The name of the object to retrieve and configure.
 * @param type The type of the object to retrieve and configure.
 * @param configurationAction The configuration action.
 * @see [NamedDomainObjectCollection.named]
 * @throws UnknownDomainObjectException if the object is not found.
 */
inline fun <reified S : Any> NamedDomainObjectCollection<S>.named(
  name: DomainObjectName<S>,
  type: Class<S>,
  configurationAction: Action<in S> = Actions.doNothing()
): NamedDomainObjectProvider<S> = named(name.value, type, configurationAction)

/**
 * Returns a collection containing the objects in this collection of the
 * given type. Equivalent to calling `withType(type).all(configureAction)`.
 *
 * @param configuration The action to execute for each object in the resulting collection.
 * @return The matching objects. Returns an empty collection
 *   if there are no such objects in this collection.
 * @see [DomainObjectCollection.withType]
 * @since 0.1.0
 */
inline fun <reified S : Any> DomainObjectCollection<in S>.withType(
  noinline configuration: (S) -> Unit
): DomainObjectCollection<S>? = withType(S::class.java, configuration)

/**
 * Returns a collection containing the objects in this collection of the given
 * type. The returned collection is live, so that when matching objects are later
 * added to this collection, they are also visible in the filtered collection.
 *
 * @return The matching objects. Returns an empty collection
 *   if there are no such objects in this collection.
 * @see [DomainObjectCollection.withType]
 * @since 0.1.0
 */
inline fun <reified S : Any> DomainObjectCollection<in S>.withType(): DomainObjectCollection<S> =
  withType(S::class.java)

/**
 * Registers or retrieves a [T] by name and type, then configures it.
 *
 * @param name The name of the object.
 * @param configurationAction The configuration action.
 * @return A provider for the new object [T].
 * @since 0.1.0
 */
fun <T> NamedDomainObjectContainer<T>.registerOnce(
  name: String,
  configurationAction: Action<in T> = Actions.doNothing()
): NamedDomainObjectProvider<T> = if (names.contains(name)) {
  named(name, configurationAction)
} else {
  register(name, configurationAction)
}

/**
 * Registers or retrieves a [S] by name and type, then configures it.
 *
 * @param name The name of the new object.
 * @param configurationAction The configuration action.
 * @return A provider for the new object [S].
 */
inline fun <reified S : Any> NamedDomainObjectContainer<S>.registerOnce(
  name: DomainObjectName<S>,
  configurationAction: Action<in S> = Actions.doNothing()
): NamedDomainObjectProvider<S> = if (names.contains(name.value)) {
  named(name, configurationAction)
} else {
  register(name, configurationAction)
}

/**
 * Registers a new object [S] and configures it.
 *
 * @param name The name of the object.
 * @param configurationAction The configuration action.
 * @return A provider for the new object [S].
 */
inline fun <reified S : Any> NamedDomainObjectContainer<S>.register(
  name: DomainObjectName<S>,
  configurationAction: Action<in S> = Actions.doNothing()
): NamedDomainObjectProvider<S> = register(name.value, configurationAction)
