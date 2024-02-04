/*
 * Copyright (C) 2024 Rick Busarow
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

import com.rickbusarow.kgx.internal.ElementInfoAction
import com.rickbusarow.kgx.internal.ElementInfoAction.ElementValue
import com.rickbusarow.kgx.internal.ElementInfoAction.RegisteredElement
import com.rickbusarow.kgx.names.DomainObjectName
import com.rickbusarow.kgx.stdlib.castNamed
import org.gradle.api.Action
import org.gradle.api.DomainObjectCollection
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.UnknownDomainObjectException
import org.gradle.internal.Actions
import kotlin.reflect.KProperty

/**
 * Invokes this [ElementInfoAction] with the provided element name and
 * value. The type of the element is inferred from the type of the value.
 *
 * @param elementName The name of the element.
 * @param elementValue The value of the element, encapsulated in an [ElementValue].
 * @receiver ElementInfoAction The action to be executed.
 * @since 0.1.5
 */
public inline operator fun <reified T : Any> ElementInfoAction<T>.invoke(
  elementName: String,
  elementValue: ElementValue<T>
) {
  execute(RegisteredElement(elementName, T::class.java, elementValue))
}

/**
 * Invokes this [ElementInfoAction] with the provided element name, type and value.
 * This is useful when the type of the element is not the same as the type of the value.
 *
 * @param elementName The name of the element.
 * @param elementType The type of the element.
 * @param elementValue The value of the element, encapsulated in an [ElementValue].
 * @receiver ElementInfoAction The action to be executed.
 * @since 0.1.5
 */
public operator fun <T : Any> ElementInfoAction<T>.invoke(
  elementName: String,
  elementType: Class<out T>,
  elementValue: ElementValue<T>
) {
  execute(RegisteredElement(elementName, elementType, elementValue))
}

/**
 * Shorthand for `getByName(myDomainObjectName.value)`.
 *
 * @param name The name of the object to find.
 * @return The object.
 * @see NamedDomainObjectCollection.getByName
 * @since 0.1.6
 * @throws UnknownDomainObjectException if the object is not found.
 */
public inline fun <reified S : Any> NamedDomainObjectCollection<S>.getByName(
  name: DomainObjectName<S>
): S = getByName(name.value)

/**
 * Shorthand for `getByName(myDomainObjectName.value) { ... }`.
 *
 * @param name The name of the object to find.
 * @param configurationAction The configuration action.
 * @return The object.
 * @see NamedDomainObjectCollection.getByName
 * @since 0.1.6
 * @throws UnknownDomainObjectException if the object is not found.
 */
public inline fun <reified S : Any> NamedDomainObjectCollection<S>.getByName(
  name: DomainObjectName<S>,
  configurationAction: Action<in S>
): S = getByName(name.value, configurationAction)

/**
 * An indexing operator alias for [NamedDomainObjectCollection.getByName].
 *
 * Locates an object by name, failing if there is no such object.
 *
 * @param name The object name
 * @return The object with the given name.
 * @see NamedDomainObjectCollection.getByName
 * @since 0.1.11
 * @throws UnknownDomainObjectException when there is no such object in this collection.
 */
public operator fun <S : Any> NamedDomainObjectCollection<S>.get(
  name: DomainObjectName<S>
): S = getByName(name.value)

/**
 * An indexing operator alias for [NamedDomainObjectCollection.getByName].
 *
 * Locates an object by name, failing if there is no such object.
 *
 * @param name The object name
 * @return The object with the given name.
 * @see NamedDomainObjectCollection.getByName
 * @throws UnknownDomainObjectException when there is no such object in this collection.
 */
public operator fun <S : Any> NamedDomainObjectCollection<S>.get(name: String): S =
  getByName(name)

/**
 * Shorthand for `findByName(myDomainObjectName.value)`.
 *
 * @param name The name of the object to find.
 * @return The object, or `null` if it is not found.
 * @see NamedDomainObjectCollection.findByName
 * @since 0.1.6
 */
public inline fun <reified S : Any> NamedDomainObjectCollection<S>.findByName(
  name: DomainObjectName<S>
): S? = findByName(name.value)

/**
 * Shorthand for `named(myDomainObjectName.value) { ... }`.
 *
 * @param name The name of the object to retrieve and configure.
 * @param configurationAction The configuration action.
 * @since 0.1.6
 * @throws UnknownDomainObjectException if the object is not found.
 */
public inline fun <reified S : Any> NamedDomainObjectCollection<S>.named(
  name: DomainObjectName<S>,
  configurationAction: Action<in S> = Actions.doNothing()
): NamedDomainObjectProvider<S> = named(name.value, configurationAction)

/**
 * Shorthand for `named(myDomainObjectName.value, S::class.java) { ... }`.
 *
 * @param name The name of the object to retrieve and configure.
 * @param type The type of the object to retrieve and configure.
 * @param configurationAction The configuration action.
 * @since 0.1.6
 * @throws UnknownDomainObjectException if the object is not found.
 */
public inline fun <reified S : Any> NamedDomainObjectCollection<S>.named(
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
 * @see DomainObjectCollection.withType
 * @since 0.1.0
 */
public inline fun <reified S : Any> DomainObjectCollection<in S>.withType(
  noinline configuration: (S) -> Unit
): DomainObjectCollection<S>? = withType(S::class.java, configuration)

/**
 * Returns a collection containing the objects in this collection of the given
 * type. The returned collection is live, so that when matching objects are later
 * added to this collection, they are also visible in the filtered collection.
 *
 * @return The matching objects. Returns an empty collection
 *   if there are no such objects in this collection.
 * @see DomainObjectCollection.withType
 * @since 0.1.0
 */
public inline fun <reified S : Any> DomainObjectCollection<in S>.withType(): DomainObjectCollection<S> =
  withType(S::class.java)

/**
 * Registers or retrieves a [T] by name and type, then configures it.
 *
 * @param name The name of the object.
 * @param configurationAction The configuration action.
 * @return A provider for the new object [T].
 * @since 0.1.0
 */
public fun <T> NamedDomainObjectContainer<T>.registerOnce(
  name: String,
  configurationAction: Action<in T> = Actions.doNothing()
): NamedDomainObjectProvider<T> =
  if (names.contains(name)) {
    named(name, configurationAction)
  } else {
    register(name, configurationAction)
  }

/**
 * Allows a [NamedDomainObjectCollection] to be used
 * as a property delegate, using the `KProperty` name.
 */
public operator fun <T : Any> NamedDomainObjectCollection<T>.provideDelegate(
  thisRef: Any?,
  property: KProperty<*>
): NamedDomainObjectProvider<T> = named(property.name)

/**
 * Delegate that calls `get()` on the provider the first time it's accessed.
 *
 * ```
 * // given:
 * val someObjectProvider: Provider<SomeObject> = ...
 *
 * // this delegate:
 * val someObject: SomeObject by someObjectProvider
 * ```
 */
public inline operator fun <reified T : Any, reified R : T> NamedDomainObjectProvider<out T>.getValue(
  thisRef: Any?,
  property: KProperty<*>
): R = get().castNamed(name, R::class)
