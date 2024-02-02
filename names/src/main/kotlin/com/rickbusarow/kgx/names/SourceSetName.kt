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

package com.rickbusarow.kgx.names

import com.rickbusarow.kgx.names.ConfigurationName.Companion.asConfigurationName
import com.rickbusarow.kgx.names.TaskName.Companion.asTaskName
import com.rickbusarow.kgx.names.stdlib.capitalize
import com.rickbusarow.kgx.names.stdlib.decapitalize
import kotlin.properties.ReadOnlyProperty
import com.rickbusarow.kgx.names.ConfigurationName.Companion.anvilConfig as configAnvilConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.apiConfig as configApiConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.compileOnlyConfig as configCompileOnlyConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.implementationConfig as configImplementationConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.kaptConfig as configKaptConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.kspConfig as configKspConfig
import com.rickbusarow.kgx.names.ConfigurationName.Companion.runtimeOnlyConfig as configRuntimeOnlyConfig

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
public value class SourceSetName(
  override val value: String
) : DomainObjectName<Any> {
  override fun toString(): String = "(SourceSetName) `$value`"

  public companion object {
    /**
     * name of the `androidTest` source set
     *
     * @since 0.1.6
     */
    public val androidTest: SourceSetName = SourceSetName("androidTest")

    /**
     * name of the `debug` source set
     *
     * @since 0.1.6
     */
    public val debug: SourceSetName = SourceSetName("debug")

    /**
     * name of the `main` source set
     *
     * @since 0.1.6
     */
    public val main: SourceSetName = SourceSetName("main")

    /**
     * name of the `release` source set
     *
     * @since 0.1.6
     */
    public val release: SourceSetName = SourceSetName("release")

    /**
     * name of the `test` source set
     *
     * @since 0.1.6
     */
    public val test: SourceSetName = SourceSetName("test")

    /**
     * name of the `testFixtures` source set
     *
     * @since 0.1.6
     */
    public val testFixtures: SourceSetName = SourceSetName("testFixtures")

    /**
     * name of the `commonMain` source set
     *
     * @since 0.1.6
     */
    public val commonMain: SourceSetName = SourceSetName("commonMain")

    /**
     * name of the `commonTest` source set
     *
     * @since 0.1.6
     */
    public val commonTest: SourceSetName = SourceSetName("commonTest")

    /**
     * name of the `commonJvm` source set
     *
     * @since 0.1.6
     */
    public val commonJvm: SourceSetName = SourceSetName("commonJvm")

    /**
     * name of the `commonJvmTest` source set
     *
     * @since 0.1.6
     */
    public val commonJvmTest: SourceSetName = SourceSetName("commonJvmTest")

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
    public fun sourceSetName(): ReadOnlyProperty<Any?, SourceSetName> =
      DomainObjectName.lazyName { name ->
        SourceSetName(name)
      }

    /**
     * Creates a [SourceSetName] from the receiver string.
     *
     * @since 0.1.6
     */
    public fun String.asSourceSetName(): SourceSetName = SourceSetName(this)

    /**
     * @return true if this [SourceSetName] is a testFixtures
     *   source set, such as `testFixtures` or `testFixturesDebug`
     * @since 0.1.6
     */
    public fun SourceSetName.isTestFixtures(): Boolean =
      value.startsWith(testFixtures.value, ignoreCase = true)

    /**
     * @return true if this [SourceSetName] is "main"
     * @since 0.1.6
     */
    public fun SourceSetName.isMain(): Boolean = this == main

    /**
     * Removes [prefix] from the receiver [SourceSetName] and returns
     * the result. The first letter of the result is decapitalized.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.removePrefix(prefix: String): SourceSetName =
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
    public fun SourceSetName.removePrefix(prefix: DomainObjectName<*>): SourceSetName =
      removePrefix(prefix.value)

    /**
     * @return true if the receiver [SourceSetName] starts with [prefix]
     * @since 0.1.6
     */
    public fun SourceSetName.hasPrefix(prefix: String): Boolean = value.startsWith(prefix)

    /**
     * @return true if the receiver [SourceSetName] starts with [prefix]
     * @since 0.1.6
     */
    public fun SourceSetName.hasPrefix(prefix: DomainObjectName<*>): Boolean =
      hasPrefix(prefix.value)

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the
     * result. The first letter of the original name is capitalized.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.addPrefix(prefix: String): String = prefix.plus(value.capitalize())

    /**
     * Removes [suffix] from the receiver [SourceSetName] and returns the result.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.removeSuffix(suffix: String): SourceSetName =
      value
        .removeSuffix(suffix.capitalize())
        .asSourceSetName()

    /**
     * Removes [suffix] from the receiver [SourceSetName] and returns the result.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.removeSuffix(suffix: DomainObjectName<*>): SourceSetName =
      removeSuffix(suffix.value.capitalize())

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the
     * result. The first letter of the new suffix name is capitalized.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.addSuffix(suffix: String): String = value.plus(suffix.capitalize())

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the result as a
     * [ConfigurationName]. The first letter of the original name is capitalized.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.addPrefix(prefix: ConfigurationName): ConfigurationName =
      prefix.value.plus(value.capitalize()).asConfigurationName()

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the result as a
     * [ConfigurationName]. The first letter of the new suffix name is capitalized.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.addSuffix(suffix: ConfigurationName): ConfigurationName =
      value.plus(suffix.value.capitalize()).asConfigurationName()

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the result
     * as a [TaskName]. The first letter of the original name is capitalized.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.addPrefix(prefix: TaskName): TaskName =
      prefix.value.plus(value.capitalize()).asTaskName()

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the result as
     * a [TaskName]. The first letter of the new suffix name is capitalized.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.addSuffix(suffix: TaskName): TaskName =
      value.plus(suffix.value.capitalize()).asTaskName()

    /**
     * Adds [prefix] to the receiver [SourceSetName] and returns the result as
     * a [SourceSetName]. The first letter of the original name is capitalized.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.addPrefix(prefix: SourceSetName): SourceSetName =
      prefix.value.plus(value.capitalize()).asSourceSetName()

    /**
     * Adds [suffix] to the receiver [SourceSetName] and returns the result as a
     * [SourceSetName]. The first letter of the new suffix name is capitalized.
     *
     * @since 0.1.6
     */
    public fun SourceSetName.addSuffix(suffix: SourceSetName): SourceSetName =
      value.plus(suffix.value.capitalize()).asSourceSetName()

    /**
     * @return the 'api' name for this source set, such as `api`, `debugApi`, or `commonMainApi`
     * @since 0.1.6
     */
    @Deprecated(
      "moved to ConfigurationName",
      ReplaceWith("apiConfig()", "com.rickbusarow.kgx.names.ConfigurationName.Companion.apiConfig")
    )
    public fun SourceSetName.apiConfig(): ConfigurationName = configApiConfig()

    /**
     * @return the 'api' name for this source set, such as
     *   `compileOnly`,`compileOnlyDebug`, or `compileOnlyTest`
     * @since 0.1.6
     */
    @Deprecated(
      "moved to ConfigurationName",
      ReplaceWith(
        "compileOnlyConfig()",
        "com.rickbusarow.kgx.names.ConfigurationName.Companion.compileOnlyConfig"
      )
    )
    public fun SourceSetName.compileOnlyConfig(): ConfigurationName = configCompileOnlyConfig()

    /**
     * @return the 'implementation' name for this source set, such as
     *   `implementation`, `debugImplementation`, or `commonMainImplementation`
     * @since 0.1.6
     */
    @Deprecated(
      "moved to ConfigurationName",
      ReplaceWith(
        "implementationConfig()",
        "com.rickbusarow.kgx.names.ConfigurationName.Companion.implementationConfig"
      )
    )
    public fun SourceSetName.implementationConfig(): ConfigurationName =
      configImplementationConfig()

    /**
     * @return the 'runtimeOnly' name for this source set, such as
     *   `runtimeOnly`, `runtimeOnlyTest`, or `runtimeOnlyAndroidTest`
     * @since 0.1.6
     */
    @Deprecated(
      "moved to ConfigurationName",
      ReplaceWith(
        "runtimeOnlyConfig()",
        "com.rickbusarow.kgx.names.ConfigurationName.Companion.runtimeOnlyConfig"
      )
    )
    public fun SourceSetName.runtimeOnlyConfig(): ConfigurationName = configRuntimeOnlyConfig()

    /**
     * @return the 'kapt' name for this source set, such as `kapt`, `kaptTest`, or `kaptAndroidTest`
     * @since 0.1.6
     */
    @Deprecated(
      "moved to ConfigurationName",
      ReplaceWith(
        "kaptConfig()",
        "com.rickbusarow.kgx.names.ConfigurationName.Companion.kaptConfig"
      )
    )
    public fun SourceSetName.kaptConfig(): ConfigurationName = configKaptConfig()

    /**
     * @return the 'anvil' name for this source set, such
     *   as `anvil`, `anvilTest`, or `anvilAndroidTest`
     * @since 0.1.6
     */
    @Deprecated(
      "moved to ConfigurationName",
      ReplaceWith(
        "anvilConfig()",
        "com.rickbusarow.kgx.names.ConfigurationName.Companion.anvilConfig"
      )
    )
    public fun SourceSetName.anvilConfig(): ConfigurationName = configAnvilConfig()

    /**
     * @return the 'ksp' name for this source set, such as `ksp`, `kspTest`, or `kspAndroidTest`
     * @since 0.1.6
     */
    @Deprecated(
      "moved to ConfigurationName",
      ReplaceWith("kspConfig()", "com.rickbusarow.kgx.names.ConfigurationName.Companion.kspConfig")
    )
    public fun SourceSetName.kspConfig(): ConfigurationName = configKspConfig()
  }
}
