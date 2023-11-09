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

package com.rickbusarow.kgx.names

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A type-safe wrapper for a domain object name.
 *
 * `S` is the type of the domain object, like [Task][org.gradle.api.Task]
 * or [SourceSet][org.gradle.api.tasks.SourceSet].
 *
 * @since 0.1.6
 */
interface DomainObjectName<in S : Any> {
  /**
   * The unqualified, simple name of the domain object.
   *
   * @since 0.1.6
   */
  val value: String

  companion object {
    /**
     * Returns an instance of [DomainObjectName] with the given [name], bound to the type of [S].
     *
     * @since 0.1.6
     */
    fun <S : Any> domainObjectName(name: String): DomainObjectName<S> {
      return object : DomainObjectName<S> {
        override val value: String = name
      }
    }

    internal inline fun <reified S : DomainObjectName<T>, reified T : Any> lazyName(
      crossinline init: (
        @ParameterName("name")
        String
      ) -> S
    ): ReadOnlyProperty<Any?, S> {
      return object : ReadOnlyProperty<Any?, S> {
        var name: S? = null

        override fun getValue(
          thisRef: Any?,
          property: KProperty<*>
        ): S {
          if (name == null) {
            name = init(property.name)
          }
          return name as S
        }
      }
    }
  }
}
