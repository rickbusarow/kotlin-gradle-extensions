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

import org.gradle.api.Action
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.plugins.PluginManager

/** @since 0.1.3 */
inline fun PluginManager.withAnyPlugin(
  vararg ids: String,
  crossinline action: (AppliedPlugin) -> Unit
) {
  for (id in ids) {
    withPlugin(id) { action(it) }
  }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.application") { ... }`. */
fun PluginManager.withApplicationPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.application", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.application") { ... }`. */
inline fun PluginManager.withApplicationPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.application") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.base") { ... }`. */
fun PluginManager.withBasePlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.base", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.base") { ... }`. */
inline fun PluginManager.withBasePlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.base") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.build-init") { ... }`. */
fun PluginManager.withBuildInitPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.build-init", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.build-init") { ... }`. */
inline fun PluginManager.withBuildInitPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.build-init") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.distribution") { ... }`. */
fun PluginManager.withDistributionPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.distribution", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.distribution") { ... }`. */
inline fun PluginManager.withDistributionPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.distribution") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.groovy") { ... }`. */
fun PluginManager.withGroovyPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.groovy", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.groovy") { ... }`. */
inline fun PluginManager.withGroovyPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.groovy") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.groovy-base") { ... }`. */
fun PluginManager.withGroovyBasePlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.groovy-base", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.groovy-base") { ... }`. */
inline fun PluginManager.withGroovyBasePlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.groovy-base") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.groovy-gradle-plugin") { ... }`. */
fun PluginManager.withPrecompiledGroovyPluginsPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.groovy-gradle-plugin", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.groovy-gradle-plugin") { ... }`. */
inline fun PluginManager.withPrecompiledGroovyPluginsPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.groovy-gradle-plugin") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.idea") { ... }`. */
fun PluginManager.withIdeaPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.idea", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.idea") { ... }`. */
inline fun PluginManager.withIdeaPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.idea") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java") { ... }`. */
fun PluginManager.withJavaPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.java", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java") { ... }`. */
inline fun PluginManager.withJavaPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.java") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-base") { ... }`. */
fun PluginManager.withJavaBasePlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.java-base", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-base") { ... }`. */
inline fun PluginManager.withJavaBasePlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.java-base") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-gradle-plugin") { ... }`. */
fun PluginManager.withJavaGradlePluginPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.java-gradle-plugin", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-gradle-plugin") { ... }`. */
inline fun PluginManager.withJavaGradlePluginPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.java-gradle-plugin") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-library") { ... }`. */
fun PluginManager.withJavaLibraryPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.java-library", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-library") { ... }`. */
inline fun PluginManager.withJavaLibraryPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.java-library") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-library-distribution") { ... }`. */
fun PluginManager.withJavaLibraryDistributionPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.java-library-distribution", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-library-distribution") { ... }`. */
inline fun PluginManager.withJavaLibraryDistributionPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.java-library-distribution") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-platform") { ... }`. */
fun PluginManager.withJavaPlatformPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.java-platform", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-platform") { ... }`. */
inline fun PluginManager.withJavaPlatformPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.java-platform") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-test-fixtures") { ... }`. */
fun PluginManager.withJavaTestFixturesPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.java-test-fixtures", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.java-test-fixtures") { ... }`. */
inline fun PluginManager.withJavaTestFixturesPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.java-test-fixtures") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.signing") { ... }`. */
fun PluginManager.withSigningPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.gradle.signing", action)
}

/** Shorthand for `pluginManager.withPlugin("org.gradle.signing") { ... }`. */
inline fun PluginManager.withSigningPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.gradle.signing") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.android") { ... }`. */
fun PluginManager.withKotlinAndroidPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.jetbrains.kotlin.android", action)
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.android") { ... }`. */
inline fun PluginManager.withKotlinAndroidPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.jetbrains.kotlin.android") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.js") { ... }`. */
fun PluginManager.withKotlinJsPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.jetbrains.kotlin.js", action)
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.js") { ... }`. */
inline fun PluginManager.withKotlinJsPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.jetbrains.kotlin.js") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.jvm") { ... }`. */
fun PluginManager.withKotlinJvmPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.jetbrains.kotlin.jvm", action)
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.jvm") { ... }`. */
inline fun PluginManager.withKotlinJvmPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.jetbrains.kotlin.jvm") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.kapt") { ... }`. */
fun PluginManager.withKapt3Plugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.jetbrains.kotlin.kapt", action)
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.kapt") { ... }`. */
inline fun PluginManager.withKapt3Plugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.jetbrains.kotlin.kapt") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") { ... }`. */
fun PluginManager.withKotlinMultiplatformPlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.jetbrains.kotlin.multiplatform", action)
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") { ... }`. */
inline fun PluginManager.withKotlinMultiplatformPlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.jetbrains.kotlin.multiplatform") { action(it) }
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.plugin.parcelize") { ... }`. */
fun PluginManager.withKotlinParcelizePlugin(
  action: Action<in AppliedPlugin>
) {
  withPlugin("org.jetbrains.kotlin.plugin.parcelize", action)
}

/** Shorthand for `pluginManager.withPlugin("org.jetbrains.kotlin.plugin.parcelize") { ... }`. */
inline fun PluginManager.withKotlinParcelizePlugin(
  crossinline action: (AppliedPlugin) -> Unit
) {
  withPlugin("org.jetbrains.kotlin.plugin.parcelize") { action(it) }
}
