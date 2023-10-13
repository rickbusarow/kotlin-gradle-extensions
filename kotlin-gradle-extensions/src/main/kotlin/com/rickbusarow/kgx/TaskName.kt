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

import org.gradle.api.Task
import org.gradle.language.base.plugins.LifecycleBasePlugin
import kotlin.properties.ReadOnlyProperty

/**
 * Wraps the unqualified, simple name of a Gradle Task, like `check` or `publishToMavenLocal`.
 *
 * @property value the name
 */
@JvmInline
value class TaskName(
  override val value: String
) : DomainObjectName<Task>,
  Comparable<TaskName> {

  override fun compareTo(other: TaskName): Int = value.compareTo(other.value)

  companion object {
    /** Creates a [TaskName] from the receiver string. */
    fun String.asTaskName(): TaskName = TaskName(this)

    /**
     * Returns a [ReadOnlyProperty] which lazily creates a [TaskName] from the property name.
     *
     * These two are equivalent:
     * ```
     * val check: TaskName by taskName()
     * val check: TaskName = TaskName("api")
     * ```
     *
     * @see TaskName
     */
    @Suppress("MemberNameEqualsClassName")
    fun taskName(): ReadOnlyProperty<Any?, TaskName> {
      return DomainObjectName.lazyName { name -> TaskName(name) }
    }

    /** @see LifecycleBasePlugin.ASSEMBLE_TASK_NAME */
    val assemble: TaskName = TaskName(LifecycleBasePlugin.ASSEMBLE_TASK_NAME)

    /** @see LifecycleBasePlugin.BUILD_TASK_NAME */
    val build: TaskName = TaskName(LifecycleBasePlugin.BUILD_TASK_NAME)

    /** @see LifecycleBasePlugin.CHECK_TASK_NAME */
    val check: TaskName = TaskName(LifecycleBasePlugin.CHECK_TASK_NAME)

    /** @see LifecycleBasePlugin.CLEAN_TASK_NAME */
    val clean: TaskName = TaskName(LifecycleBasePlugin.CLEAN_TASK_NAME)
  }
}
