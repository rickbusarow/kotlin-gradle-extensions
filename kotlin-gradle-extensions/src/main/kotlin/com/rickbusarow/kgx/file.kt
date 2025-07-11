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

import java.io.File

/**
 * Walks upward in the file tree, looking for a directory which will resolve [relativePath].
 *
 * For example, given a receiver File path of './a/b/c/' and a `relativePath` of 'foo/bar.txt', this
 * function will attempt to resolve the following paths in order:
 *
 * ```text
 * ./a/b/c/foo/bar.txt
 * ./a/b/foo/bar.txt
 * ./a/foo/bar.txt
 * ./foo/bar.txt
 * ```
 *
 * @returns the first path to contain an [existent][File.exists] File for [relativePath], or `null`
 *   if it could not be resolved
 * @see resolveInParent for a version which throws if nothing is resolved
 * @since 0.1.0
 */
public fun File.resolveInParentOrNull(relativePath: String): File? =
  resolve(relativePath).existsOrNull()
    ?: parentFile?.resolveInParentOrNull(relativePath)

/**
 * Non-nullable version of [resolveInParentOrNull]
 *
 * @see resolveInParentOrNull for a nullable, non-throwing variant
 * @since 0.1.0
 * @throws IllegalArgumentException if a file cannot be resolved
 */
public fun File.resolveInParent(relativePath: String): File =
  requireNotNull(resolveInParentOrNull(relativePath)) {
    "Could not resolve a file with relative path in any parent paths.\n" +
      "\t       relative path: $relativePath\n" +
      "\tstarting parent path: $absolutePath"
  }.normalize()

/**
 * @return the receiver [File] if it exists in the file system, otherwise null
 * @since 0.1.0
 */
public fun File.existsOrNull(): File? = takeIf { it.exists() }

/**
 * @return true if the receiver [File] is a directory with at least one child file which satisfies
 *   [childPredicate]
 * @since 0.1.0
 */
public fun File.isDirectoryWithFiles(
  childPredicate: (File) -> Boolean = { it.exists() }
): Boolean =
  !isFile && listFiles()?.any(childPredicate) == true

/**
 * Returns true if the receiver [File] is `/build/` or `/.gradle/`, but there is no sibling
 * `/build.gradle.kts` or `/settings.gradle.kts`.
 *
 * The most common cause of this would be switching between git branches with different module
 * structures. Since `build` and `.gradle` directories are ignored in git, they'll stick around
 * after a branch switch.
 *
 * @since 0.1.0
 */
public fun File.isOrphanedBuildOrGradleDir(): Boolean =
  when {
    !isDirectory -> false
    name != "build" && name != ".gradle" -> false
    !exists() -> false
    parentFile!!.hasGradleProjectFiles() -> false
    else -> true
  }

/**
 * Returns true if the receiver [File] is `/gradle.properties`, but there is no sibling
 * `/build.gradle.kts` or `/settings.gradle.kts`.
 *
 * The most common cause of this would be switching between git branches with different module
 * structures. Since all `gradle.properties` files except the root are ignored in git, they'll stick
 * around after a branch switch.
 *
 * @since 0.1.0
 */
public fun File.isOrphanedGradleProperties(): Boolean =
  when {
    !isFile -> false
    name != "gradle.properties" -> false
    parentFile!!.hasGradleProjectFiles() -> false
    else -> true
  }

/**
 * Returns true if the receiver [File] is a directory which contains at least one of
 * `settings.gradle.kts`, `settings.gradle`, `build.gradle.kts`, or `build.gradle`.
 *
 * @since 0.1.0
 */
public fun File.hasGradleProjectFiles(): Boolean =
  when {
    !isDirectory -> false
    resolve("settings.gradle.kts").exists() -> true
    resolve("settings.gradle").exists() -> true
    resolve("build.gradle.kts").exists() -> true
    resolve("build.gradle").exists() -> true
    else -> false
  }
