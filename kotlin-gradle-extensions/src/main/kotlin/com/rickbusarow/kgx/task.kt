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

import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider

/**
 * Configures a task by name if it already exists, or configures all tasks that match the name.
 *
 * @param taskName The name of the task.
 * @param configurationAction The configuration block for the task.
 */
@EagerGradleApi
fun TaskContainer.maybeNamed(taskName: String, configurationAction: Action<Task>) {

  if (names.contains(taskName)) {
    named(taskName, configurationAction)
    return
  }

  matchingName(taskName).configureEach(configurationAction)
}

/**
 * code golf for `matching { it.name == taskName }`
 *
 * @param taskName The name of the tasks to filter.
 * @return A collection of tasks that have names matching [taskName].
 */
@EagerGradleApi
fun TaskContainer.matchingName(taskName: String): TaskCollection<Task> =
  matching { it.name == taskName }

/**
 * code golf for `withType<T>().matching { it.name == taskName }`
 *
 * @param taskName The name of the tasks to filter.
 * @return A collection of tasks of type [T] with names matching [taskName].
 */
@EagerGradleApi
inline fun <reified T : Task> TaskContainer.matchingNameWithType(
  taskName: String
): TaskCollection<T> = withType<T>().matching { it.name == taskName }

/**
 * Makes all tasks in this collection depend on the given objects.
 *
 * @param objects Dependency objects to be added to every task in the collection.
 * @return The original collection for chaining.
 */
fun <T : Task> TaskCollection<T>.dependOn(vararg objects: Any): TaskCollection<T> {
  return also { taskCollection ->
    taskCollection.configureEach { task -> task.dependsOn(*objects) }
  }
}

/**
 * Adds dependencies to the task wrapped by this provider.
 *
 * @param objects Dependency objects to be added to the task.
 * @return The original task provider for chaining.
 */
fun <T : Task> TaskProvider<T>.dependsOn(vararg objects: Any): TaskProvider<T> {
  return also { provider ->
    provider.configure { task ->
      task.dependsOn(*objects)
    }
  }
}

/**
 * Specifies that all tasks in this collection must run after the given objects.
 *
 * @param objects Dependency objects that should run before every task in the collection.
 * @return The original collection for chaining.
 */
fun <T : Task> TaskCollection<T>.mustRunAfter(vararg objects: Any): TaskCollection<T> {
  return also { taskCollection ->
    taskCollection.configureEach { task -> task.mustRunAfter(*objects) }
  }
}

/**
 * Specifies that the task wrapped by this provider must run after the given objects.
 *
 * @param objects Dependency objects that should run before the task.
 * @return The original task provider for chaining.
 */
fun <T : Task> TaskProvider<T>.mustRunAfter(vararg objects: Any): TaskProvider<T> {
  return also { provider ->
    provider.configure { task ->
      task.mustRunAfter(*objects)
    }
  }
}

/**
 * Returns a collection containing the objects in this collection of the given
 * type. The returned collection is live, so that when matching objects are later
 * added to this collection, they are also visible in the filtered collection.
 *
 * @return The matching objects. Returns an empty collection
 *   if there are no such objects in this collection.
 * @see [TaskCollection.withType]
 */
inline fun <reified S : Task> TaskCollection<in S>.withType(): TaskCollection<S> =
  withType(S::class.java)

/**
 * Registers a new task of type [T] and configures it.
 *
 * @param name The name of the task.
 * @param constructorArguments Arguments to be passed to the task's constructor.
 * @param configuration Configuration block for the task.
 * @return A task provider for the newly created task.
 */
inline fun <reified T : Task> TaskContainer.register(
  name: String,
  vararg constructorArguments: Any,
  noinline configuration: (T) -> Unit
): TaskProvider<T> = register(name, T::class.java, *constructorArguments)
  .apply { configure { configuration(it) } }

/**
 * Registers or retrieves a task by name and type, and then configures it.
 *
 * @param name The name of the task.
 * @param type The class of the task.
 * @param configurationAction The configuration action.
 * @return A task provider.
 */
fun <T : Task> TaskContainer.registerOnce(
  name: String,
  type: Class<T>,
  configurationAction: Action<in T>
): TaskProvider<T> = if (names.contains(name)) {
  named(name, type, configurationAction)
} else {
  register(name, type, configurationAction)
}

/**
 * @return the fully qualified name of this task's
 *   type, without any '_Decorated' suffix if one exists
 */
fun Task.undecoratedTypeName(): String {
  return javaClass.canonicalName.removeSuffix("_Decorated")
}

/**
 * Registers or retrieves a task by name and type, and then configures it.
 *
 * @param name The name of the task.
 * @param configurationAction The configuration action.
 * @return A task provider.
 */
@JvmName("registerOnceInline")
inline fun <reified T : Task> TaskContainer.registerOnce(
  name: String,
  configurationAction: Action<in T>
): TaskProvider<T> = registerOnce(name, T::class.java, configurationAction)

/**
 * Registers or retrieves a task by name and type, and then configures it.
 *
 * @param name The name of the task.
 * @return A task provider.
 */
inline fun <reified T : Task> TaskContainer.registerOnce(name: String): TaskProvider<T> =
  if (names.contains(name)) {
    named(name, T::class.java)
  } else {
    register(name, T::class.java)
  }

/**
 * makes the receiver task a dependency of the [dependentTask] parameter.
 *
 * @param dependentTask The task that will depend on this one.
 * @return The original task provider for chaining.
 */
fun <T : Task> TaskProvider<T>.addAsDependencyTo(dependentTask: TaskProvider<*>): TaskProvider<T> {
  return also { receiver ->
    dependentTask.dependsOn(receiver)
  }
}

/** makes the receiver task a dependency of the tasks in the [dependentTasks] collection.
 *
 * @receiver The task that will depend on this one.
 * @return The original task provider for chaining.
 */
fun <T : Task> TaskProvider<T>.addAsDependencyTo(
  dependentTasks: TaskCollection<*>
): TaskProvider<T> {
  return also { receiver ->
    dependentTasks.dependOn(receiver)
  }
}

/** Just a typed version of `task.outputs.upToDateWhen { ... }` */
internal inline fun <reified T : Task> T.outputsUpToDateWhen(
  crossinline predicate: (T) -> Boolean
) {
  outputs.upToDateWhen { predicate(it as T) }
}
