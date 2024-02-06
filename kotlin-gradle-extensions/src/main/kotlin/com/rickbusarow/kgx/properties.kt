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

import com.rickbusarow.kgx.stdlib.castNamed
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
public inline fun <reified T> Project.propertyAs(name: String): T = property(name) as T

/**
 * Shorthand for `providers.gradleProperty(name).get()`
 *
 * @since 0.1.10
 */
public fun Project.gradleProperty(name: String): String = providers.gradleProperty(name).get()

/**
 * Shorthand for `providers.gradleProperty(name)`
 *
 * @since 0.1.10
 */
public fun Project.gradlePropertyAsProvider(name: String): Provider<String> =
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
public inline fun <reified T : Any> Project.property(
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
public inline fun <reified T : Any> Project.property(
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
public inline fun <reified T : Any> Project.propertyOrNull(name: String): T? {
  return findProperty(name)?.castNamed(name)
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
public fun <T> Property<T>.convention(
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
public fun <T> Property<T>.convention(
  default: Provider<T>,
  additionalDefaults: List<Provider<T>>
): Property<T> = convention(default.orElse(additionalDefaults))
