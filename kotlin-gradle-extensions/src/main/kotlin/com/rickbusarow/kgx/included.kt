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

import com.rickbusarow.kgx.internal.allIncludedProjects
import org.gradle.api.Task
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskCollection

/**
 * Look at the internal modules of an included build, find
 * any tasks with a matching name, and return them all.
 *
 * Note that this forces the included build to configure.
 */
@EagerGradleApi
fun Gradle.includedAllProjectsTasks(taskName: String): List<TaskCollection<Task>> {
  return allIncludedProjects().map { it.tasks.matchingName(taskName) }
}
