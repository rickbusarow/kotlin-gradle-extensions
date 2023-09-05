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
 * shorthand for `this == rootProject`
 *
 * For composite builds, this will return true for the root of each included build.
 *
 * @see com.rickbusarow.kgx.internal.isRealRootProject to check
 *   if the project is the ultimate root of a composite build
 * @since 0.1.0
 */
internal fun Project.isRootProject() = this == rootProject

/**
 * shorthand for `layout.buildDirectory.get().asFile`
 *
 * @since 0.1.0
 */
internal fun Project.buildDir(): File = layout.buildDirectory.get().asFile

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
