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
import org.gradle.api.Task
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.TaskCollection
import java.io.File

/**
 * throws with [message] if the receiver project is not the root project
 *
 * @since 0.1.0
 * @throws IllegalStateException if the project is not the root project
 */
public fun Project.checkProjectIsRoot(
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
public fun Project.isRealRootProject(): Boolean {
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
public val Project.isPartOfRootBuild: Boolean
  get() = gradle.parent == null

/**
 * shorthand for `this == rootProject`
 *
 * For composite builds, this will return true for the root of each included build.
 *
 * @see com.rickbusarow.kgx.isRealRootProject to check if
 *   the project is the ultimate root of a composite build
 * @since 0.1.0
 */
public fun Project.isRootProject(): Boolean = this == rootProject

/**
 * shorthand for `layout.buildDirectory.get().asFile`
 *
 * @since 0.1.0
 */
public fun Project.buildDir(): File = layout.buildDirectory.get().asFile

/**
 * Finds all tasks named [taskName] in all projects.
 * Does not throw if there are no tasks with that name.
 *
 * @since 0.1.0
 * @throws IllegalStateException if the project is not the root project
 */
@EagerGradleApi
public fun Project.allProjectsTasksMatchingName(taskName: String): List<TaskCollection<Task>> {
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
public inline fun <reified T : Task> Project.allProjectsTasksMatchingNameWithType(
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
public fun Project.subProjectsTasksMatchingName(taskName: String): List<TaskCollection<Task>> =
  subprojects.map { proj -> proj.tasks.matchingName(taskName) }

/**
 * Finds all tasks named [taskName] in this project's subprojects.
 * Does not throw if there are no tasks with that name.
 *
 * @since 0.1.0
 */
@EagerGradleApi
public inline fun <reified T : Task> Project.subProjectsTasksMatchingNameWithType(
  taskName: String
): List<TaskCollection<T>> = subprojects.map { proj -> proj.tasks.matchingNameWithType(taskName) }

/**
 * shorthand for `extensions.getByType(JavaPluginExtension::class.java)`
 *
 * @since 0.1.1
 */
@Deprecated("renamed to `javaExtension`", ReplaceWith("javaExtension"))
public val Project.java: JavaPluginExtension get() = javaExtension

/**
 * shorthand for `extensions.getByType(JavaPluginExtension::class.java)`
 *
 * @since 0.1.1
 */
public val Project.javaExtension: JavaPluginExtension
  get() = extensions.getByType(JavaPluginExtension::class.java)

/** shorthand for `extensions.findByType(JavaPluginExtension::class.java)` */
public val Project.javaExtensionOrNull: JavaPluginExtension?
  get() = extensions.findByType(JavaPluginExtension::class.java)

/**
 * shorthand for
 * `project.dependencies.project(mapOf("path" to path, "configuration" to configuration))`
 *
 * @since 0.1.5
 */
public fun Project.projectDependency(
  path: String,
  configuration: String? = null
): ProjectDependency = dependencies.project(path, configuration)

/**
 * `true` if the current build is part of the IntelliJ IDEA sync process
 *
 * @since 0.1.9
 */
public val Project.isInIdeaSync: Boolean
  get() = providers.getSystemPropertyOrNull("idea.sync.active")
    ?.toBooleanStrictOrNull() ?: false
