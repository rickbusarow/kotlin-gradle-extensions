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

import com.rickbusarow.kgx.SourceSetName.Companion.addPrefix
import com.rickbusarow.kgx.SourceSetName.Companion.addSuffix
import com.rickbusarow.kgx.SourceSetName.Companion.apiConfig
import com.rickbusarow.kgx.SourceSetName.Companion.compileOnlyConfig
import com.rickbusarow.kgx.SourceSetName.Companion.hasPrefix
import com.rickbusarow.kgx.SourceSetName.Companion.implementationConfig
import com.rickbusarow.kgx.SourceSetName.Companion.isMain
import com.rickbusarow.kgx.SourceSetName.Companion.isTestFixtures
import com.rickbusarow.kgx.SourceSetName.Companion.kaptVariant
import com.rickbusarow.kgx.SourceSetName.Companion.removePrefix
import com.rickbusarow.kgx.SourceSetName.Companion.removeSuffix
import com.rickbusarow.kgx.SourceSetName.Companion.runtimeOnlyConfig
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class SourceSetNameTest {

  @Test
  fun `isTestFixtures should identify testFixtures source sets correctly`() {
    SourceSetName("testFixtures").isTestFixtures() shouldBe true
    SourceSetName("testFixturesDebug").isTestFixtures() shouldBe true
    SourceSetName("main").isTestFixtures() shouldBe false
  }

  @Test
  fun `isMain should identify main source set correctly`() {
    SourceSetName("main").isMain() shouldBe true
    SourceSetName("debug").isMain() shouldBe false
  }

  @Test
  fun `removePrefix should remove prefix correctly`() {
    SourceSetName("debugMain").removePrefix("debug").value shouldBe "main"
    SourceSetName("testFixturesDebug").removePrefix("testFixtures").value shouldBe "debug"
  }

  @Test
  fun `hasPrefix should check prefix correctly`() {
    SourceSetName("debugMain").hasPrefix("debug") shouldBe true
    SourceSetName("main").hasPrefix("debug") shouldBe false
  }

  @Test
  fun `addPrefix should add prefix correctly`() {
    SourceSetName("main").addPrefix("debug").value shouldBe "debugMain"
    SourceSetName("debug").addPrefix("testFixtures").value shouldBe "testFixturesDebug"
  }

  @Test
  fun `removeSuffix should remove suffix correctly`() {
    SourceSetName("debugMain").removeSuffix("Main").value shouldBe "debug"
    SourceSetName("testFixturesDebug").removeSuffix("Debug").value shouldBe "testFixtures"
  }

  @Test
  fun `addSuffix should add suffix correctly`() {
    SourceSetName("main").addSuffix("Debug").value shouldBe "mainDebug"
    SourceSetName("debug").addSuffix("TestFixtures").value shouldBe "debugTestFixtures"
  }

  @Test
  fun `apiConfig should return correct ConfigurationName`() {
    SourceSetName("main").apiConfig().value shouldBe "api"
    SourceSetName("debug").apiConfig().value shouldBe "debugApi"
  }

  @Test
  fun `compileOnlyConfig should return correct ConfigurationName`() {
    SourceSetName("main").compileOnlyConfig().value shouldBe "api"
    SourceSetName("debug").compileOnlyConfig().value shouldBe "compileOnlyDebug"
  }

  @Test
  fun `implementationConfig should return correct ConfigurationName`() {
    SourceSetName("main").implementationConfig().value shouldBe "implementation"
    SourceSetName("debug").implementationConfig().value shouldBe "debugImplementation"
  }

  @Test
  fun `runtimeOnlyConfig should return correct ConfigurationName`() {
    SourceSetName("main").runtimeOnlyConfig().value shouldBe "api"
    SourceSetName("debug").runtimeOnlyConfig().value shouldBe "runtimeOnlyDebug"
  }

  @Test
  fun `kaptVariant should return correct ConfigurationName`() {
    SourceSetName("main").kaptVariant().value shouldBe "kapt"
    SourceSetName("debug").kaptVariant().value shouldBe "kaptDebug"
  }

  @Test
  fun `addPrefix should capitalize first letter of original name`() {
    SourceSetName("main").addPrefix("DEBUG").value shouldBe "DEBUGMain"
  }

  @Test
  fun `addSuffix should capitalize first letter of suffix`() {
    SourceSetName("main").addSuffix("debug").value shouldBe "mainDebug"
  }

  @Test
  fun `apiConfig should handle custom source sets`() {
    SourceSetName("custom").apiConfig().value shouldBe "customApi"
  }

  @Test
  fun `compileOnlyConfig should handle custom source sets`() {
    SourceSetName("custom").compileOnlyConfig().value shouldBe "compileOnlyCustom"
  }

  @Test
  fun `implementationConfig should handle custom source sets`() {
    SourceSetName("custom").implementationConfig().value shouldBe "customImplementation"
  }

  @Test
  fun `runtimeOnlyConfig should handle custom source sets`() {
    SourceSetName("custom").runtimeOnlyConfig().value shouldBe "runtimeOnlyCustom"
  }

  @Test
  fun `kaptVariant should handle custom source sets`() {
    SourceSetName("custom").kaptVariant().value shouldBe "kaptCustom"
  }

  @Test
  fun `toString should return correct string representation`() {
    SourceSetName("main").toString() shouldBe "(SourceSetName) `main`"
  }

  @Test
  fun `SourceSetName should be sortable`() {
    val list = listOf(
      SourceSetName("main"),
      SourceSetName("debug"),
      SourceSetName("test"),
      SourceSetName("release")
    )
    val sortedList = list.sortedBy { it.value }
    sortedList.map { it.value } shouldBe listOf("debug", "main", "release", "test")
  }

  @Test
  fun `sourceSetName delegate should infer the correct name`() {
    val main: SourceSetName by SourceSetName.sourceSetName()
    main.value shouldBe "main"

    val custom: SourceSetName by SourceSetName.sourceSetName()
    custom.value shouldBe "custom"
  }
}
