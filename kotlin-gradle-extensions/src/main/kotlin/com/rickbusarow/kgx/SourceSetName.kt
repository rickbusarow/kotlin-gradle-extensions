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

import com.rickbusarow.kgx.ConfigurationName.Companion.asConfigurationName
import com.rickbusarow.kgx.stdlib.capitalize
import com.rickbusarow.kgx.stdlib.decapitalize
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
 */
@JvmInline
value class SourceSetName(override val value: String) : DomainObjectName<Any> {

  override fun toString(): String = "(SourceSetName) `$value`"

  companion object {
    /** name of the `androidTest` source set */
    val androidTest: SourceSetName = SourceSetName("androidTest")

    /** name of the `anvil` source set */
    val anvil: SourceSetName = SourceSetName("anvil")

    /** name of the `debug` source set */
    val debug: SourceSetName = SourceSetName("debug")

    /** name of the `kapt` source set */
    val kapt: SourceSetName = SourceSetName("kapt")

    /** name of the `main` source set */
    val main: SourceSetName = SourceSetName("main")

    /** name of the `release` source set */
    val release: SourceSetName = SourceSetName("release")

    /** name of the `test` source set */
    val test: SourceSetName = SourceSetName("test")

    /** name of the `testFixtures` source set */
    val testFixtures: SourceSetName = SourceSetName("testFixtures")

    /** name of the `commonMain` source set */
    val commonMain: SourceSetName = SourceSetName("commonMain")

    /** name of the `commonTest` source set */
    val commonTest: SourceSetName = SourceSetName("commonTest")

    /** name of the `commonJvm` source set */
    val commonJvm: SourceSetName = SourceSetName("commonJvm")

    /** name of the `commonJvmTest` source set */
    val commonJvmTest: SourceSetName = SourceSetName("commonJvmTest")

    /**
     * Delegate for creating a [SourceSetName] from a property name.
     *
     * These two are equivalent:
     * ```
     * val main: SourceSetName by sourceSetName()
     * val main: SourceSetName = SourceSetName("main")
     * ```
     */
    @Suppress("MemberNameEqualsClassName")
    fun sourceSetName(): ReadOnlyProperty<Any?, SourceSetName> {
      return DomainObjectName.lazyName { name -> SourceSetName(name) }
    }

    /** Creates a [SourceSetName] from the receiver string. */
    fun String.asSourceSetName(): SourceSetName = SourceSetName(this)

    /**
     * @return true if this [SourceSetName] is a testFixtures
     *   source set, such as `testFixtures` or `testFixturesDebug`
     */
    fun SourceSetName.isTestFixtures(): Boolean =
      value.startsWith(testFixtures.value, ignoreCase = true)

    /** @return true if this [SourceSetName] is "main" */
    fun SourceSetName.isMain(): Boolean = this == main

    /**
     * Removes [prefix] from the receiver [SourceSetName] and returns
     * the result. The first letter of the result is decapitalized.
     */
    fun SourceSetName.removePrefix(prefix: String): SourceSetName = value.removePrefix(prefix)
      .decapitalize()
      .asSourceSetName()

    /**
     * Removes [prefix] from the receiver [SourceSetName] and returns
     * the result. The first letter of the result is decapitalized.
     */
    fun SourceSetName.removePrefix(prefix: SourceSetName): SourceSetName =
      removePrefix(prefix.value)

    /**
     * Removes [prefix] from the receiver [SourceSetName] and returns
     * the result. The first letter of the result is decapitalized.
     */
    fun SourceSetName.removePrefix(prefix: ConfigurationName): SourceSetName =
      removePrefix(prefix.value)

    /** @return true if the receiver [SourceSetName] starts with [prefix] */
    fun SourceSetName.hasPrefix(prefix: String): Boolean = value.startsWith(prefix)

    /** @return true if the receiver [SourceSetName] starts with [prefix] */
    fun SourceSetName.hasPrefix(prefix: SourceSetName): Boolean = hasPrefix(prefix.value)

    /** @return true if the receiver [SourceSetName] starts with [prefix] */
    fun SourceSetName.hasPrefix(prefix: ConfigurationName): Boolean = hasPrefix(prefix.value)

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the
     * result. The first letter of the original name is capitalized.
     */
    fun SourceSetName.addPrefix(prefix: String): SourceSetName = prefix.plus(value.capitalize())
      .asSourceSetName()

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the
     * result. The first letter of the original name is capitalized.
     */
    fun SourceSetName.addPrefix(prefix: SourceSetName): SourceSetName = addPrefix(prefix.value)

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the
     * result. The first letter of the original name is capitalized.
     */
    fun SourceSetName.addPrefix(prefix: ConfigurationName): SourceSetName = addPrefix(prefix.value)

    /** Removes [suffix] from the receiver [SourceSetName] and returns the result. */
    fun SourceSetName.removeSuffix(suffix: String): SourceSetName =
      value.removeSuffix(suffix.capitalize())
        .asSourceSetName()

    /** Removes [suffix] from the receiver [SourceSetName] and returns the result. */
    fun SourceSetName.removeSuffix(suffix: SourceSetName): SourceSetName =
      removeSuffix(suffix.value.capitalize())

    /** Removes [suffix] from the receiver [SourceSetName] and returns the result. */
    fun SourceSetName.removeSuffix(suffix: ConfigurationName): SourceSetName =
      removeSuffix(suffix.value.capitalize())

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the
     * result. The first letter of the new suffix name is capitalized.
     */
    fun SourceSetName.addSuffix(suffix: String): SourceSetName = value.plus(suffix.capitalize())
      .asSourceSetName()

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the
     * result. The first letter of the new suffix name is capitalized.
     */
    fun SourceSetName.addSuffix(suffix: SourceSetName): SourceSetName = addSuffix(suffix.value)

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the
     * result. The first letter of the new suffix name is capitalized.
     */
    fun SourceSetName.addSuffix(suffix: ConfigurationName): SourceSetName = addSuffix(suffix.value)

    /** @return the 'api' name for this source set, such as `api`, `debugApi`, or `commonMainApi` */
    fun SourceSetName.apiConfig(): ConfigurationName {
      return if (isMain()) {
        ConfigurationName.api
      } else {
        "${value}Api".asConfigurationName()
      }
    }

    /** @return the 'api' name for this source set, such as `api`, `debugApi`, or `commonMainApi` */
    fun SourceSetName.compileOnlyConfig(): ConfigurationName {
      return if (isMain()) {
        ConfigurationName.api
      } else {
        "compileOnly${value.capitalize()}".asConfigurationName()
      }
    }

    /**
     * @return the 'implementation' name for this source set, such as
     *   `implementation`, `debugImplementation`, or `commonMainImplementation`
     */
    fun SourceSetName.implementationConfig(): ConfigurationName {
      return if (isMain()) {
        ConfigurationName.implementation
      } else {
        "${value}Implementation".asConfigurationName()
      }
    }

    /**
     * @return the 'runtimeOnly' name for this source set, such as
     *   `runtimeOnly`, `runtimeOnlyTest`, or `runtimeOnlyAndroidTest`
     */
    fun SourceSetName.runtimeOnlyConfig(): ConfigurationName {
      return if (isMain()) {
        ConfigurationName.api
      } else {
        "runtimeOnly${value.capitalize()}".asConfigurationName()
      }
    }

    /**
     * @return the 'kapt' name for this source set, such as `kapt`, `kaptTest`, or `kaptAndroidTest`
     */
    fun SourceSetName.kaptVariant(): ConfigurationName {
      return if (isMain()) {
        ConfigurationName.kapt
      } else {
        "kapt${value.capitalize()}".asConfigurationName()
      }
    }
  }
}
