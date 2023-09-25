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

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.TaskCollection
import java.io.File

/**
 * throws with [message] if the receiver project is not the root project
 *
 * @since 0.1.0
 * @throws IllegalStateException if the project is not the root project
 */
fun Project.checkProjectIsRoot(
  message: () -> Any = { "Only apply this plugin to the project root." }
) {
  if (this != rootProject) {
    throw GradleException(message().toString())
  }
}

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
 * shorthand for `this == rootProject`
 *
 * For composite builds, this will return true for the root of each included build.
 *
 * @see com.rickbusarow.kgx.internal.isRealRootProject to check
 *   if the project is the ultimate root of a composite build
 * @since 0.1.0
 */
fun Project.isRootProject(): Boolean = this == rootProject

/**
 * shorthand for `layout.buildDirectory.get().asFile`
 *
 * @since 0.1.0
 */
fun Project.buildDir(): File = layout.buildDirectory.get().asFile

/**
 * Finds all tasks named [taskName] in all projects.
 * Does not throw if there are no tasks with that name.
 *
 * @since 0.1.0
 * @throws IllegalStateException if the project is not the root project
 */
@EagerGradleApi
fun Project.allProjectsTasksMatchingName(taskName: String): List<TaskCollection<Task>> {
  checkProjectIsRoot { "only call `allProjectsTasksMatchingName(...)` from the root project." }
  return allprojects.map { proj -> proj.tasks.matchingName(taskName) }
}

/**
 * Finds all tasks named [taskName] in all projects.
 * Does not throw if there are no tasks with that name.
 *
 * @since 0.1.0
 * @throws IllegalStateException if the project is not the root project
 */
@EagerGradleApi
inline fun <reified T : Task> Project.allProjectsTasksMatchingNameWithType(
  taskName: String
): List<TaskCollection<T>> {
  checkProjectIsRoot { "only call `allProjectsTasksMatchingName(...)` from the root project." }
  return allprojects.map { proj ->
    proj.tasks.matchingNameWithType(taskName)
  }
}

/**
 * Finds all tasks named [taskName] in this project's subprojects.
 * Does not throw if there are no tasks with that name.
 *
 * @since 0.1.0
 */
@EagerGradleApi
fun Project.subProjectsTasksMatchingName(taskName: String): List<TaskCollection<Task>> {
  return subprojects.map { proj -> proj.tasks.matchingName(taskName) }
}

/**
 * Finds all tasks named [taskName] in this project's subprojects.
 * Does not throw if there are no tasks with that name.
 *
 * @since 0.1.0
 */
@EagerGradleApi
inline fun <reified T : Task> Project.subProjectsTasksMatchingNameWithType(
  taskName: String
): List<TaskCollection<T>> {
  return subprojects
    .map { proj -> proj.tasks.matchingNameWithType(taskName) }
}

/**
 * shorthand for `extensions.getByType(JavaPluginExtension::class.java)`
 *
 * @since 0.1.1
 */
val Project.java: JavaPluginExtension
  get() = extensions.getByType(JavaPluginExtension::class.java)

/**
 * shorthand for
 * `project.dependencies.project(mapOf("path" to path, "configuration" to configuration))`
 */
fun Project.projectDependency(
  path: String,
  configuration: String? = null
): ProjectDependency = dependencies.project(path, configuration)
