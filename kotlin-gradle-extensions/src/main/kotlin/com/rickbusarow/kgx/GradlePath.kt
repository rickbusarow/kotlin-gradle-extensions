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

import org.gradle.StartParameter

public typealias GradlePath = org.gradle.util.Path

// /**
//  * For fully qualified/absolute path like `:a:b:c`, this would be `:`.
//  *
//  * ```text
//  * receiver | returned
//  * ---------|---------
//  * :a:b:c   | :
//  * :        | :
//  * a:b:c    | a
//  * ```
//  *
//  */
// val GradlePath.root: GradlePath
//   @Suppress("UnstableApiUsage")
//   get() = this.takeFirstSegments(1)

/** For a path of `:a:b:c`, this would be `:a`. */
public val GradlePath.firstSegment: GradlePath
  @Suppress("UnstableApiUsage")
  get() = takeFirstSegments(1)

/** For a path of `:a:b:c`, this would be `c`. */
public val GradlePath.lastSegment: GradlePath
  get() = removeFirstSegments(segmentCount() - 1)

/**
 * Travels from the full path upwards.
 *
 * For a path of `:a:b:c`, this would be `[ ':a:b', ':a' ]`.
 */
public fun GradlePath.parents(): Sequence<GradlePath> = generateSequence(parent) { it.parent }

/**
 * Travels from the full path upwards.
 *
 * For a path of `:a:b:c`, this would be `[ ':a:b:c', ':a:b', ':a' ]`.
 */
public fun GradlePath.parentsWithSelf(): Sequence<GradlePath> {
  return generateSequence(this) { it.parent }
}

public fun StartParameter.taskNamesAsPaths(): List<GradlePath> {
  return taskNames.map { GradlePath.path(it) }
}
