/*
 * Copyright (C) 2025 Rick Busarow
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

@file:Suppress("ForbiddenImport")

package com.rickbusarow.kgx.internal

import com.rickbusarow.kgx.internal.ElementInfoAction.ElementValue
import com.rickbusarow.kgx.internal.ElementInfoAction.ElementValue.Instance
import com.rickbusarow.kgx.internal.ElementInfoAction.ElementValue.ProviderInstance
import com.rickbusarow.kgx.internal.ElementInfoAction.RegisteredElement
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.internal.DefaultNamedDomainObjectCollection
import org.gradle.api.internal.DefaultNamedDomainObjectCollection.ElementInfo
import com.rickbusarow.kgx.invoke as kgxInvoke

/**
 * Invokes this [ElementInfoAction] with the provided element name and
 * value. The type of the element is inferred from the type of the value.
 *
 * @param elementName The name of the element.
 * @param elementValue The value of the element, encapsulated in an [ElementValue].
 * @receiver [ElementInfoAction] The action to be executed.
 * @since 0.1.5
 */
@Deprecated(
  "moved to `com.rickbusarow.kgx.invoke`",
  ReplaceWith("invoke(elementName, elementValue)", "com.rickbusarow.kgx.invoke")
)
public inline operator fun <reified T : Any> ElementInfoAction<T>.invoke(
  elementName: String,
  elementValue: ElementValue<T>
) {
  kgxInvoke(elementName, elementValue)
}

/**
 * Invokes this [ElementInfoAction] with the provided element name, type and value.
 * This is useful when the type of the element is not the same as the type of the value.
 *
 * @param elementName The name of the element.
 * @param elementType The type of the element.
 * @param elementValue The value of the element, encapsulated in an [ElementValue].
 * @receiver [ElementInfoAction] The action to be executed.
 * @since 0.1.5
 */
@Deprecated(
  "moved to `com.rickbusarow.kgx.invoke`",
  ReplaceWith("invoke(elementName, elementType, elementValue)", "com.rickbusarow.kgx.invoke")
)
public operator fun <T : Any> ElementInfoAction<T>.invoke(
  elementName: String,
  elementType: Class<out T>,
  elementValue: ElementValue<T>
) {
  kgxInvoke(elementName, elementType, elementValue)
}

/**
 * Checks if the [NamedDomainObjectProvider] has been realized.
 *
 * @receiver [NamedDomainObjectProvider] The provider to be checked.
 * @return `true` if the provider has been realized, `false` otherwise.
 * @since 0.1.5
 */
@InternalGradleApiAccess
public fun <T : Any> NamedDomainObjectProvider<T>.isRealized(): Boolean {
  return this is DefaultNamedDomainObjectCollection<*>.AbstractDomainObjectCreatingProvider<*>
}

/**
 * Checks if an element with the specified name has been realized in the collection. This
 * is useful when the element is not yet realized, but the realization state is not known.
 *
 * @param elementName The name of the element to check.
 * @receiver [NamedDomainObjectCollection] The collection to be checked.
 * @return `true` if the element has been realized, `false` otherwise.
 * @since 0.1.5
 */
@InternalGradleApiAccess
public fun <T : Any> NamedDomainObjectCollection<T>.hasRealized(elementName: String): Boolean {
  return names.contains(elementName) && named(elementName).isRealized()
}

/**
 * Converts an [ElementInfo] to a [RegisteredElement], facilitating
 * a more structured interaction with the element information.
 *
 * @param namedDomainObjectCollection The collection containing the element.
 * @receiver [ElementInfo] The information of the element to be converted.
 * @return A [RegisteredElement] encapsulating the element information.
 * @since 0.1.5
 */
@PublishedApi
@OptIn(InternalGradleApiAccess::class)
internal inline fun <reified T : Any> ElementInfo<T>.toRegisteredInfo(
  namedDomainObjectCollection: NamedDomainObjectCollection<T>
): RegisteredElement<T> {

  val provider = namedDomainObjectCollection.named(name)

  return when {
    provider.isRealized() ->
      RegisteredElement.ObjectBackedRegisteredElement(
        elementName = name,
        elementValue = Instance(provider.get())
      )

    else ->
      RegisteredElement.ProviderBackedRegisteredElement<T>(
        elementName = name,
        elementType = T::class.java,
        elementValue = ProviderInstance(provider)
      )
  }
}

/**
 * Invokes a given [Action] when an element is registered in the collection,
 * without triggering its creation or configuration. The given [configurationAction]
 * is executed against the object before it is returned from the provider.
 *
 * @param configurationAction The [Action] to execute on the newly registered element.
 * @receiver [NamedDomainObjectCollection] where the element is being registered.
 * @since 0.1.0
 * @throws IllegalArgumentException If the receiver is not a [DefaultNamedDomainObjectCollection].
 */
@InternalGradleApiAccess
public inline fun <reified T> NamedDomainObjectCollection<T>.whenElementRegistered(
  configurationAction: Action<T>
) {
  whenElementKnown {
    named(it.name, T::class.java, configurationAction)
  }
}

/**
 * Invokes a given [Action] when an element is registered in the collection,
 * without triggering its creation or configuration. The given [configurationAction]
 * is executed against the object before it is returned from the provider.
 *
 * @param configurationAction The [Action] to execute on the newly registered element.
 * @receiver [NamedDomainObjectCollection] where the element is being registered.
 * @since 0.1.0
 * @throws IllegalArgumentException If the receiver is not a [DefaultNamedDomainObjectCollection].
 */
@InternalGradleApiAccess
public inline fun <reified T> NamedDomainObjectCollection<T>.whenElementKnown(
  configurationAction: Action<ElementInfo<T>>
) {
  requireDefaultCollection().whenElementKnown(configurationAction)
}

/**
 * Invokes a given [Action] when an element is registered in the collection,
 * without triggering its creation or configuration. The given [configurationAction]
 * is executed against the object before it is returned from the provider.
 *
 * @param configurationAction The [Action] to execute on the newly registered element.
 * @receiver [NamedDomainObjectCollection] where the element is being registered.
 * @since 0.1.0
 * @throws IllegalArgumentException If the receiver is not a [DefaultNamedDomainObjectCollection].
 */
@InternalGradleApiAccess
public inline fun <reified T : Any> NamedDomainObjectCollection<T>.whenElementKnown(
  configurationAction: ElementInfoAction<T>
) {
  requireDefaultCollection().whenElementKnown { elementInfo ->
    @Suppress("UNCHECKED_CAST")
    val elementType = elementInfo.type as Class<T>
    configurationAction.kgxInvoke(
      elementName = elementInfo.name,
      elementType = elementType,
      elementValue = ElementValue(named(elementInfo.name))
    )
  }
}

/**
 * Invokes a given [Action] when an element with a specific name is registered
 * in the collection, without triggering its creation or configuration. The given
 * [configurationAction] is executed against the object before it is returned from the provider.
 *
 * @param elementName The name of the element to observe.
 * @param configurationAction The [Action] to execute on the newly registered element.
 * @receiver [NamedDomainObjectCollection] where the element is being registered.
 * @since 0.1.0
 * @throws IllegalArgumentException If the receiver is not a [DefaultNamedDomainObjectCollection].
 */
@InternalGradleApiAccess
public inline fun <reified T> NamedDomainObjectCollection<T>.whenElementRegistered(
  elementName: String,
  configurationAction: Action<T>
) {
  whenElementKnown { info ->
    if (info.name == elementName) {
      named(elementName, configurationAction)
    }
  }
}

/**
 * Invokes a given [Action] when an element with a specific name and type is registered
 * in the collection, without triggering its creation or configuration. The given
 * [configurationAction] is executed against the object before it is returned from the provider.
 *
 * @param elementName The name of the element to observe.
 * @param configurationAction The [Action] to execute on the newly registered element.
 * @receiver [NamedDomainObjectCollection] where the element is being registered.
 * @since 0.1.0
 * @throws IllegalArgumentException If the receiver is not a [DefaultNamedDomainObjectCollection].
 */
@JvmName("whenElementRegisteredTyped")
@InternalGradleApiAccess
public inline fun <reified T, reified R : T> NamedDomainObjectCollection<T>.whenElementRegistered(
  elementName: String,
  configurationAction: Action<R>
) {
  whenElementKnown { info ->
    if (info.name == elementName) {
      named(elementName, R::class.java, configurationAction)
    }
  }
}

/** @since 0.1.1 */
@InternalGradleApiAccess
@Suppress("MaxLineLength")
public inline fun <reified T> NamedDomainObjectCollection<T>.requireDefaultCollection(): DefaultNamedDomainObjectCollection<T> {
  require(this is DefaultNamedDomainObjectCollection<T>) {
    "The receiver collection must extend " +
      "${DefaultNamedDomainObjectCollection::class.qualifiedName}, " +
      "but this elementType is ${this::class.java.canonicalName}."
  }
  return this@requireDefaultCollection
}
