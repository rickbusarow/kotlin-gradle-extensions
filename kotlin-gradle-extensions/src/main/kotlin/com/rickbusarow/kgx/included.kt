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

import org.gradle.api.Task
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskCollection
import org.gradle.composite.internal.DefaultIncludedBuild
import org.gradle.composite.internal.DefaultIncludedBuild.IncludedBuildImpl

/**
 * Look at the root project of an included build, find any task with a
 * matching name, and return it or null. This is an alternative to the standard
 * [IncludedBuild.task][org.gradle.api.initialization.IncludedBuild.task] function in
 * that the standard `task` version will throw an exception if the task is not registered.
 *
 * Note that this forces the included build to configure.
 *
 * @since 0.1.0
 */
@EagerGradleApi
public fun Gradle.includedRootProjectsTasks(taskName: String): List<TaskCollection<Task>> {
  return includedBuilds.mapNotNull { included ->

    val includedImpl = included as IncludedBuildImpl

    val implState = includedImpl.target as DefaultIncludedBuild

    implState.ensureProjectsConfigured()

    implState.mutableModel.rootProject.tasks.matchingName(taskName)
  }
}
