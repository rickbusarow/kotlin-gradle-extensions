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

@file:Suppress("ForbiddenImport")

package com.rickbusarow.kgx

import io.kotest.matchers.shouldBe
import org.gradle.api.internal.provider.DefaultProperty
import org.gradle.api.internal.provider.DefaultProvider
import org.gradle.api.internal.provider.PropertyHost
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.concurrent.Callable

class ProvidersTest {

  @Nested
  inner class `convention` {

    @Test
    fun `convention with single value just resolves to Gradle's member function`() {

      val root = property<String>()

      val rootConvention = root.convention(provider { "a" })

      rootConvention.orNull shouldBe "a"
    }

    @Test
    fun `convention with a multi-value vararg`() {

      val root = property<String>()

      val rootConvention = root.convention(
        provider { null },
        provider { "foo" },
        provider { "bar" }
      )

      rootConvention.get() shouldBe "foo"
    }

    @Test
    fun `convention with an empty list`() {

      val root = property<String>()

      val rootConvention = root.convention(
        provider { null },
        emptyList()
      )

      rootConvention.orNull shouldBe null
    }

    @Test
    fun `convention with a single-value list`() {

      val root = property<String>()

      val rootConvention = root.convention(
        provider { null },
        listOf(
          provider { "foo" }
        )
      )

      rootConvention.get() shouldBe "foo"
    }

    @Test
    fun `convention with a multi-value list`() {

      val root = property<String>()

      val rootConvention = root.convention(
        provider { null },
        listOf(
          provider { null },
          provider { "foo" },
          provider { "bar" }
        )
      )

      rootConvention.get() shouldBe "foo"
    }
  }

  @Nested
  inner class `orElse` {

    @Test
    fun `orElse with no args resolves to the extension`() {

      val root = provider<String> { null }

      val rootOrElse = root.orElse()

      rootOrElse.orNull shouldBe null
    }

    @Test
    fun `orElse with single value just resolves to Gradle's member function`() {

      val root = provider<String> { null }

      val rootOrElse = root.orElse(provider { null })

      rootOrElse.orNull shouldBe null
    }

    @Test
    fun `orElse with a multi-value vararg`() {

      val root = provider<String> { null }

      val rootOrElse = root.orElse(
        provider { null },
        provider { "foo" },
        provider { "bar" }
      )

      rootOrElse.get() shouldBe "foo"
    }

    @Test
    fun `orElse with an empty list`() {

      val root = provider<String> { null }

      val rootOrElse = root.orElse(emptyList())

      rootOrElse.orNull shouldBe null
    }

    @Test
    fun `orElse with a single-value list`() {

      val root = provider<String> { null }

      val rootOrElse = root.orElse(
        listOf(
          provider { "foo" }
        )
      )

      rootOrElse.get() shouldBe "foo"
    }

    @Test
    fun `orElse with a multi-value list`() {

      val root = provider<String> { null }

      val rootOrElse = root.orElse(
        listOf(
          provider { null },
          provider { "foo" },
          provider { "bar" }
        )
      )

      rootOrElse.get() shouldBe "foo"
    }
  }
}

inline fun <reified T> provider(value: Callable<T?>): Provider<T> = DefaultProvider(value)
inline fun <reified T> property(): Property<T> = DefaultProperty(PropertyHost.NO_OP, T::class.java)
