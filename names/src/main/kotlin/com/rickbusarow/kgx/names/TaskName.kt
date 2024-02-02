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

package com.rickbusarow.kgx.names

import com.rickbusarow.kgx.names.SourceSetName.Companion.addPrefix
import com.rickbusarow.kgx.names.SourceSetName.Companion.addSuffix
import com.rickbusarow.kgx.names.SourceSetName.Companion.isMain
import org.gradle.api.Task
import org.gradle.language.base.plugins.LifecycleBasePlugin
import kotlin.properties.ReadOnlyProperty

/**
 * Wraps the unqualified, simple name of a Gradle Task, like `check` or `publishToMavenLocal`.
 *
 * @property value the name
 * @since 0.1.6
 */
@JvmInline
public value class TaskName(
  override val value: String
) : DomainObjectName<Task>,
  Comparable<TaskName> {
  override fun compareTo(other: TaskName): Int = value.compareTo(other.value)

  public companion object {
    /**
     * @see LifecycleBasePlugin.ASSEMBLE_TASK_NAME
     * @since 0.1.6
     */
    public val assemble: TaskName = TaskName(LifecycleBasePlugin.ASSEMBLE_TASK_NAME)

    /**
     * @see LifecycleBasePlugin.BUILD_TASK_NAME
     * @since 0.1.6
     */
    public val build: TaskName = TaskName(LifecycleBasePlugin.BUILD_TASK_NAME)

    /**
     * `jar`
     *
     * @since 0.1.7
     */
    public val jar: TaskName = TaskName("jar")

    /**
     * @see LifecycleBasePlugin.CHECK_TASK_NAME
     * @since 0.1.6
     */
    public val check: TaskName = TaskName(LifecycleBasePlugin.CHECK_TASK_NAME)

    /**
     * `compileJavaWithJavac`
     *
     * @since 0.1.7
     */
    public val compileJavaWithJavac: TaskName = TaskName("compileJavaWithJavac")

    /**
     * `compileJava`
     *
     * @since 0.1.7
     */
    public val compileJava: TaskName = TaskName("compileJava")

    /**
     * `compileKotlin`
     *
     * @since 0.1.7
     */
    public val compileKotlin: TaskName = TaskName("compileKotlin")

    /**
     * `testUnitTest`
     *
     * @since 0.1.7
     */
    public val testUnitTest: TaskName = TaskName("testUnitTest")

    /**
     * `test`
     *
     * @since 0.1.7
     */
    public val test: TaskName = TaskName("test")

    /**
     * @see LifecycleBasePlugin.CLEAN_TASK_NAME
     * @since 0.1.6
     */
    public val clean: TaskName = TaskName(LifecycleBasePlugin.CLEAN_TASK_NAME)

    /**
     * ex: `assemble` or `assembleDebug`
     *
     * @since 0.1.7
     */
    public fun SourceSetName.assemble(): TaskName = if (isMain()) assemble else addPrefix(assemble)

    /**
     * ex: `test` or `debugTest`
     *
     * @since 0.1.7
     */
    public fun SourceSetName.test(): TaskName = if (isMain()) test else addSuffix(test)

    /**
     * ex: `testUnitTest` or `testDebugUnitTest`
     *
     * @since 0.1.7
     */
    public fun SourceSetName.testUnitTest(): TaskName =
      if (isMain()) {
        testUnitTest
      } else {
        "${addPrefix("test")}UnitTest".asTaskName()
      }

    /**
     * ex: `compileKotlin` or `compileDebugKotlin`
     *
     * @since 0.1.7
     */
    public fun SourceSetName.compileKotlin(): TaskName =
      if (isMain()) {
        compileKotlin
      } else {
        "${addPrefix("compile")}Kotlin".asTaskName()
      }

    /**
     * ex: `compileJava` or `compileDebugJava`
     *
     * @since 0.1.7
     */
    public fun SourceSetName.compileJava(): TaskName =
      if (isMain()) {
        compileJava
      } else {
        "${addPrefix("compile")}Java".asTaskName()
      }

    /**
     * ex: `compileDebugJavaWithJavac`
     *
     * @since 0.1.7
     */
    public fun SourceSetName.compileJavaWithJavac(): TaskName =
      if (isMain()) {
        compileJavaWithJavac
      } else {
        "${addPrefix("compile")}JavaWithJavac".asTaskName()
      }

    /**
     * Creates a [TaskName] from the receiver string.
     *
     * @since 0.1.6
     */
    public fun String.asTaskName(): TaskName = TaskName(this)

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
     * @since 0.1.6
     */
    @Suppress("MemberNameEqualsClassName")
    public fun taskName(): ReadOnlyProperty<Any?, TaskName> =
      DomainObjectName.lazyName { name ->
        TaskName(name)
      }
  }
}
