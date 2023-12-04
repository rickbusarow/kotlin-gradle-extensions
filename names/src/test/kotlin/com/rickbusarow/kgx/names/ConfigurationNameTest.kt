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

import com.rickbusarow.kgx.names.ConfigurationName.Companion.anvilConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.apiConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.compileOnlyConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.implementationConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.kaptConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.kspConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.runtimeOnlyConfig
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ConfigurationNameTest {
  @Test
  fun `toSourceSetName should return correct SourceSetName`() {
    val configName1 = ConfigurationName("api")
    configName1.toSourceSetName().value shouldBe "main"

    val configName2 = ConfigurationName("debugImplementation")
    configName2.toSourceSetName().value shouldBe "debug"

    val configName3 = ConfigurationName("testImplementation")
    configName3.toSourceSetName().value shouldBe "test"
  }

  @Test
  fun `nameWithoutSourceSet should return base name without source set prefix`() {
    val configName1 = ConfigurationName("api")
    configName1.nameWithoutSourceSet() shouldBe "api"

    val configName2 = ConfigurationName("debugApi")
    configName2.nameWithoutSourceSet() shouldBe "Api"

    val configName3 = ConfigurationName("testImplementation")
    configName3.nameWithoutSourceSet() shouldBe "Implementation"
  }

  @Test
  fun `switchSourceSet should switch source set correctly`() {
    val configName1 = ConfigurationName("api")
    configName1.switchSourceSet(SourceSetName("test")).value shouldBe "testApi"

    val configName2 = ConfigurationName("debugApi")
    configName2.switchSourceSet(SourceSetName("release")).value shouldBe "releaseApi"
  }

  @Test
  fun `apiVariant should return api variant correctly`() {
    val configName1 = ConfigurationName("api")
    configName1.apiVariant().value shouldBe "api"

    val configName2 = ConfigurationName("debugImplementation")
    configName2.apiVariant().value shouldBe "debugApi"
  }

  @Test
  fun `implementationVariant should return implementation variant correctly`() {
    val configName1 = ConfigurationName("api")
    configName1.implementationVariant().value shouldBe "implementation"

    val configName2 = ConfigurationName("debugApi")
    configName2.implementationVariant().value shouldBe "debugImplementation"
  }

  @Test
  fun `kaptVariant should return kapt variant correctly`() {
    val configName1 = ConfigurationName("api")
    configName1.kaptVariant().value shouldBe "kapt"

    val configName2 = ConfigurationName("debugApi")
    configName2.kaptVariant().value shouldBe "kaptDebug"
  }

  @Test
  fun `isApi should return true for api variants`() {
    val configName1 = ConfigurationName("api")
    configName1.isApi() shouldBe true

    val configName2 = ConfigurationName("debugApi")
    configName2.isApi() shouldBe true
  }

  @Test
  fun `isImplementation should return true for implementation variants`() {
    val configName1 = ConfigurationName("implementation")
    configName1.isImplementation() shouldBe true

    val configName2 = ConfigurationName("debugImplementation")
    configName2.isImplementation() shouldBe true
  }

  @Test
  fun `isKapt should return true for kapt variants`() {
    val configName1 = ConfigurationName("kapt")
    configName1.isKapt() shouldBe true

    val configName2 = ConfigurationName("kaptDebug")
    configName2.isKapt() shouldBe true
  }

  @Test
  fun `toSourceSetName should return correct SourceSetName for main configurations`() {
    val configName1 = ConfigurationName("api")
    val configName2 = ConfigurationName("implementation")
    val configName3 = ConfigurationName("compileOnly")

    configName1.toSourceSetName().value shouldBe "main"
    configName2.toSourceSetName().value shouldBe "main"
    configName3.toSourceSetName().value shouldBe "main"
  }

  @Test
  fun `toSourceSetName should return correct SourceSetName for non-main configurations`() {
    val configName1 = ConfigurationName("debugApi")
    val configName2 = ConfigurationName("releaseImplementation")
    val configName3 = ConfigurationName("testCompileOnly")

    configName1.toSourceSetName().value shouldBe "debug"
    configName2.toSourceSetName().value shouldBe "release"
    configName3.toSourceSetName().value shouldBe "test"
  }

  @Test
  fun `nameWithoutSourceSet should return correct base name for main configurations`() {
    val configName1 = ConfigurationName("api")
    val configName2 = ConfigurationName("implementation")
    val configName3 = ConfigurationName("compileOnly")

    configName1.nameWithoutSourceSet() shouldBe "api"
    configName2.nameWithoutSourceSet() shouldBe "implementation"
    configName3.nameWithoutSourceSet() shouldBe "compileOnly"
  }

  @Test
  fun `nameWithoutSourceSet should return correct base name for non-main configurations`() {
    val configName1 = ConfigurationName("debugApi")
    val configName2 = ConfigurationName("releaseImplementation")
    val configName3 = ConfigurationName("testCompileOnly")

    configName1.nameWithoutSourceSet() shouldBe "Api"
    configName2.nameWithoutSourceSet() shouldBe "Implementation"
    configName3.nameWithoutSourceSet() shouldBe "CompileOnly"
  }

  @Test
  fun `switchSourceSet should correctly switch source set for main configurations`() {
    val configName1 = ConfigurationName("api")
    val configName2 = ConfigurationName("implementation")

    configName1.switchSourceSet(SourceSetName("test")).value shouldBe "testApi"
    configName2.switchSourceSet(SourceSetName("debug")).value shouldBe "debugImplementation"
  }

  @Test
  fun `switchSourceSet should correctly switch source set for non-main configurations`() {
    val configName1 = ConfigurationName("debugApi")
    val configName2 = ConfigurationName("releaseImplementation")

    configName1.switchSourceSet(SourceSetName("test")).value shouldBe "testApi"
    configName2.switchSourceSet(SourceSetName("debug")).value shouldBe "debugImplementation"
  }

  @Test
  fun `isApi should correctly identify api configurations`() {
    val configName1 = ConfigurationName("api")
    val configName2 = ConfigurationName("debugApi")
    val configName3 = ConfigurationName("releaseImplementation")

    configName1.isApi() shouldBe true
    configName2.isApi() shouldBe true
    configName3.isApi() shouldBe false
  }

  @Test
  fun `isImplementation should correctly identify implementation configurations`() {
    val configName1 = ConfigurationName("implementation")
    val configName2 = ConfigurationName("debugImplementation")
    val configName3 = ConfigurationName("releaseApi")

    configName1.isImplementation() shouldBe true
    configName2.isImplementation() shouldBe true
    configName3.isImplementation() shouldBe false
  }

  @Test
  fun `isKapt should correctly identify kapt configurations`() {
    val configName1 = ConfigurationName("kapt")
    val configName2 = ConfigurationName("kaptDebug")
    val configName3 = ConfigurationName("releaseApi")

    configName1.isKapt() shouldBe true
    configName2.isKapt() shouldBe true
    configName3.isKapt() shouldBe false
  }

  @Test
  fun `compareTo should correctly compare ConfigurationNames`() {
    val configName1 = ConfigurationName("api")
    val configName2 = ConfigurationName("debugApi")
    val configName3 = ConfigurationName("api")

    configName1.compareTo(configName2) shouldBe -3
    configName1.compareTo(configName3) shouldBe 0
    configName2.compareTo(configName1) shouldBe 3
  }

  @Nested
  inner class `base config factory extensions` {
    @Test
    fun `main configs`() {

      SourceSetName.main.apiConfig() shouldBe ConfigurationName.api
      SourceSetName.main.apiConfig().value shouldBe "api"

      SourceSetName.main.compileOnlyConfig() shouldBe ConfigurationName.compileOnly
      SourceSetName.main.compileOnlyConfig().value shouldBe "compileOnly"

      SourceSetName.main.implementationConfig() shouldBe ConfigurationName.implementation
      SourceSetName.main.implementationConfig().value shouldBe "implementation"

      SourceSetName.main.runtimeOnlyConfig() shouldBe ConfigurationName.runtimeOnly
      SourceSetName.main.runtimeOnlyConfig().value shouldBe "runtimeOnly"

      SourceSetName.main.kaptConfig() shouldBe ConfigurationName.kapt
      SourceSetName.main.kaptConfig().value shouldBe "kapt"

      SourceSetName.main.anvilConfig() shouldBe ConfigurationName.anvil
      SourceSetName.main.anvilConfig().value shouldBe "anvil"

      SourceSetName.main.kspConfig() shouldBe ConfigurationName.ksp
      SourceSetName.main.kspConfig().value shouldBe "ksp"
    }

    @Test
    fun `test configs`() {

      SourceSetName.test.apiConfig().value shouldBe "testApi"

      SourceSetName.test.compileOnlyConfig().value shouldBe "testCompileOnly"

      SourceSetName.test.implementationConfig().value shouldBe "testImplementation"

      SourceSetName.test.runtimeOnlyConfig().value shouldBe "testRuntimeOnly"

      SourceSetName.test.kaptConfig().value shouldBe "kaptTest"

      SourceSetName.test.anvilConfig().value shouldBe "anvilTest"

      SourceSetName.test.kspConfig().value shouldBe "kspTest"
    }
  }
}
