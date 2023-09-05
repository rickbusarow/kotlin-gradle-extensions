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
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

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
 * @param name The name of the task.
 * @param configurationAction The configuration action.
 * @return A provider for the new object [T].
 * @since 0.1.0
 */
fun <T> NamedDomainObjectContainer<T>.registerOnce(
  name: String,
  configurationAction: Action<in T>
): NamedDomainObjectProvider<T> = if (names.contains(name)) {
  named(name, configurationAction)
} else {
  register(name, configurationAction)
}
