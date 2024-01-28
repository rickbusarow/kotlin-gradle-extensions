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

import com.rickbusarow.kase.AbstractKase2
import com.rickbusarow.kase.Kase2
import com.rickbusarow.kase.gradle.GradleTestVersions
import com.rickbusarow.kase.gradle.TestVersions
import com.rickbusarow.kase.gradle.versions
import com.rickbusarow.kgx.BuildConfig.version
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.TestFactory

class GradlePluginCallbackTest : KgxGradleTest() {

  override val testEnvironmentFactory = KgxGradleTestEnvironment.Factory()

  override val params: List<TestVersions>
    get() = kaseMatrix.versions(GradleTestVersions)

  val gradlePluginKases: List<Kase2<String, String>> = listOf(
    pair("application", "withApplicationPlugin"),
    pair("base", "withBasePlugin"),
    pair("`build-init`", "withBuildInitPlugin"),
    pair("distribution", "withDistributionPlugin"),
    pair("groovy", "withGroovyPlugin"),
    pair("`groovy-base`", "withGroovyBasePlugin"),
    pair("`groovy-gradle-plugin`", "withPrecompiledGroovyPluginsPlugin"),
    pair("idea", "withIdeaPlugin"),
    pair("java", "withJavaPlugin"),
    pair("`java-base`", "withJavaBasePlugin"),
    pair("`java-gradle-plugin`", "withJavaGradlePluginPlugin"),
    pair("`java-library`", "withJavaLibraryPlugin"),
    pair("`java-library-distribution`", "withJavaLibraryDistributionPlugin"),
    pair("`java-platform`", "withJavaPlatformPlugin"),
    pair("`java-test-fixtures`", "withJavaTestFixturesPlugin")
  )

  @TestFactory
  fun `core gradle plugins`() = gradlePluginKases.asContainers { (pluginId, name) ->
    testFactory {

      rootProject {
        buildFile(
          //language=kotlin
          """
          import com.rickbusarow.kgx.$name

          buildscript {
            dependencies {
              classpath("com.rickbusarow.kgx:kotlin-gradle-extensions:$version")
            }
          }

          plugins {
            $pluginId
          }

          plugins.$name { println("plugins callback invoked") }
          pluginManager.$name { println("pluginManager callback invoked") }
          """.trimIndent()
        )
      }

      shouldSucceed("tasks") {
        output shouldContain "plugins callback invoked"
        output shouldContain "pluginManager callback invoked"
      }
    }
  }

  fun pair(pluginId: String, functionName: String): PluginIdFunctionPair {
    return PluginIdFunctionPair(pluginId = pluginId, functionName = functionName)
  }

  data class PluginIdFunctionPair(
    val pluginId: String,
    val functionName: String
  ) : AbstractKase2<String, String>(
    a1 = pluginId,
    a2 = functionName,
    displayNameFactory = { "id: $pluginId | functionName: $functionName" }
  )
}
