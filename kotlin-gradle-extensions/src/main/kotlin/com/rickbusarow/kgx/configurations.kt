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

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer

/**
 * Shorthand for `configurations.getByName("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.api: Configuration
  get() = getByName(ConfigurationName.api)

/**
 * Shorthand for `configurations.findByName("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.apiOrNull: Configuration?
  get() = findByName(ConfigurationName.api)

/**
 * Shorthand for `configurations.named("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.apiProvider: NamedDomainObjectProvider<Configuration>
  get() = named(ConfigurationName.api)

/**
 * Shorthand for `configurations.named("api") { ... }`.
 *
 * @since 0.1.6
 */
fun ConfigurationContainer.api(
  configurationAction: Action<Configuration>
): NamedDomainObjectProvider<Configuration>? = named(ConfigurationName.api, configurationAction)

/**
 * Shorthand for `configurations.getByName("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.compileOnly: Configuration
  get() = getByName(ConfigurationName.compileOnly)

/**
 * Shorthand for `configurations.findByName("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.compileOnlyOrNull: Configuration?
  get() = findByName(ConfigurationName.compileOnly)

/**
 * Shorthand for `configurations.named("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.compileOnlyProvider: NamedDomainObjectProvider<Configuration>
  get() = named(ConfigurationName.compileOnly)

/**
 * Shorthand for `configurations.named("api") { ... }`.
 *
 * @since 0.1.6
 */
fun ConfigurationContainer.compileOnly(
  configurationAction: Action<Configuration>
): NamedDomainObjectProvider<Configuration>? = named(
  ConfigurationName.compileOnly,
  configurationAction
)

/**
 * Shorthand for `configurations.getByName("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.implementation: Configuration
  get() = getByName(ConfigurationName.implementation)

/**
 * Shorthand for `configurations.findByName("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.implementationOrNull: Configuration?
  get() = findByName(ConfigurationName.implementation)

/**
 * Shorthand for `configurations.named("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.implementationProvider: NamedDomainObjectProvider<Configuration>
  get() = named(ConfigurationName.implementation)

/**
 * Shorthand for `configurations.named("api") { ... }`.
 *
 * @since 0.1.6
 */
fun ConfigurationContainer.implementation(
  configurationAction: Action<Configuration>
): NamedDomainObjectProvider<Configuration>? = named(
  ConfigurationName.implementation,
  configurationAction
)

/**
 * Shorthand for `configurations.getByName("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.kapt: Configuration
  get() = getByName(ConfigurationName.kapt)

/**
 * Shorthand for `configurations.findByName("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.kaptOrNull: Configuration?
  get() = findByName(ConfigurationName.kapt)

/**
 * Shorthand for `configurations.named("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.kaptProvider: NamedDomainObjectProvider<Configuration>
  get() = named(ConfigurationName.kapt)

/**
 * Shorthand for `configurations.named("api") { ... }`.
 *
 * @since 0.1.6
 */
fun ConfigurationContainer.kapt(
  configurationAction: Action<Configuration>
): NamedDomainObjectProvider<Configuration>? = named(ConfigurationName.kapt, configurationAction)

/**
 * Shorthand for `configurations.getByName("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.runtimeOnly: Configuration
  get() = getByName(ConfigurationName.runtimeOnly)

/**
 * Shorthand for `configurations.findByName("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.runtimeOnlyOrNull: Configuration?
  get() = findByName(ConfigurationName.runtimeOnly)

/**
 * Shorthand for `configurations.named("api")`.
 *
 * @since 0.1.6
 */
val ConfigurationContainer.runtimeOnlyProvider: NamedDomainObjectProvider<Configuration>
  get() = named(ConfigurationName.runtimeOnly)

/**
 * Shorthand for `configurations.named("api") { ... }`.
 *
 * @since 0.1.6
 */
fun ConfigurationContainer.runtimeOnly(
  configurationAction: Action<Configuration>
): NamedDomainObjectProvider<Configuration>? = named(
  ConfigurationName.runtimeOnly,
  configurationAction
)
