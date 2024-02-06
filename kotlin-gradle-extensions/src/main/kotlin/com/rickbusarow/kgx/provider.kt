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

import org.gradle.api.provider.Provider
import kotlin.reflect.KProperty

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
public fun <T> Provider<T>.orElse(
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
public fun <T> Provider<T>.orElse(
  additionalDefaults: List<Provider<T>>
): Provider<T> = additionalDefaults.fold(this) { acc, other ->
  acc.orElse(other)
}

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
 *
 * @throws org.gradle.api.internal.provider.MissingValueException if the provider returns `null`
 */
public inline operator fun <reified T : Any> Provider<out T>.getValue(
  thisRef: Any?,
  property: KProperty<*>
): T = get()
