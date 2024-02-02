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

import org.gradle.api.invocation.Gradle

/**
 * Generates a sequence of parent [Gradle] instances for the receiver [Gradle] instance,
 * starting at the receiver's immediate parent. The sequence will be empty if the
 * receiver is the root build. The sequence ends when it reaches the root build.
 *
 * @return A [Sequence] of parent [Gradle] instances.
 * @see
 *   [Gradle User Manual: Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html)
 * @since 0.1.4
 */
public fun Gradle.parents(): Sequence<Gradle> = generateSequence(parent) { it.parent }

/**
 * Generates a sequence of the receiver [Gradle] instance and its parents,
 * starting with the receiver. The sequence ends when it reaches the root build.
 *
 * @return A [Sequence] of [Gradle] instances including the receiver and its parents.
 * @see
 *   [Gradle User Manual: Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html)
 * @since 0.1.4
 */
public fun Gradle.parentsWithSelf(): Sequence<Gradle> = generateSequence(this) { it.parent }
