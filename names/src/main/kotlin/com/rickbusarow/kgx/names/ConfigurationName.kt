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

import com.rickbusarow.kgx.names.SourceSetName.Companion.apiConfig
import com.rickbusarow.kgx.names.SourceSetName.Companion.asSourceSetName
import com.rickbusarow.kgx.names.SourceSetName.Companion.implementationConfig
import com.rickbusarow.kgx.names.SourceSetName.Companion.kaptConfig
import com.rickbusarow.kgx.names.stdlib.capitalize
import com.rickbusarow.kgx.names.stdlib.decapitalize
import org.gradle.api.artifacts.Configuration
import kotlin.properties.ReadOnlyProperty

/**
 * Wraps the unqualified, simple name of a Gradle
 * Configuration, like `implementation` or `debugApi`.
 *
 * @property value the name
 * @since 0.1.6
 */
@JvmInline
value class ConfigurationName(
  override val value: String
) : DomainObjectName<Configuration>,
  Comparable<ConfigurationName> {

  /**
   * Strips the "base Configuration name" (`api`, `implementation`, `compileOnly`,
   * `runtimeOnly`) from an aggregate name like `debugImplementation`.
   *
   * examples:
   *
   * ```
   * Config                           SourceSet
   * | api                            | main
   * | compileOnlyApi                 | main
   * | implementation                 | main
   * | debugImplementation            | debug
   * | testImplementation             | test
   * | internalReleaseImplementation  | internalRelease
   * ```
   *
   * @return the name of the source set used with this configuration, wrapped in [SourceSetName]
   * @since 0.1.6
   */
  fun toSourceSetName(): SourceSetName = when (this.value) {
    // "main" source set configurations omit the "main" from their name,
    // creating "implementation" instead of "mainImplementation"
    in mainConfigurations -> SourceSetName.main
    // all other configurations (like "test", "debug", or "androidTest")
    // are just "$sourceSetName${baseConfigurationName.capitalize()}"
    else -> this.value.extractSourceSetName()
  }

  /**
   * Returns the base name of the Configuration without any source set prefix.
   *
   * For "main" source sets, this function just returns the same string,
   * e.g.: ConfigurationName("api").nameWithoutSourceSet() == "api"
   * ConfigurationName("implementation").nameWithoutSourceSet() == "implementation"
   *
   * For other source sets, it returns the base configuration names:
   * ConfigurationName("debugApi").nameWithoutSourceSet() == "Api"
   * ConfigurationName("testImplementation").nameWithoutSourceSet() == "Implementation"
   *
   * @since 0.1.6
   */
  fun nameWithoutSourceSet(): String {
    return when {
      isKapt() -> ConfigurationName.kapt.value
      else -> value.removePrefix(toSourceSetName().value)
    }
  }

  /**
   * Returns the base name of the Configuration without any source set prefix.
   *
   * For "main" source sets, this function just returns the same string,
   * e.g.: ConfigurationName("api").nameWithoutSourceSet() == "api"
   * ConfigurationName("implementation").nameWithoutSourceSet() == "implementation"
   *
   * For other source sets, it returns the base configuration names:
   * ConfigurationName("debugApi").nameWithoutSourceSet() == "Api"
   * ConfigurationName("testImplementation").nameWithoutSourceSet() == "Implementation"
   *
   * @since 0.1.6
   */
  fun switchSourceSet(newSourceSetName: SourceSetName): ConfigurationName {

    return when {
      isKapt() -> ConfigurationName(
        "${nameWithoutSourceSet()}${newSourceSetName.value.capitalize()}"
      )

      else -> ConfigurationName(
        "${newSourceSetName.value}${nameWithoutSourceSet().capitalize()}"
      )
    }
  }

  /**
   * find the "base" configuration name and remove it
   *
   * For instance, `debugCompileOnly` would find the "CompileOnly"
   * and remove it, returning "debug" as the sourceSet name
   *
   * @since 0.1.6
   */
  private fun String.extractSourceSetName(): SourceSetName {
    // All kapt configurations start with `kapt`
    //
    //  Config             SourceSet
    //  | kaptAndroidTest  | androidTest
    //  | kaptTest         | test
    //  | kapt             | main
    //  etc.
    if (this.startsWith(kapt.value)) {
      return removePrefix(kapt.value)
        .decapitalize()
        .asSourceSetName()
    }

    // All the base JVM configurations omit "main" from their configuration name
    //
    //  Config             SourceSet
    //  | api              | main
    //  | compileOnlyApi   | main
    //  | implementation   | main
    //  etc.
    val configType = mainConfigurationsCapitalized
      .find { this.endsWith(it) }
      ?: return asSourceSetName()

    // For any other configuration, the formula is $sourceSetName${baseConfigurationName.capitalize()}
    //
    //  Config                SourceSet
    //  | debugApi            | debug
    //  | releaseCompileOnly  | release
    //  | testImplementation  | test
    //  etc.
    return removeSuffix(configType)
      .decapitalize()
      .asSourceSetName()
  }

  /**
   * Returns the '-api' version of the current configuration.
   *
   * In Returns | api | api | implementation | api | compileOnly | api | testImplementation
   * | testApi | debug | debugApi | androidTestImplementation | androidTestApi
   *
   * @return for any main/common configuration, just returns `api`. For any
   *   other configuration, it returns the [SourceSetName] appended with `Api`.
   * @since 0.1.6
   */
  fun apiVariant(): ConfigurationName = toSourceSetName().apiConfig()

  /**
   * Returns the '-implementation' version of the current configuration.
   *
   * In Returns | implementation | implementation | implementation | implementation
   * | compileOnly | implementation | testImplementation | testImplementation | debug
   * | debugImplementation | androidTestImplementation | androidTestImplementation
   *
   * @return for any main/common configuration, just returns `implementation`. For any
   *   other configuration, it returns the [SourceSetName] appended with `Implementation`.
   * @since 0.1.6
   */
  fun implementationVariant(): ConfigurationName = toSourceSetName().implementationConfig()

  /**
   * Returns the 'kapt-' version of the current configuration.
   *
   * @return for any main/common configuration, just returns `kapt`. For any
   *   other configuration, it returns `kapt` appended with the [SourceSetName].
   * @since 0.1.6
   */
  fun kaptVariant(): ConfigurationName = toSourceSetName().kaptConfig()

  /**
   * @return true if the configuration is an `api` variant
   * @since 0.1.6
   */
  fun isApi(): Boolean = this == apiVariant()

  /**
   * @return true if the configuration is an `implementation` variant
   * @since 0.1.6
   */
  fun isImplementation(): Boolean = this == implementationVariant()

  /**
   * @return true if the configuration is a `kapt` variant
   * @since 0.1.6
   */
  fun isKapt(): Boolean = this == kaptVariant()

  override fun compareTo(other: ConfigurationName): Int = value.compareTo(other.value)

  override fun toString(): String = "(ConfigurationName) `$value`"

  companion object {

    /**
     * name of the 'androidTestImplementation' configuration
     *
     * @since 0.1.6
     */
    val androidTestImplementation: ConfigurationName =
      ConfigurationName("androidTestImplementation")

    /**
     * name of the 'annotationProcessor' configuration
     *
     * @since 0.1.6
     */
    val annotationProcessor: ConfigurationName = ConfigurationName("annotationProcessor")

    /**
     * name of the 'anvil' configuration
     *
     * @since 0.1.6
     */
    val anvil: ConfigurationName = ConfigurationName("anvil")

    /**
     * name of the 'api' configuration
     *
     * @since 0.1.6
     */
    val api: ConfigurationName = ConfigurationName("api")

    /**
     * name of the 'compile' configuration
     *
     * @since 0.1.6
     */
    val compile: ConfigurationName = ConfigurationName("compile")

    /**
     * name of the 'compileOnly' configuration
     *
     * @since 0.1.6
     */
    val compileOnly: ConfigurationName = ConfigurationName("compileOnly")

    /**
     * name of the 'compileOnlyApi' configuration
     *
     * @since 0.1.6
     */
    val compileOnlyApi: ConfigurationName = ConfigurationName("compileOnlyApi")

    /**
     * name of the 'implementation' configuration
     *
     * @since 0.1.6
     */
    val implementation: ConfigurationName = ConfigurationName("implementation")

    /**
     * name of the 'kapt' configuration
     *
     * @since 0.1.6
     */
    val kapt: ConfigurationName = ConfigurationName("kapt")

    /**
     * name of the 'kotlinCompilerPluginClasspathMain' configuration
     *
     * @since 0.1.6
     */
    val kotlinCompileClasspath: ConfigurationName =
      ConfigurationName("kotlinCompilerPluginClasspathMain")

    /**
     * name of the 'ksp' configuration
     *
     * @since 0.1.6
     */
    val ksp: ConfigurationName = ConfigurationName("ksp")

    /**
     * name of the 'runtime' configuration
     *
     * @since 0.1.6
     */
    val runtime: ConfigurationName = ConfigurationName("runtime")

    /**
     * name of the 'runtimeOnly' configuration
     *
     * @since 0.1.6
     */
    val runtimeOnly: ConfigurationName = ConfigurationName("runtimeOnly")

    /**
     * name of the 'shadow' configuration
     *
     * @since 0.1.7
     */
    val shadow: ConfigurationName = ConfigurationName("shadow")

    /**
     * name of the 'testApi' configuration
     *
     * @since 0.1.6
     */
    val testApi: ConfigurationName = ConfigurationName("testApi")

    /**
     * name of the 'testImplementation' configuration
     *
     * @since 0.1.6
     */
    val testImplementation: ConfigurationName = ConfigurationName("testImplementation")

    /**
     * the names of all configurations consumed by the main source set
     *
     * @since 0.1.6
     */
    val mainConfigurations: List<String> = listOf(
      api.value,
      compile.value,
      compileOnly.value,
      compileOnlyApi.value,
      implementation.value,
      kapt.value,
      // kotlinCompilerPluginClasspath is a special case,
      // since the main config is suffixed with "Main"
      kotlinCompileClasspath.value,
      runtime.value,
      runtimeOnly.value,
      shadow.value
    )
      // The order of this list matters. CompileOnlyApi must be before `api` or
      // `extractSourceSetName` below will match the wrong suffix.
      .sortedByDescending { it.length }

    internal val mainCommonConfigurations: List<String> = listOf(
      api.value,
      implementation.value
    )

    private val mainConfigurationsCapitalized: Set<String> = mainConfigurations
      .map { it.capitalize() }
      .toSet()

    /**
     * the names of all configurations consumed by the main source set
     *
     * @since 0.1.6
     */
    fun main(): List<ConfigurationName> = listOf(
      compileOnlyApi,
      api,
      implementation,
      compileOnly,
      compile,
      kapt,
      runtimeOnly,
      runtime
    )

    /**
     * the base configurations which do not leak their transitive dependencies (basically not `api`)
     *
     * @since 0.1.6
     */
    fun private(): List<ConfigurationName> = listOf(
      implementation,
      compileOnly,
      compile,
      runtimeOnly,
      runtime
    )

    /**
     * the base configurations which include their dependencies as "compile" dependencies in the POM
     *
     * @since 0.1.6
     */
    fun public(): List<ConfigurationName> = listOf(
      compileOnlyApi,
      api
    )

    /**
     * Delegate for creating a [ConfigurationName] from a property name.
     *
     * These two are equivalent:
     * ```
     * val api: ConfigurationName by configurationName()
     * val api: ConfigurationName = ConfigurationName("api")
     * ```
     *
     * @since 0.1.6
     */
    @Suppress("MemberNameEqualsClassName")
    fun configurationName(): ReadOnlyProperty<Any?, ConfigurationName> {
      return DomainObjectName.lazyName { name -> ConfigurationName(name) }
    }

    /**
     * @return a ConfigurationName from this raw string
     * @since 0.1.6
     */
    fun String.asConfigurationName(): ConfigurationName = ConfigurationName(this)
  }
}
