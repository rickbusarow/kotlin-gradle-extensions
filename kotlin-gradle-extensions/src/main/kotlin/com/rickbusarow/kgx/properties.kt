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

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * Shorthand for:
 * ```
 * project.property(name) as T
 * ```
 *
 * Retrieves a property named [name], using the logic defined in [Project.property].
 * If the property is defined and its value is of type [T], that value is returned.
 * If the property is defined and is not of type [T], an exception is thrown.
 *
 * @param name the name of the property to retrieve
 * @since 0.1.10
 * @throws ClassCastException if the property is found but is not assignable to [T]
 * @throws groovy.lang.MissingPropertyException if the property is not found
 */
inline fun <reified T> Project.propertyAs(name: String): T = property(name) as T

/**
 * Shorthand for `providers.gradleProperty(name).get()`
 *
 * @since 0.1.10
 */
fun Project.gradleProperty(name: String): String = providers.gradleProperty(name).get()

/**
 * Shorthand for `providers.gradleProperty(name)`
 *
 * @since 0.1.10
 */
fun Project.gradlePropertyAsProvider(name: String): Provider<String> =
  providers.gradleProperty(name)

/**
 * Retrieves a property named [name]. If the property is defined and its value is of
 * type [T], that value is returned. If the property is defined and is not of type [T],
 * an exception is thrown. If the property is not defined, [defaultValue] is returned.
 *
 * @param name the name of the property to retrieve
 * @param defaultValue the value to return if the property is not defined
 * @since 0.1.8
 * @throws ClassCastException if the property is found but is not assignable to [T]
 */
inline fun <reified T> Project.property(
  name: String,
  defaultValue: T
): T = propertyOrNull<T>(name) ?: defaultValue

/**
 * Retrieves a property named [name]. If the property is defined and its value is of
 * type [T], that value is returned. If the property is defined and is not of type [T],
 * an exception is thrown. If the property is not defined, [defaultValue] is returned.
 *
 * @param name the name of the property to retrieve
 * @param defaultValue the value to return if the property is not defined
 * @since 0.1.8
 * @throws ClassCastException if the property is found but is not assignable to [T]
 */
inline fun <reified T> Project.property(
  name: String,
  defaultValue: () -> T
): T = propertyOrNull<T>(name) ?: defaultValue()

/**
 * Retrieves a property named [name]. If the property is defined and its value is of
 * type [T], that value is returned. If the property is defined and is not of type
 * [T], an exception is thrown. If the property is not defined, `null` is returned.
 *
 * @param name the name of the property to retrieve
 * @since 0.1.8
 * @throws ClassCastException if the property is found but is not assignable to [T]
 */
inline fun <reified T> Project.propertyOrNull(name: String): T? {

  val found = findProperty(name)

  if (found != null) {
    if (found is T) return found
    throw GradleException(
      "Property '$name' is of type ${found::class.qualifiedName}, " +
        "which is not assignable to the requested type of ${T::class.java.simpleName}"
    )
  }

  return null
}

/**
 * Chains all providers together via `orElse(...)`, then adds
 * the result as a convention to the receiver [Property].
 *
 * These are equivalent:
 * ```
 * property.convention(default.orElse(otherA.orElse(otherB)))
 *
 * property.convention(default, otherA, otherB)
 * ```
 *
 * @since 0.1.10
 */
fun <T> Property<T>.convention(
  default: Provider<T>,
  vararg additionalDefaults: Provider<T>
): Property<T> = convention(default.orElse(*additionalDefaults))

/**
 * Chains all providers together via `orElse(...)`, then adds
 * the result as a convention to the receiver [Property].
 *
 * These are equivalent:
 * ```
 * property.convention(default.orElse(otherA.orElse(otherB)))
 *
 * property.convention(default, otherA, otherB)
 * ```
 *
 * @since 0.1.10
 */
fun <T> Property<T>.convention(
  default: Provider<T>,
  additionalDefaults: List<Provider<T>>
): Property<T> = convention(default.orElse(additionalDefaults))

/**
 * Chains all providers together via `orElse(...)`.
 *
 * These are equivalent:
 * ```
 * provider.orElse(otherA.orElse(otherB))
 *
 * provider.orElse(otherA, otherB)
 * ```
 *
 * @since 0.1.10
 */
fun <T> Provider<T>.orElse(
  vararg additionalDefaults: Provider<T>
): Provider<T> = additionalDefaults.fold(this) { acc, other ->
  acc.orElse(other)
}

/**
 * Chains all providers together via `orElse(...)`.
 *
 * These are equivalent:
 * ```
 * provider.orElse(otherA.orElse(otherB))
 *
 * provider.orElse(listOf(otherA, otherB))
 * ```
 *
 * @since 0.1.10
 */
fun <T> Provider<T>.orElse(
  additionalDefaults: List<Provider<T>>
): Provider<T> = additionalDefaults.fold(this) { acc, other ->
  acc.orElse(other)
}
