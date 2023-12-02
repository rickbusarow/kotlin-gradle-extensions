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

import com.rickbusarow.kgx.internal.ElementInfoAction
import com.rickbusarow.kgx.internal.ElementInfoAction.ElementValue
import com.rickbusarow.kgx.internal.InternalGradleApiAccess
import com.rickbusarow.kgx.internal.whenElementKnown
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

/**
 * Converts the integer value of a Java version to the Gradle DSL version of [JvmTarget].
 *
 * Note this is different from the Kotlin compiler's
 * type at `org.jetbrains.kotlin.config.JvmTarget`.
 *
 * @throws NullPointerException if KGP doesn't have an enum defined for the provided [targetInt].
 */
fun JvmTarget.Companion.fromInt(targetInt: Int): JvmTarget = when (targetInt) {
  @Suppress("MagicNumber")
  8
  -> JvmTarget.fromTarget("1.8")

  else -> JvmTarget.fromTarget("$targetInt")
}

/** Converts the [JvmTarget] to its integer version number */
fun JvmTarget.toInt(): Int = target.substringAfterLast('.').toInt()

/**
 * Applies the provided [ElementInfoAction] to a Kotlin target when
 * it gets registered. This function caters to both single target and
 * multiplatform Kotlin projects by delegating to the appropriate extension.
 *
 * For single-target projects, the action is directly applied to the target. For
 * multiplatform projects, the action is handed off to the [KotlinMultiplatformExtension].
 *
 * @param configurationAction The action to apply to the Kotlin target upon registration.
 * @receiver [KotlinProjectExtension] The Kotlin project extension,
 *   which could be a single-target or multiplatform extension.
 * @since 0.1.5
 */
@InternalGradleApiAccess
fun KotlinProjectExtension.onTargetRegistered(
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
 * Applies the provided [ElementInfoAction] to a Kotlin target when it gets registered
 * within a multiplatform project. This function leverages the [whenElementKnown]
 * extension to avoid triggering the target's creation or configuration prematurely.
 *
 * @param configurationAction The action to apply to the Kotlin target upon registration.
 * @receiver [KotlinMultiplatformExtension] The Kotlin multiplatform project extension.
 * @since 0.1.5
 */
@InternalGradleApiAccess
fun KotlinMultiplatformExtension.onTargetRegistered(
  configurationAction: ElementInfoAction<KotlinTarget>
) {
  targets.whenElementKnown { configurationAction.execute(it) }
}
