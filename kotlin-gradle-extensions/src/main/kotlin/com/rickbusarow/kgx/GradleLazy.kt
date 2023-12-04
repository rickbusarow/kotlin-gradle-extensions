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

/**
 * Creates a new instance of the [GradleLazy] that uses the specified
 * initialization function [initializer] and thread-safety [mode].
 *
 * @param mode the thread-safety mode for initializing the value of the lazy property.
 * @param initializer the initialization function for the lazy property.
 *   This lambda must be compatible with Gradle's configuration cache.
 */
fun <T : Any?> gradleLazy(
  mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
  initializer: () -> T
): GradleLazy<T> = GradleLazy(mode, initializer)

/**
 * A [Lazy] implementation that is safe to use in Gradle build scripts. It serializes the
 * initializer lambda instead of the value, in case Gradle cannot serialize that value.
 */
class GradleLazy<T : Any?>(
  private val mode: LazyThreadSafetyMode,
  private val initializer: () -> T
) : Lazy<T>, java.io.Serializable {

  @Transient
  private var _value: Lazy<T> = lazy(mode, initializer)
  override val value: T
    get() = _value.value

  override fun isInitialized(): Boolean = _value.isInitialized()

  @Suppress("UnusedPrivateMember")
  private fun readObject(inStream: java.io.ObjectInputStream) {
    inStream.defaultReadObject()
    _value = lazy(mode, initializer)
  }
}
