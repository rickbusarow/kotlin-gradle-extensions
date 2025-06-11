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

import com.rickbusarow.kgx.internal.ElementInfoAction
import com.rickbusarow.kgx.internal.ElementInfoAction.ElementValue
import com.rickbusarow.kgx.internal.InternalGradleApiAccess
import com.rickbusarow.kgx.internal.whenElementKnown
import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

/**
 * Converts the integer value of a Java version to the Gradle DSL version of [JvmTarget].
 *
 * Note this is different from the Kotlin compiler's type at
 * `org.jetbrains.kotlin.config.JvmTarget`.
 *
 * @since 0.1.9
 * @throws NullPointerException if KGP doesn't have an enum defined for the provided [targetInt].
 */
public fun JvmTarget.Companion.fromInt(targetInt: Int): JvmTarget {
  @Suppress("MagicNumber")
  return when (targetInt) {
    8 -> JvmTarget.fromTarget("1.8")
    else -> JvmTarget.fromTarget("$targetInt")
  }
}

/**
 * Converts the [JvmTarget] to its integer version number
 *
 * @since 0.1.9
 */
public fun JvmTarget.toInt(): Int = target.substringAfterLast('.').toInt()

/**
 * Applies the provided [ElementInfoAction] to a Kotlin target when it gets registered. This
 * function caters to both single target and multiplatform Kotlin projects by delegating to the
 * appropriate extension.
 *
 * For single-target projects, the action is directly applied to the target. For multiplatform
 * projects, the action is handed off to the [KotlinMultiplatformExtension].
 *
 * @param configurationAction The action to apply to the Kotlin target upon registration.
 * @receiver [KotlinProjectExtension] The Kotlin project extension, which could be a single-target
 *   or multiplatform extension.
 * @since 0.1.5
 */
@InternalGradleApiAccess
public fun KotlinProjectExtension.onTargetRegistered(
  configurationAction: ElementInfoAction<KotlinTarget>
) {

  when (val extension = this) {
    is KotlinSingleTargetExtension<*> -> {
      extension.target.let { target ->

        configurationAction.invoke(
          elementName = target.name,
          elementType = target::class.java,
          elementValue = ElementValue(target)
        )
      }
    }

    is KotlinMultiplatformExtension -> {
      extension.onTargetRegistered(configurationAction = configurationAction)
    }
  }
}

/**
 * Applies the provided [ElementInfoAction] to a Kotlin target when it gets registered within a
 * multiplatform project. This function leverages the [whenElementKnown] extension to avoid
 * triggering the target's creation or configuration prematurely.
 *
 * @param configurationAction The action to apply to the Kotlin target upon registration.
 * @receiver [KotlinMultiplatformExtension] The Kotlin multiplatform project extension.
 * @since 0.1.5
 */
@InternalGradleApiAccess
public fun KotlinMultiplatformExtension.onTargetRegistered(
  configurationAction: ElementInfoAction<KotlinTarget>
) {
  targets.whenElementKnown { configurationAction.execute(it) }
}

/**
 * shorthand for `extensions.getByName("kotlin") as KotlinProjectExtension`
 *
 * @since 0.1.12
 */
public val Project.kotlinExtensionOrNull: KotlinProjectExtension?
  get() = extensions.findByName("kotlin") as? KotlinProjectExtension

/**
 * shorthand for `extensions.getByName("kotlin") as KotlinJvmProjectExtension`
 *
 * @since 0.1.12
 */
public val Project.kotlinJvmExtension: KotlinJvmProjectExtension
  get() = kotlinExtension as KotlinJvmProjectExtension

/**
 * shorthand for `extensions.findByName("kotlin") as? KotlinJvmProjectExtension`
 *
 * @since 0.1.12
 */
public val Project.kotlinJvmExtensionOrNull: KotlinJvmProjectExtension?
  get() = kotlinExtensionOrNull as? KotlinJvmProjectExtension

/**
 * Only executes [action] if the project has the kotlin plugin applied.
 *
 * Shorthand for:
 * ```
 * plugins.withId("org.jetbrains.kotlin.jvm") {
 *   extensions.configure<KotlinJvmProjectExtension>("kotlin") {
 *     // ...
 *   }
 * }
 * ```
 *
 * @since 0.1.12
 */
public fun Project.kotlinJvmExtensionSafe(action: Action<KotlinJvmProjectExtension>) {
  plugins.withKotlinJvmPlugin { action.execute(kotlinJvmExtension) }
}

/** [org.gradle.api.tasks.SourceSet] */
public typealias JavaSourceSet = org.gradle.api.tasks.SourceSet

/** Shorthand for `target.kotlinExtension.sourceSets.getByName(name)` */
public fun JavaSourceSet.kotlinSourceSet(target: Project): KotlinSourceSet {
  return target.kotlinExtension.sourceSets.getByName(name)
}

/** Shorthand for `target.kotlinExtensionOrNull?.sourceSets?.findByName(name)` */
public fun JavaSourceSet.kotlinSourceSetOrNull(target: Project): KotlinSourceSet? {
  return target.kotlinExtensionOrNull?.sourceSets?.findByName(name)
}

/** Shorthand for `target.javaExtension.sourceSets.getByName(name)` */
public fun KotlinSourceSet.javaSourceSet(target: Project): JavaSourceSet {
  return target.javaExtension.sourceSets.getByName(name)
}

/** Shorthand for `target.javaExtension.sourceSets.findByName(name)` */
public fun KotlinSourceSet.javaSourceSetOrNull(target: Project): JavaSourceSet? {
  return target.javaExtensionOrNull?.sourceSets?.findByName(name)
}
