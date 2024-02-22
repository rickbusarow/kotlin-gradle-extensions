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

import com.rickbusarow.kgx.names.DomainObjectName
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectCollectionSchema.NamedDomainObjectSchema
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import kotlin.reflect.KClass

/**
 * Alternate syntax for `named { it.matches(regex) }`
 *
 * Returns a collection containing the tasks with names matching the provided
 * filter. The returned collection is live, so that when matching tasks are added
 * to this collection, they are also visible in the filtered collection. This
 * method will NOT cause any pending objects in this container to be realized.
 *
 * @param regex tasks with names matching this pattern will be included in the result
 * @return The collection of tasks with names satisfying the filter. Returns
 *   an empty collection if there are no such tasks in this collection.
 * @since 0.1.12
 */
public inline fun <reified T : Task> TaskCollection<T>.named(regex: Regex): TaskCollection<T> {
  return named { it.matches(regex) }
}

/**
 * Shorthand for `withType(type).named { it.matches(regex) }`
 *
 * Returns a collection containing the tasks with names matching the provided
 * filter. The returned collection is live, so that when matching tasks are added
 * to this collection, they are also visible in the filtered collection. This
 * method will NOT cause any pending objects in this container to be realized.
 *
 * @param regex tasks with names matching this pattern will be included in the result
 * @param type The type of tasks to filter.
 * @return The collection of tasks with names satisfying the filter. Returns
 *   an empty collection if there are no such tasks in this collection.
 * @since 0.1.12
 */
public fun <T : Task, R : T> TaskCollection<T>.named(
  regex: Regex,
  type: KClass<R>
): TaskCollection<R> {
  return withType(type.java).named { it.matches(regex) }
}

/**
 * Shorthand for `withType(type).named { it.matches(regex) }`
 *
 * Returns a collection containing the tasks with names matching the provided
 * filter. The returned collection is live, so that when matching tasks are added
 * to this collection, they are also visible in the filtered collection. This
 * method will NOT cause any pending objects in this container to be realized.
 *
 * @param regex tasks with names matching this pattern will be included in the result
 * @param type The type of tasks to filter.
 * @return The collection of tasks with names satisfying the filter. Returns
 *   an empty collection if there are no such tasks in this collection.
 * @since 0.1.12
 */
public fun <T : Task, R : T> TaskCollection<T>.named(
  regex: Regex,
  type: Class<R>
): TaskCollection<R> {
  return withType(type).named { it.matches(regex) }
}

/**
 * Configures a task by name if it already exists, or configures all tasks that match the name.
 *
 * @param taskName The name of the task.
 * @param configurationAction The configuration block for the task.
 * @since 0.1.0
 */
public fun TaskContainer.maybeNamed(
  taskName: String,
  configurationAction: Action<Task>
) {

  if (names.contains(taskName)) {
    named(taskName, configurationAction)
    return
  }

  configureEach {
    if (it.name == taskName) {
      configurationAction.execute(it)
    }
  }
}

/**
 * code golf for `matching { it.name == taskName }`
 *
 * @param taskName The name of the tasks to filter.
 * @return A collection of tasks that have names matching [taskName].
 * @since 0.1.0
 */
@EagerGradleApi
public fun TaskContainer.matchingName(taskName: String): TaskCollection<Task> =
  matching { it.name == taskName }

/**
 * code golf for `matching { it.name in taskNames }`
 *
 * @since 0.1.5
 */
@EagerGradleApi
public fun TaskContainer.matchingNames(vararg taskNames: String): TaskCollection<Task> {
  val names = taskNames.toSet()
  return matching { it.name in names }
}

/**
 * code golf for `withType<T>().matching { it.name == taskName }`
 *
 * @param taskName The name of the tasks to filter.
 * @return A collection of tasks of type [T] with names matching [taskName].
 * @since 0.1.0
 */
@EagerGradleApi
public inline fun <reified T : Task> TaskContainer.matchingNameWithType(
  taskName: String
): TaskCollection<T> = withType<T>().matching { it.name == taskName }

/**
 * Makes all tasks in this collection depend on the given objects.
 *
 * @param objects Dependency objects to be added to every task in the collection.
 * @return The original collection for chaining.
 * @since 0.1.0
 */
public fun <T : Task> TaskCollection<T>.dependOn(vararg objects: Any): TaskCollection<T> =
  also { taskCollection ->
    taskCollection.configureEach { task -> task.dependsOn(*objects) }
  }

/**
 * Adds dependencies to the task wrapped by this provider.
 *
 * @param objects Dependency objects to be added to the task.
 * @return The original task provider for chaining.
 * @since 0.1.0
 */
public fun <T : Task> TaskProvider<T>.dependsOn(vararg objects: Any): TaskProvider<T> =
  also { provider ->
    provider.configure { task ->
      task.dependsOn(*objects)
    }
  }

/**
 * Specifies that all tasks in this collection must run after the given objects.
 *
 * @param objects Dependency objects that should run before every task in the collection.
 * @return The original collection for chaining.
 * @since 0.1.0
 */
public fun <T : Task> TaskCollection<T>.mustRunAfter(vararg objects: Any): TaskCollection<T> =
  also { taskCollection ->
    taskCollection.configureEach { task -> task.mustRunAfter(*objects) }
  }

/**
 * Specifies that the task wrapped by this provider must run after the given objects.
 *
 * @param objects Dependency objects that should run before the task.
 * @return The original task provider for chaining.
 * @since 0.1.0
 */
public fun <T : Task> TaskProvider<T>.mustRunAfter(vararg objects: Any): TaskProvider<T> =
  also { provider ->
    provider.configure { task ->
      task.mustRunAfter(*objects)
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
 * @since 0.1.0
 */
public inline fun <reified S : Task> TaskCollection<in S>.withType(): TaskCollection<S> =
  withType(S::class.java)

/**
 * Registers a new task of type [T] and configures it.
 *
 * @param name The name of the task.
 * @param constructorArguments Arguments to be passed to the task's constructor.
 * @param configuration Configuration block for the task.
 * @return A task provider for the newly created task.
 * @since 0.1.0
 */
@Deprecated(
  "renamed to `registering` to avoid a collision " +
    "with the `register(String, Class<T>, Action<T>)` member method",
  ReplaceWith(
    "registering(name, *constructorArguments) { configuration(it) }",
    "com.rickbusarow.kgx.registering"
  )
)
public inline fun <reified T : Task> TaskContainer.register(
  name: String,
  vararg constructorArguments: Any,
  noinline configuration: (T) -> Unit
): TaskProvider<T> = registering(name, *constructorArguments) { configuration(it) }

/**
 * Registers a new task of type [T] and configures it.
 *
 * @param name The name of the task.
 * @param constructorArguments Arguments to be passed to the task's constructor.
 * @param configurationAction Configuration block for the task.
 * @return A task provider for the newly created task.
 * @since 0.1.11
 */
public inline fun <reified T : Task> TaskContainer.registering(
  name: String,
  vararg constructorArguments: Any,
  configurationAction: Action<T>
): TaskProvider<T> = register(
  name = name,
  type = T::class,
  *constructorArguments,
  configurationAction = configurationAction
)

/**
 * Registers a new task of type [T] and configures it.
 *
 * @param name The name of the task.
 * @param constructorArguments Arguments to be passed to the task's constructor.
 * @param configurationAction Configuration block for the task.
 * @return A task provider for the newly created task.
 * @since 0.1.11
 */
public inline fun <reified T : Task> TaskContainer.registering(
  name: DomainObjectName<T>,
  vararg constructorArguments: Any,
  configurationAction: Action<T>
): TaskProvider<T> =
  registering(name.value, *constructorArguments, configurationAction = configurationAction)

/**
 * Registers a new task of type [T] and configures it.
 *
 * @param name The name of the task.
 * @param type The class of the task.
 * @param constructorArguments Arguments to be passed to the task's constructor.
 * @param configurationAction Configuration block for the task.
 * @return A task provider for the newly created task.
 * @since 0.1.11
 */
public inline fun <reified T : Task> TaskContainer.register(
  name: String,
  type: KClass<T>,
  vararg constructorArguments: Any,
  configurationAction: Action<T>
): TaskProvider<T> = register(name, type.java, *constructorArguments)
  .apply { configure(configurationAction) }

/**
 * Registers a new task of type [T] and configures it.
 *
 * @param name The name of the task.
 * @param type The class of the task.
 * @param constructorArguments Arguments to be passed to the task's constructor.
 * @param configurationAction Configuration block for the task.
 * @return A task provider for the newly created task.
 * @since 0.1.11
 */
public inline fun <reified T : Task> TaskContainer.register(
  name: DomainObjectName<T>,
  type: KClass<T>,
  vararg constructorArguments: Any,
  configurationAction: Action<T>
): TaskProvider<T> = register(
  name = name.value,
  type = type,
  *constructorArguments,
  configurationAction = configurationAction
)

/**
 * Registers or retrieves a task by name and type, and then configures it.
 *
 * @param name The name of the task.
 * @param type The class of the task.
 * @param configurationAction The configuration action.
 * @return A task provider.
 * @since 0.1.0
 */
public fun <T : Task> TaskContainer.registerOnce(
  name: String,
  type: Class<T>,
  configurationAction: Action<in T>
): TaskProvider<T> = if (names.contains(name)) {
  named(name, type, configurationAction)
} else {
  register(name, type, configurationAction)
}

/**
 * Registers or retrieves a task by name and type, and then configures it.
 *
 * @param name The name of the task.
 * @param type The class of the task.
 * @param configurationAction The configuration action.
 * @return A task provider.
 * @since 0.1.11
 */
public fun <T : Task> TaskContainer.registerOnce(
  name: DomainObjectName<T>,
  type: Class<T>,
  configurationAction: Action<in T>
): TaskProvider<T> = registerOnce(
  name = name.value,
  type = type,
  configurationAction = configurationAction
)

/**
 * @return the fully qualified name of this task's
 *   type, without any '_Decorated' suffix if one exists
 * @since 0.1.0
 */
public fun Task.undecoratedTypeName(): String = javaClass.canonicalName.removeSuffix("_Decorated")

/**
 * Registers or retrieves a task by name and type, and then configures it.
 *
 * @param name The name of the task.
 * @param configurationAction The configuration action.
 * @return A task provider.
 * @since 0.1.0
 */
@JvmName("registerOnceInline")
public inline fun <reified T : Task> TaskContainer.registerOnce(
  name: String,
  configurationAction: Action<in T>
): TaskProvider<T> = registerOnce(name, T::class.java, configurationAction)

/**
 * Registers or retrieves a task by name and type, and then configures it.
 *
 * @param name The name of the task.
 * @return A task provider.
 * @since 0.1.0
 */
public inline fun <reified T : Task> TaskContainer.registerOnce(name: String): TaskProvider<T> =
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
 * @since 0.1.0
 */
public fun <T : Task> TaskProvider<T>.addAsDependencyTo(
  dependentTask: TaskProvider<*>
): TaskProvider<T> = also { receiver ->
  dependentTask.dependsOn(receiver)
}

/**
 * makes the receiver task a dependency of the tasks in the [dependentTasks] collection.
 *
 * @receiver The task that will depend on this one.
 * @return The original task provider for chaining.
 * @since 0.1.0
 */
public fun <T : Task> TaskProvider<T>.addAsDependencyTo(
  dependentTasks: TaskCollection<*>
): TaskProvider<T> = also { receiver ->
  dependentTasks.dependOn(receiver)
}

/**
 * Just a typed version of `task.outputs.upToDateWhen { ... }`
 *
 * @since 0.1.0
 */
public inline fun <reified T : Task> T.outputsUpToDateWhen(crossinline predicate: (T) -> Boolean) {
  outputs.upToDateWhen { predicate(it as T) }
}

/**
 * Shorthand for `(this as ExtensionAware).extraProperties`, since every
 * [TaskCollection] implementation will implement [ExtensionAware].
 *
 * @since 0.1.10
 */
public val TaskCollection<*>.extras: ExtraPropertiesExtension
  get() = (this as ExtensionAware).extensions.extraProperties

/**
 * @since 0.1.10
 * @throws IllegalArgumentException if there are multiple tasks of that name when ignoring its case
 */
public fun TaskCollection<*>.namedOrNull(taskName: String): NamedDomainObjectSchema? {

  // This will typically be a 1:1 grouping,
  // but Gradle does allow you to re-use task names with different capitalization,
  // like 'foo' and 'Foo'.
  val namesLowercase =
    extras.getOrPut("taskNamesLowercaseToSchema") {
      collectionSchema.elements.groupBy { it.name.lowercase() }
    }

  val taskNameLowercase = taskName.lowercase()

  // All tasks that match the lowercase name
  val lowercaseMatches = namesLowercase[taskNameLowercase] ?: return null

  // The task with the same case as the requested name, or null
  val exactMatch = lowercaseMatches.singleOrNull { it.name == taskName }

  if (exactMatch != null) {
    return exactMatch
  }

  require(lowercaseMatches.size == 1) {
    "Task name '$taskName' is ambiguous.  " +
      "It matches multiple tasks: ${lowercaseMatches.map { it.name }}"
  }

  return lowercaseMatches.single()
}
