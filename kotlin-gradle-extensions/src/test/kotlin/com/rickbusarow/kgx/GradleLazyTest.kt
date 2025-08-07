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

package com.rickbusarow.kgx

import com.rickbusarow.kase.HasParams
import com.rickbusarow.kase.Kase1
import com.rickbusarow.kase.kases
import com.rickbusarow.kase.testFactory
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.TestFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class GradleLazyTest : HasParams<Kase1<LazyThreadSafetyMode>> {

  override val params = kases(
    LazyThreadSafetyMode.entries,
    displayNameFactory = { "mode: $a1" }
  )

  @TestFactory
  fun `isInitialized is only true after the value is accessed`() = testFactory { (mode) ->

    val lazy = gradleLazy(mode) { "foo" }

    lazy.isInitialized() shouldBe false

    lazy.value shouldBe "foo"

    lazy.isInitialized() shouldBe true
  }

  @TestFactory
  fun `serializes and deserializes with the value is Serializable`() = testFactory { (mode) ->

    val lazy = gradleLazy(mode) { "foo" }

    lazy.value shouldBe "foo"

    val cycled = lazy.cycle()

    cycled.value shouldBe "foo"
  }

  @TestFactory
  fun `lazy instance works as a delegate`() = testFactory { (mode) ->

    val holder = LazyHolder(mode) { "foo" }

    holder.lazy shouldBe "foo"

    val cycled = holder.cycle()

    cycled.lazy shouldBe "foo"
  }

  @Suppress("UNCHECKED_CAST")
  fun <T : Serializable> T.cycle(): T {
    val bytes = ByteArrayOutputStream()
      .also { bytes ->
        ObjectOutputStream(bytes).use { it.writeObject(this@cycle) }
      }
      .toByteArray()

    return ObjectInputStream(ByteArrayInputStream(bytes)).use { it.readObject() as T }
  }

  class LazyHolder<T>(mode: LazyThreadSafetyMode, initializer: SerializableLambda<T>) : Serializable {
    val lazy by gradleLazy(mode, initializer)
  }
}
