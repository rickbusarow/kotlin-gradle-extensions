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

import com.rickbusarow.kgx.names.ConfigurationName.Companion.asConfigurationName
import com.rickbusarow.kgx.names.TaskName.Companion.asTaskName
import com.rickbusarow.kgx.names.stdlib.capitalize
import com.rickbusarow.kgx.names.stdlib.decapitalize
import kotlin.properties.ReadOnlyProperty

/**
 * Wraps the unqualified, simple name of a `SourceSet` object, like `main` or `debug`.
 *
 * Note that the bound on this name is `Any`, not `SourceSet`, because
 * the java [SourceSet][org.gradle.api.tasks.SourceSet] and kotlin
 * [KotlinSourceSet][org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet]
 * classes do not share a common interface.
 *
 * @property value the name
 * @since 0.1.6
 */
@JvmInline
value class SourceSetName(
  override val value: String
) : DomainObjectName<Any> {
  override fun toString(): String = "(SourceSetName) `$value`"

  companion object {
    /**
     * name of the `androidTest` source set
     *
     * @since 0.1.6
     */
    val androidTest: SourceSetName = SourceSetName("androidTest")

    /**
     * name of the `debug` source set
     *
     * @since 0.1.6
     */
    val debug: SourceSetName = SourceSetName("debug")

    /**
     * name of the `main` source set
     *
     * @since 0.1.6
     */
    val main: SourceSetName = SourceSetName("main")

    /**
     * name of the `release` source set
     *
     * @since 0.1.6
     */
    val release: SourceSetName = SourceSetName("release")

    /**
     * name of the `test` source set
     *
     * @since 0.1.6
     */
    val test: SourceSetName = SourceSetName("test")

    /**
     * name of the `testFixtures` source set
     *
     * @since 0.1.6
     */
    val testFixtures: SourceSetName = SourceSetName("testFixtures")

    /**
     * name of the `commonMain` source set
     *
     * @since 0.1.6
     */
    val commonMain: SourceSetName = SourceSetName("commonMain")

    /**
     * name of the `commonTest` source set
     *
     * @since 0.1.6
     */
    val commonTest: SourceSetName = SourceSetName("commonTest")

    /**
     * name of the `commonJvm` source set
     *
     * @since 0.1.6
     */
    val commonJvm: SourceSetName = SourceSetName("commonJvm")

    /**
     * name of the `commonJvmTest` source set
     *
     * @since 0.1.6
     */
    val commonJvmTest: SourceSetName = SourceSetName("commonJvmTest")

    /**
     * Delegate for creating a [SourceSetName] from a property name.
     *
     * These two are equivalent:
     * ```
     * val main: SourceSetName by sourceSetName()
     * val main: SourceSetName = SourceSetName("main")
     * ```
     *
     * @since 0.1.6
     */
    @Suppress("MemberNameEqualsClassName")
    fun sourceSetName(): ReadOnlyProperty<Any?, SourceSetName> {
      return DomainObjectName.lazyName { name -> SourceSetName(name) }
    }

    /**
     * Creates a [SourceSetName] from the receiver string.
     *
     * @since 0.1.6
     */
    fun String.asSourceSetName(): SourceSetName = SourceSetName(this)

    /**
     * @return true if this [SourceSetName] is a testFixtures
     *   source set, such as `testFixtures` or `testFixturesDebug`
     * @since 0.1.6
     */
    fun SourceSetName.isTestFixtures(): Boolean =
      value.startsWith(testFixtures.value, ignoreCase = true)

    /**
     * @return true if this [SourceSetName] is "main"
     * @since 0.1.6
     */
    fun SourceSetName.isMain(): Boolean = this == main

    /**
     * Removes [prefix] from the receiver [SourceSetName] and returns
     * the result. The first letter of the result is decapitalized.
     *
     * @since 0.1.6
     */
    fun SourceSetName.removePrefix(prefix: String): SourceSetName =
      value
        .removePrefix(prefix)
        .decapitalize()
        .asSourceSetName()

    /**
     * Removes [prefix] from the receiver [SourceSetName] and returns
     * the result. The first letter of the result is decapitalized.
     *
     * @since 0.1.6
     */
    fun SourceSetName.removePrefix(prefix: DomainObjectName<*>): SourceSetName =
      removePrefix(prefix.value)

    /**
     * @return true if the receiver [SourceSetName] starts with [prefix]
     * @since 0.1.6
     */
    fun SourceSetName.hasPrefix(prefix: String): Boolean = value.startsWith(prefix)

    /**
     * @return true if the receiver [SourceSetName] starts with [prefix]
     * @since 0.1.6
     */
    fun SourceSetName.hasPrefix(prefix: DomainObjectName<*>): Boolean = hasPrefix(prefix.value)

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the
     * result. The first letter of the original name is capitalized.
     *
     * @since 0.1.6
     */
    fun SourceSetName.addPrefix(prefix: String): String = prefix.plus(value.capitalize())

    /**
     * Removes [suffix] from the receiver [SourceSetName] and returns the result.
     *
     * @since 0.1.6
     */
    fun SourceSetName.removeSuffix(suffix: String): SourceSetName =
      value
        .removeSuffix(suffix.capitalize())
        .asSourceSetName()

    /**
     * Removes [suffix] from the receiver [SourceSetName] and returns the result.
     *
     * @since 0.1.6
     */
    fun SourceSetName.removeSuffix(suffix: DomainObjectName<*>): SourceSetName =
      removeSuffix(suffix.value.capitalize())

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the
     * result. The first letter of the new suffix name is capitalized.
     *
     * @since 0.1.6
     */
    fun SourceSetName.addSuffix(suffix: String): String = value.plus(suffix.capitalize())

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the result as a
     * [ConfigurationName]. The first letter of the original name is capitalized.
     *
     * @since 0.1.6
     */
    fun SourceSetName.addPrefix(prefix: ConfigurationName): ConfigurationName =
      prefix.value.plus(value.capitalize()).asConfigurationName()

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the result as a
     * [ConfigurationName]. The first letter of the new suffix name is capitalized.
     *
     * @since 0.1.6
     */
    fun SourceSetName.addSuffix(suffix: ConfigurationName): ConfigurationName =
      value.plus(suffix.value.capitalize()).asConfigurationName()

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the result
     * as a [TaskName]. The first letter of the original name is capitalized.
     *
     * @since 0.1.6
     */
    fun SourceSetName.addPrefix(prefix: TaskName): TaskName =
      prefix.value.plus(value.capitalize()).asTaskName()

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the result as
     * a [TaskName]. The first letter of the new suffix name is capitalized.
     *
     * @since 0.1.6
     */
    fun SourceSetName.addSuffix(suffix: TaskName): TaskName =
      value.plus(suffix.value.capitalize()).asTaskName()

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the result as
     * a [SourceSetName]. The first letter of the original name is capitalized.
     *
     * @since 0.1.6
     */
    fun SourceSetName.addPrefix(prefix: SourceSetName): SourceSetName =
      prefix.value.plus(value.capitalize()).asSourceSetName()

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the result as a
     * [SourceSetName]. The first letter of the new suffix name is capitalized.
     *
     * @since 0.1.6
     */
    fun SourceSetName.addSuffix(suffix: SourceSetName): SourceSetName =
      value.plus(suffix.value.capitalize()).asSourceSetName()

    /**
     * @return the 'api' name for this source set, such as `api`, `debugApi`, or `commonMainApi`
     * @since 0.1.6
     */
    fun SourceSetName.apiConfig(): ConfigurationName {
      return if (isMain()) {
        ConfigurationName.api
      } else {
        "${value}Api".asConfigurationName()
      }
    }

    /**
     * @return the 'api' name for this source set, such as
     *   `compileOnly`,`compileOnlyDebug`, or `compileOnlyTest`
     * @since 0.1.6
     */
    fun SourceSetName.compileOnlyConfig(): ConfigurationName =
      if (isMain()) {
        ConfigurationName.compileOnly
      } else {
        addSuffix(ConfigurationName.compileOnly)
      }

    /**
     * @return the 'implementation' name for this source set, such as
     *   `implementation`, `debugImplementation`, or `commonMainImplementation`
     * @since 0.1.6
     */
    fun SourceSetName.implementationConfig(): ConfigurationName =
      if (isMain()) {
        ConfigurationName.implementation
      } else {
        addSuffix(ConfigurationName.implementation)
      }

    /**
     * @return the 'runtimeOnly' name for this source set, such as
     *   `runtimeOnly`, `runtimeOnlyTest`, or `runtimeOnlyAndroidTest`
     * @since 0.1.6
     */
    fun SourceSetName.runtimeOnlyConfig(): ConfigurationName =
      if (isMain()) {
        ConfigurationName.runtimeOnly
      } else {
        addSuffix(ConfigurationName.runtimeOnly)
      }

    /**
     * @return the 'kapt' name for this source set, such as `kapt`, `kaptTest`, or `kaptAndroidTest`
     * @since 0.1.6
     */
    fun SourceSetName.kaptConfig(): ConfigurationName =
      if (isMain()) {
        ConfigurationName.kapt
      } else {
        addPrefix(ConfigurationName.kapt)
      }

    /**
     * @return the 'anvil' name for this source set, such
     *   as `anvil`, `anvilTest`, or `anvilAndroidTest`
     * @since 0.1.6
     */
    fun SourceSetName.anvilConfig(): ConfigurationName =
      if (isMain()) {
        ConfigurationName.anvil
      } else {
        addPrefix(ConfigurationName.anvil)
      }

    /**
     * @return the 'ksp' name for this source set, such as `ksp`, `kspTest`, or `kspAndroidTest`
     * @since 0.1.6
     */
    fun SourceSetName.kspConfig(): ConfigurationName =
      if (isMain()) {
        ConfigurationName.ksp
      } else {
        addPrefix(ConfigurationName.ksp)
      }
  }
}
