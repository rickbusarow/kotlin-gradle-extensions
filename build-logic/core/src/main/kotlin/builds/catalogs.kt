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

package builds

import com.rickbusarow.kgx.extras
import com.rickbusarow.kgx.getOrPut
import com.rickbusarow.kgx.libsCatalog
import com.rickbusarow.kgx.version
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider

/**
 * Convenience for reading the library version from `libs.versions.toml`
 *
 * @since 0.1.0
 */
val Project.VERSION_NAME: String
  get() = extras.getOrPut("VERSION_NAME") { libsCatalog.version("kgx-dev") }

/** @since 0.1.0 */
val Project.versionIsSnapshot: Boolean
  get() = extras.getOrPut("versionIsSnapshot") { VERSION_NAME.endsWith("-SNAPSHOT") }

const val GROUP: String = "com.rickbusarow.kgx"

/**
 * "1.6", "1.7", "1.8", etc.
 *
 * @since 0.1.0
 */
val Project.KOTLIN_API: String
  get() = libsCatalog.version("kotlinApi")

/**
 * the jdk used in packaging
 *
 * "1.6", "1.8", "11", etc.
 *
 * @since 0.1.0
 */
val Project.JVM_TARGET: String
  get() = libsCatalog.version("jvmTarget")

/**
 * the jdk used to build the project
 *
 * "1.6", "1.8", "11", etc.
 *
 * @since 0.1.0
 */
val Project.JDK: String
  get() = libsCatalog.version("jdk")

/**
 * `6`, `8`, `11`, etc.
 *
 * @since 0.1.0
 */
val Project.JVM_TARGET_INT: Int
  get() = JVM_TARGET.substringAfterLast('.').toInt()

val Provider<MinimalExternalModuleDependency>.moduleName: String
  get() = get().name
