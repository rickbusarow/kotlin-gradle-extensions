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
value class TaskName(
  override val value: String
) : DomainObjectName<Task>,
  Comparable<TaskName> {

  override fun compareTo(other: TaskName): Int = value.compareTo(other.value)

  companion object {

    /**
     * @see LifecycleBasePlugin.ASSEMBLE_TASK_NAME
     * @since 0.1.6
     */
    val assemble: TaskName = TaskName(LifecycleBasePlugin.ASSEMBLE_TASK_NAME)

    /**
     * @see LifecycleBasePlugin.BUILD_TASK_NAME
     * @since 0.1.6
     */
    val build: TaskName = TaskName(LifecycleBasePlugin.BUILD_TASK_NAME)

    /** `jar` */
    val jar: TaskName = TaskName("jar")

    /**
     * @see LifecycleBasePlugin.CHECK_TASK_NAME
     * @since 0.1.6
     */
    val check: TaskName = TaskName(LifecycleBasePlugin.CHECK_TASK_NAME)

    /** `compileJavaWithJavac` */
    val compileJavaWithJavac: TaskName = TaskName(
      "compileJavaWithJavac"
    )

    /** `compileJava` */
    val compileJava: TaskName = TaskName("compileJava")

    /** `compileKotlin` */
    val compileKotlin: TaskName = TaskName("compileKotlin")

    /** `testUnitTest` */
    val testUnitTest: TaskName = TaskName("testUnitTest")

    /** `test` */
    val test: TaskName = TaskName("test")

    /**
     * @see LifecycleBasePlugin.CLEAN_TASK_NAME
     * @since 0.1.6
     */
    val clean: TaskName = TaskName(LifecycleBasePlugin.CLEAN_TASK_NAME)

    /** ex: `assemble` or `assembleDebug` */
    fun SourceSetName.assemble(): TaskName = if (isMain()) assemble else addPrefix(assemble)

    /** ex: `test` or `debugTest` */
    fun SourceSetName.test(): TaskName = if (isMain()) test else addSuffix(test)

    /** ex: `testUnitTest` or `testDebugUnitTest` */
    fun SourceSetName.testUnitTest(): TaskName = if (isMain()) {
      testUnitTest
    } else {
      "${addPrefix("test")}UnitTest".asTaskName()
    }

    /** ex: `compileKotlin` or `compileDebugKotlin` */
    fun SourceSetName.compileKotlin(): TaskName = if (isMain()) {
      compileKotlin
    } else {
      "${addPrefix("compile")}Kotlin".asTaskName()
    }

    /** ex: `compileJava` or `compileDebugJava` */
    fun SourceSetName.compileJava(): TaskName = if (isMain()) {
      compileJava
    } else {
      "${addPrefix("compile")}Java".asTaskName()
    }

    /** ex: `compileDebugJavaWithJavac` */
    fun SourceSetName.compileJavaWithJavac(): TaskName = if (isMain()) {
      compileJavaWithJavac
    } else {
      "${addPrefix("compile")}JavaWithJavac".asTaskName()
    }

    /**
     * Creates a [TaskName] from the receiver string.
     *
     * @since 0.1.6
     */
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
     * @since 0.1.6
     */
    @Suppress("MemberNameEqualsClassName")
    fun taskName(): ReadOnlyProperty<Any?, TaskName> {
      return DomainObjectName.lazyName { name -> TaskName(name) }
    }
  }
}
