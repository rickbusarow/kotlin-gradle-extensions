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

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.matchers.shouldBe
import org.gradle.api.internal.provider.MissingValueException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NamedDomainObjectProviderKtTest {

  @Nested
  inner class `getValue` {

    @Test
    fun `getter is only called lazily`() {

      var getterCalled = false

      val provider = namedDomainObjectProvider("bar") {
        getterCalled = true
        "foo"
      }

      val value: String by provider

      getterCalled shouldBe false

      value shouldBe "foo"

      getterCalled shouldBe true
    }

    @Test
    fun `getter is called for every property read instead of caching`() {

      var getterCalled = 0

      val provider = namedDomainObjectProvider("bar") {
        getterCalled++
        "foo"
      }

      val value: String by provider

      getterCalled shouldBe 0

      value shouldBe "foo"
      getterCalled shouldBe 1

      value shouldBe "foo"
      getterCalled shouldBe 2
    }

    @Test
    fun `a null provider value throws when read`() {

      val provider = namedDomainObjectProvider<String>("bar") { null }

      val value: String by provider

      shouldThrow<MissingValueException> {
        value shouldBe null
      }
    }

    @Test
    fun `a non-null value of the wrong type throws with a detailed message`() {

      val provider = namedDomainObjectProvider<Dog>("retriever") {
        StandardPoodle("Frenchy")
      }

      val value: Retriever by provider

      shouldThrowWithMessage<ClassCastException>(
        """
          Property 'retriever' cannot be cast to the requested type.
                    value: StandardPoodle(name=Frenchy)
              actual type: ${StandardPoodle::class.qualifiedName}
            required type: ${Retriever::class.qualifiedName}
        """.trimIndent()
      ) {
        value shouldBe null
      }
    }

    @Test
    fun `a value of an assignable type is cast`() {

      val provider = namedDomainObjectProvider<Dog>("standard-poodle") {
        StandardPoodle("Frenchy")
      }

      val dog: Dog by provider
      dog shouldBe StandardPoodle("Frenchy")

      val poodle: Poodle by provider
      poodle shouldBe StandardPoodle("Frenchy")

      val standardPoodle: StandardPoodle by provider
      standardPoodle shouldBe StandardPoodle("Frenchy")
    }
  }
}

interface Dog {
  val name: String
}

interface Retriever : Dog
interface Poodle : Dog
data class StandardPoodle(override val name: String) : Poodle
