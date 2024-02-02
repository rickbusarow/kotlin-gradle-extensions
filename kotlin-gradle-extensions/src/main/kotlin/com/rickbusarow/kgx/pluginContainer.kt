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
import org.gradle.api.Plugin
import org.gradle.api.plugins.PluginContainer

/** @since 0.1.0 */
public inline fun PluginContainer.withAny(
  vararg ids: String,
  crossinline action: (Plugin<*>) -> Unit
) {
  for (id in ids) {
    withId(id) { action(it) }
  }
}

/**
 * Add the plugin if it hasn't been applied already.
 *
 * @since 0.1.0
 */
public fun PluginContainer.applyOnce(id: String) {
  if (!hasPlugin(id)) apply(id)
}

/**
 * Add the plugin if it hasn't been applied already.
 *
 * @since 0.1.0
 */
public inline fun <reified T : Plugin<*>> PluginContainer.applyOnce() {
  if (!hasPlugin(T::class.java)) apply(T::class.java)
}

/**
 * Shorthand for `plugins.withId("org.gradle.application") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withApplicationPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.application", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.application") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withApplicationPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.application") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.base") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withBasePlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.base", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.base") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withBasePlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.base") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.build-init") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withBuildInitPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.build-init", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.build-init") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withBuildInitPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.build-init") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.distribution") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withDistributionPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.distribution", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.distribution") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withDistributionPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.distribution") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.groovy") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withGroovyPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.groovy", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.groovy") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withGroovyPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.groovy") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.groovy-base") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withGroovyBasePlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.groovy-base", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.groovy-base") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withGroovyBasePlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.groovy-base") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.groovy-gradle-plugin") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withPrecompiledGroovyPluginsPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.groovy-gradle-plugin", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.groovy-gradle-plugin") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withPrecompiledGroovyPluginsPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.groovy-gradle-plugin") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.idea") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withIdeaPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.idea", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.idea") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withIdeaPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.idea") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.java") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withJavaPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.java", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.java") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withJavaPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.java") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-base") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withJavaBasePlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.java-base", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-base") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withJavaBasePlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.java-base") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-gradle-plugin") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withJavaGradlePluginPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.java-gradle-plugin", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-gradle-plugin") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withJavaGradlePluginPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.java-gradle-plugin") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-library") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withJavaLibraryPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.java-library", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-library") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withJavaLibraryPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.java-library") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-library-distribution") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withJavaLibraryDistributionPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.java-library-distribution", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-library-distribution") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withJavaLibraryDistributionPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.java-library-distribution") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-platform") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withJavaPlatformPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.java-platform", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-platform") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withJavaPlatformPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.java-platform") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-test-fixtures") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withJavaTestFixturesPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.java-test-fixtures", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.java-test-fixtures") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withJavaTestFixturesPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.java-test-fixtures") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.gradle.signing") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withSigningPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.gradle.signing", action)
}

/**
 * Shorthand for `plugins.withId("org.gradle.signing") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withSigningPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.gradle.signing") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.android") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withKotlinAndroidPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.jetbrains.kotlin.android", action)
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.android") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withKotlinAndroidPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.jetbrains.kotlin.android") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.js") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withKotlinJsPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.jetbrains.kotlin.js", action)
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.js") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withKotlinJsPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.jetbrains.kotlin.js") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.jvm") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withKotlinJvmPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.jetbrains.kotlin.jvm", action)
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.jvm") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withKotlinJvmPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.jetbrains.kotlin.jvm") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.kapt") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withKapt3Plugin(
  action: Action<in Plugin<*>>
) {
  withId("org.jetbrains.kotlin.kapt", action)
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.kapt") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withKapt3Plugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.jetbrains.kotlin.kapt") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.multiplatform") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withKotlinMultiplatformPlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.jetbrains.kotlin.multiplatform", action)
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.multiplatform") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withKotlinMultiplatformPlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.jetbrains.kotlin.multiplatform") { action(it) }
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.plugin.parcelize") { ... }`.
 *
 * @since 0.1.10
 */
public fun PluginContainer.withKotlinParcelizePlugin(
  action: Action<in Plugin<*>>
) {
  withId("org.jetbrains.kotlin.plugin.parcelize", action)
}

/**
 * Shorthand for `plugins.withId("org.jetbrains.kotlin.plugin.parcelize") { ... }`.
 *
 * @since 0.1.10
 */
public inline fun PluginContainer.withKotlinParcelizePlugin(
  crossinline action: (Plugin<*>) -> Unit
) {
  withId("org.jetbrains.kotlin.plugin.parcelize") { action(it) }
}
