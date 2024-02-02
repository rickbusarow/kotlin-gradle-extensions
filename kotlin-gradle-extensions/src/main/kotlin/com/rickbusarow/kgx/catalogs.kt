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

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.properties.ReadOnlyProperty

/**
 * shorthand for `extensions.getByType(VersionCatalogsExtension::class.java)`
 *
 * @since 0.1.1
 */
public val Project.catalogs: VersionCatalogsExtension
  get() = extensions.getByType(VersionCatalogsExtension::class.java)

/**
 * non-dsl version of `libs`
 *
 * ex:
 * ```
 * val myCatalog = project.libsCatalog
 * ```
 *
 * @since 0.1.0
 */
public val Project.libsCatalog: VersionCatalog
  get() = catalogs.named("libs")

/**
 * non-dsl version of `libs._____`
 *
 * ex:
 *
 * ```
 * "api"(project.libsCatalog.dependency("square-anvil-annotations"))
 * ```
 *
 * @since 0.1.0
 */
@Deprecated("renamed to `library`", ReplaceWith("library(alias)"))
public fun VersionCatalog.dependency(alias: String): Provider<MinimalExternalModuleDependency> {
  return library(alias)
}

/**
 * non-dsl version of `libs._____`
 *
 * ex:
 *
 * ```
 * "api"(project.libsCatalog.dependency("square-anvil-annotations"))
 * ```
 *
 * @since 0.1.0
 */
public fun VersionCatalog.library(alias: String): Provider<MinimalExternalModuleDependency> {
  return findLibrary(alias)
    .orElseThrow {
      GradleException("No dependency was found in the catalog for the alias '$alias'.")
    }
}

/**
 * A property delegate for accessing a library from the
 * receiver catalog, using the property name as the alias.
 *
 * ex:
 * ```
 * val `anvil-compiler` by project.libsCatalog.library
 * ```
 *
 * @since 0.1.9
 */
public val VersionCatalog.library: ReadOnlyProperty<Any?, Provider<MinimalExternalModuleDependency>>
  get() = ReadOnlyProperty { _, prop -> library(prop.name) }

/**
 * non-dsl version of `libs.versions._____.get()`
 *
 * ex:
 *
 * ```
 * val anvilVersion = project.libsCatalog.version("square-anvil")
 * ```
 *
 * @since 0.1.0
 */
public fun VersionCatalog.version(alias: String): String {
  return findVersion(alias)
    .orElseThrow {
      GradleException("No version was found in the catalog for the alias '$alias'.")
    }.requiredVersion
}

/**
 * A property delegate for accessing a version from the
 * receiver catalog, using the property name as the alias.
 *
 * ex:
 * ```
 * val anvil by project.libsCatalog.version
 * ```
 *
 * @since 0.1.9
 */
public val VersionCatalog.version: ReadOnlyProperty<Any?, String>
  get() = ReadOnlyProperty { _, prop -> version(prop.name) }

/**
 * non-dsl version of `libs.versions._____.get().pluginId`
 *
 * ex:
 *
 * ```
 * val anvilId = project.libsCatalog.pluginId("square-anvil")
 * ```
 *
 * @since 0.1.0
 */
public fun VersionCatalog.pluginId(alias: String): String {
  val errorMessage by lazy(NONE) {
    "No plugin ID was found in the catalog for the alias '$alias'."
  }
  return findPlugin(alias)
    .orElseThrow { GradleException(errorMessage) }
    .orNull
    ?.pluginId
    ?: throw GradleException(errorMessage)
}

/**
 * A property delegate for accessing a plugin ID from the
 * receiver catalog, using the property name as the alias.
 *
 * ex:
 * ```
 * val anvil by project.libsCatalog.pluginId
 * ```
 *
 * @since 0.1.9
 */
public val VersionCatalog.pluginId: ReadOnlyProperty<Any?, String>
  get() = ReadOnlyProperty { _, prop -> pluginId(prop.name) }

/**
 * shorthand for `get().pluginId`
 *
 * @since 0.1.4
 */
public val Provider<PluginDependency>.pluginId: String
  get() = get().pluginId
