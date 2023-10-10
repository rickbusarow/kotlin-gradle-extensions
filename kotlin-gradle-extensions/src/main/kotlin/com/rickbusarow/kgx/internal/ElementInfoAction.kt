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

package com.rickbusarow.kgx.internal

import com.rickbusarow.kgx.internal.ElementInfoAction.ElementValue.Instance
import com.rickbusarow.kgx.internal.ElementInfoAction.ElementValue.ProviderInstance
import com.rickbusarow.kgx.internal.ElementInfoAction.RegisteredElement
import dev.drewhamilton.poko.Poko
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.provider.Provider

/**
 * An alternative to [Action][org.gradle.api.Action]<[Pair][kotlin.Pair]<String, T>> or
 * [Action][org.gradle.api.Action]<[ElementInfo<T>][org.gradle.api.internal.DefaultNamedDomainObjectCollection.ElementInfo]>.
 *
 * This interface is intended to be used as a lambda parameter to the [whenElementKnown] function.
 *
 * @since 0.1.5
 */
fun interface ElementInfoAction<T : Any> : Action<RegisteredElement<T>> {

  /**
   * A public and stable version of
   * [DefaultNamedDomainObjectCollection.ElementInfo][org.gradle.api.internal.DefaultNamedDomainObjectCollection.ElementInfo],
   * representing an element being registered within a
   * [NamedDomainObjectCollection][org.gradle.api.NamedDomainObjectCollection].
   *
   * @since 0.1.5
   */
  sealed interface RegisteredElement<T> {
    /**
     * @see org.gradle.api.internal.DefaultNamedDomainObjectCollection.ElementInfo.getName
     * @since 0.1.5
     */
    val elementName: String

    /**
     * @see org.gradle.api.internal.DefaultNamedDomainObjectCollection.ElementInfo.getType
     * @since 0.1.5
     */
    val elementType: Class<out T>

    /**
     * The value of the element, which can be either an instance or a [Provider].
     *
     * @since 0.1.5
     */
    val elementValue: ElementValue<T>

    /**
     * @see elementName
     * @since 0.1.5
     */
    operator fun component1(): String = elementName

    /**
     * @see elementType
     * @since 0.1.5
     */
    operator fun component2(): Class<out T> = elementType

    /**
     * @see elementValue
     * @since 0.1.5
     */
    operator fun component3(): ElementValue<T> = elementValue

    /**
     * Represents a registered element backed by a concrete instance. This
     * is the most common case -- when the element is already realized.
     *
     * @property elementName see
     *   [org.gradle.api.internal.DefaultNamedDomainObjectCollection.ElementInfo.getName]
     * @property elementValue The concrete instance of the element.
     * @since 0.1.5
     */
    class ObjectBackedRegisteredElement<T : Any>(
      override val elementName: String,
      override val elementValue: Instance<T>
    ) : RegisteredElement<T> {
      /**
       * The concrete instance of the element.
       *
       * @since 0.1.5
       */
      val obj: T get() = elementValue.value
      override val elementType: Class<out T> = obj::class.java

      override fun component3(): Instance<T> = elementValue
    }

    /**
     * Represents a registered element backed by a [Provider]. This is the
     * case when the element is not yet realized. It's also the case when
     * the element is realized, but the realization state is not known.
     *
     * @property elementName see
     *   [org.gradle.api.internal.DefaultNamedDomainObjectCollection.ElementInfo.getName]
     * @property elementType see
     *   [org.gradle.api.internal.DefaultNamedDomainObjectCollection.ElementInfo.getType]
     * @property elementValue The [Provider] of the element.
     * @since 0.1.5
     */
    class ProviderBackedRegisteredElement<T : Any>(
      override val elementName: String,
      override val elementType: Class<out T>,
      override val elementValue: ProviderInstance<T>
    ) : RegisteredElement<T> {
      /**
       * The [Provider] of the element.
       *
       * @since 0.1.5
       */
      val provider: Provider<T> get() = elementValue.value

      override fun component3(): ProviderInstance<T> = elementValue
    }

    companion object {
      /**
       * Facilitates the creation of [RegisteredElement] instances based on the provided
       * parameters. It checks the type of [elementValue] to decide whether to create
       * an [ObjectBackedRegisteredElement] or a [ProviderBackedRegisteredElement].
       *
       * @param elementName see
       *   [org.gradle.api.internal.DefaultNamedDomainObjectCollection.ElementInfo.getName]
       * @param elementType see
       *   [org.gradle.api.internal.DefaultNamedDomainObjectCollection.ElementInfo.getType]
       * @param elementValue The value of the element,
       *   which can be either an instance or a provider.
       * @return A [RegisteredElement] instance, either [ObjectBackedRegisteredElement]
       *   or [ProviderBackedRegisteredElement], depending on the type of [elementValue].
       * @since 0.1.5
       */
      operator fun <T : Any> invoke(
        elementName: String,
        elementType: Class<out T>,
        elementValue: ElementValue<T>
      ): RegisteredElement<T> = when (elementValue) {
        is Instance<T> -> ObjectBackedRegisteredElement(
          elementName = elementName,
          elementValue = elementValue
        )

        is ProviderInstance<T> -> ProviderBackedRegisteredElement(
          elementName = elementName,
          elementType = elementType,
          elementValue = elementValue
        )
      }
    }
  }

  /**
   * Encapsulates the value of a registered element which can be either a concrete
   * instance or a [Provider]. This is useful when the element is not yet realized. It's
   * also useful when the element is realized, but the realization state is not known.
   *
   * @since 0.1.5
   */
  sealed interface ElementValue<T> {
    /**
     * The value of the element, which can be either an instance or a [Provider].
     *
     * @since 0.1.5
     */
    val value: Any?

    /**
     * Represents a concrete instance of a registered element. This is the most common case.
     *
     * @since 0.1.5
     */
    @Poko
    class Instance<T>(override val value: T) : ElementValue<T>

    /**
     * Represents a provider for a registered element. This
     * is the case when the element is not yet realized.
     *
     * @since 0.1.5
     */
    @JvmInline
    value class ProviderInstance<T>(
      override val value: NamedDomainObjectProvider<T>
    ) : ElementValue<T>

    companion object {
      /**
       * Facilitates the creation of an [Instance] from
       * a concrete value. This is the most common case.
       *
       * @param value The value of the element.
       * @return An [Instance] wrapping the provided value.
       * @since 0.1.5
       */
      operator fun <T : Any> invoke(value: T): Instance<T> = Instance(value)

      /**
       * Facilitates the creation of a [ProviderInstance] from a
       * [NamedDomainObjectProvider]. It checks the realization state of the
       * provider to decide whether to create an [Instance] or a [ProviderInstance].
       *
       * @param provider The provider of the element.* @return Either an [Instance]
       *   or a [ProviderInstance], based on the realization state of the provider.
       * @see NamedDomainObjectProvider.isRealized
       * @since 0.1.5
       * @throws IllegalStateException If an attempt is made to get
       *   the value from the provider and it's not yet realized.
       */
      @OptIn(InternalGradleApiAccess::class)
      operator fun <T : Any> invoke(provider: NamedDomainObjectProvider<T>): ElementValue<T> =
        when {
          provider.isRealized() -> Instance(provider.get())
          else -> ProviderInstance(provider)
        }
    }
  }
}
