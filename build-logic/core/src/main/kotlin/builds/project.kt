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

package builds

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.GradleInternal
import org.gradle.api.plugins.PluginContainer

/**
 * Determines whether the receiver project is the "real" root of this
 * composite build, as opposed to the root projects of included builds.
 */
fun Project.isRealRootProject(): Boolean {
  return (gradle as GradleInternal).isRootBuild && this == rootProject
}

/** Add the plugin if it hasn't been applied already. */
fun PluginContainer.applyOnce(id: String) {
  if (!hasPlugin(id)) apply(id)
}

/** Add the plugin if it hasn't been applied already. */
inline fun <reified T : Plugin<*>> PluginContainer.applyOnce() {
  if (!hasPlugin(T::class.java)) apply(T::class.java)
}

/**
 * throws with [message] if the receiver project is not the root project
 *
 * @throws IllegalStateException if the project is not the root project
 */
fun Project.checkProjectIsRoot(
  message: () -> Any = { "Only apply this plugin to the project root." }
) {
  check(this == rootProject, message)
}
