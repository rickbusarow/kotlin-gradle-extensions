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

import io.kotest.assertions.asClue
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.junit.jupiter.api.Test

class JvmTargetTest {

  @Test
  fun `fromInt maps to enums and toInt maps to the same version`() {

    val allTargets = mutableSetOf<JvmTarget>()

    forAll(
      row(8, JvmTarget.JVM_1_8),
      row(9, JvmTarget.JVM_9),
      row(10, JvmTarget.JVM_10),
      row(11, JvmTarget.JVM_11),
      row(12, JvmTarget.JVM_12),
      row(13, JvmTarget.JVM_13),
      row(14, JvmTarget.JVM_14),
      row(15, JvmTarget.JVM_15),
      row(16, JvmTarget.JVM_16),
      row(17, JvmTarget.JVM_17),
      row(18, JvmTarget.JVM_18),
      row(19, JvmTarget.JVM_19),
      row(20, JvmTarget.JVM_20),
      row(21, JvmTarget.JVM_21)
    ) { int, target ->

      allTargets.add(target)

      JvmTarget.fromInt(int) shouldBe target
      target.toInt() shouldBe int
    }

    "all defined enum values should be covered in this test".asClue {
      allTargets shouldContainExactly JvmTarget.values().toSet()
    }
  }
}
