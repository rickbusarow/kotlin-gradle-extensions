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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginContainer
import java.io.File

/**
 * Determines whether the receiver project is the "real" root of this
 * composite build, as opposed to the root projects of included builds.
 *
 * @since 0.1.0
 */
fun Project.isRealRootProject(): Boolean {
  return isPartOfRootBuild && isRootProject()
}

/**
 * Determines whether the receiver project belongs to
 * the root build of a "composite" or "included" build.
 *
 * A build is considered a "root build" if it has no parent build.
 *
 * @return `true` if the project belongs to the root build, `false` otherwise.
 * @see org.gradle.api.initialization.IncludedBuild
 * @see
 *   [Gradle User Manual: Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html)
 * @since 0.1.4
 */
val Project.isPartOfRootBuild: Boolean
  get() = gradle.parent == null

/**
 * Generates a sequence of parent [Gradle] instances for the receiver [Gradle] instance,
 * starting at the receiver's immediate parent. The sequence will be empty if the
 * receiver is the root build. The sequence ends when it reaches the root build.
 *
 * @return A [Sequence] of parent [Gradle] instances.
 * @see
 *   [Gradle User Manual: Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html)
 * @since 0.1.4
 */
fun Gradle.parents(): Sequence<Gradle> = generateSequence(parent) { it.parent }

/**
 * Generates a sequence of the receiver [Gradle] instance and its parents,
 * starting with the receiver. The sequence ends when it reaches the root build.
 *
 * @return A [Sequence] of [Gradle] instances including the receiver and its parents.
 * @see
 *   [Gradle User Manual: Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html)
 * @since 0.1.4
 */
fun Gradle.parentsWithSelf(): Sequence<Gradle> = generateSequence(this) { it.parent }

/**
 * shorthand for `layout.buildDirectory.get().asFile`
 *
 * @since 0.1.0
 */
fun Project.buildDir(): File = layout.buildDirectory.get().asFile

/**
 * Add the plugin if it hasn't been applied already.
 *
 * @since 0.1.0
 */
fun PluginContainer.applyOnce(id: String) {
  if (!hasPlugin(id)) apply(id)
}

/**
 * Add the plugin if it hasn't been applied already.
 *
 * @since 0.1.0
 */
inline fun <reified T : Plugin<*>> PluginContainer.applyOnce() {
  if (!hasPlugin(T::class.java)) apply(T::class.java)
}

/**
 * throws with [message] if the receiver project is not the root project
 *
 * @since 0.1.0
 * @throws IllegalStateException if the project is not the root project
 */
fun Project.checkProjectIsRoot(
  message: () -> Any = { "Only apply this plugin to the project root." }
) {
  check(this == rootProject, message)
}

val Project.javaExtension: JavaPluginExtension
  get() = extensions.getByType(JavaPluginExtension::class.java)

/**
 * `rootProject == this`
 *
 * @since 0.1.0
 */
fun Project.isRootProject(): Boolean {
  return rootProject == this
}
