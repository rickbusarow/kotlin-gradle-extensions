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

import com.rickbusarow.kase.Kase2
import com.rickbusarow.kase.files.HasWorkingDir
import com.rickbusarow.kase.files.TestFunctionCoordinates
import com.rickbusarow.kase.gradle.DslLanguage.KotlinDsl
import com.rickbusarow.kase.gradle.GradleTestEnvironment
import com.rickbusarow.kase.gradle.GradleTestVersions
import com.rickbusarow.kase.gradle.TestVersions
import com.rickbusarow.kase.kase
import com.rickbusarow.kgx.BuildConfig.version
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.TestFactory

class GradlePluginCallbackTest : KgxGradleTest<TestVersions>() {

  override val kases: List<TestVersions>
    get() = versionMatrix.versions(GradleTestVersions)

  val gradlePluginKases: List<Kase2<String, String>> = listOf(
    kase(a1 = "application", "withApplicationPlugin"),
    kase(a1 = "base", "withBasePlugin"),
    kase(a1 = "`build-init`", "withBuildInitPlugin"),
    kase(a1 = "distribution", "withDistributionPlugin"),
    kase(a1 = "groovy", "withGroovyPlugin"),
    kase(a1 = "`groovy-base`", "withGroovyBasePlugin"),
    kase(a1 = "`groovy-gradle-plugin`", "withPrecompiledGroovyPluginsPlugin"),
    kase(a1 = "idea", "withIdeaPlugin"),
    kase(a1 = "java", "withJavaPlugin"),
    kase(a1 = "`java-base`", "withJavaBasePlugin"),
    kase(a1 = "`java-gradle-plugin`", "withJavaGradlePluginPlugin"),
    kase(a1 = "`java-library`", "withJavaLibraryPlugin"),
    kase(a1 = "`java-library-distribution`", "withJavaLibraryDistributionPlugin"),
    kase(a1 = "`java-platform`", "withJavaPlatformPlugin"),
    kase(a1 = "`java-test-fixtures`", "withJavaTestFixturesPlugin")
  )

  fun testEnvironmentFactory(
    versions: TestVersions,
    testFunctionCoordinates: TestFunctionCoordinates
  ): (Kase2<String, String>) -> GradleTestEnvironment = { kase ->
    GradleTestEnvironment(
      testVersions = versions,
      dslLanguage = KotlinDsl(),
      hasWorkingDir = HasWorkingDir(
        listOf(versions.displayName, kase.displayName),
        testFunctionCoordinates
      ),
      defaultBuildFile = buildFileDefault(versions),
      defaultSettingsFile = settingsFileDefault(versions)
    )
  }

  @TestFactory
  fun `core gradle plugins`() = kases.asContainers { versions ->
    gradlePluginKases.asTests(
      testEnvironmentFactory = testEnvironmentFactory(versions, testFunctionCoordinates)
    ) { (pluginId, name) ->

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
}
