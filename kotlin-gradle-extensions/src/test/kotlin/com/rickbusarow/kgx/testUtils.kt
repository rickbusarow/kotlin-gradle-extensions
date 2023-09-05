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

import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import java.io.File
import java.util.stream.Stream

fun <T> Iterable<T>.container(
  name: (T) -> String,
  action: (T) -> Iterable<DynamicNode>
): Stream<DynamicContainer> = map { t ->
  DynamicContainer.dynamicContainer(name(t), action(t))
}.stream()

fun test(name: String, action: () -> Unit): DynamicTest = DynamicTest.dynamicTest(name, action)

fun <T> Iterable<T>.test(name: (T) -> String, action: (T) -> Unit): List<DynamicTest> = map { t ->
  DynamicTest.dynamicTest(name(t)) { action(t) }
}

/**
 * Creates a new file if it doesn't already exist, creating parent
 * directories if necessary. If the file already exists, its content will
 * be overwritten. If content is provided, it will be written to the file.
 *
 * @param content The content to be written to the file. Defaults to null.
 * @param overwrite If true, any existing content will be overwritten. Otherwise, nothing is done.
 * @return The created file.
 * @since 0.1.0
 */
fun File.createSafely(content: String? = null, overwrite: Boolean = true): File = apply {
  when {
    content != null && (!exists() || overwrite) -> makeParentDir().writeText(content)
    else -> {
      makeParentDir().createNewFile()
    }
  }
}

/**
 * Creates the directories represented by the receiver [File] if they don't already exist.
 *
 * @receiver [File] The directories to create.
 * @return The directory file.
 * @since 0.1.0
 */
fun File.mkdirsInline(): File = apply(File::mkdirs)

/**
 * Creates the parent directory of the receiver [File] if it doesn't already exist.
 *
 * @receiver [File] The file whose parent directory is to be created.
 * @return The file with its parent directory created.
 * @since 0.1.0
 */
fun File.makeParentDir(): File = apply {
  val fileParent = requireNotNull(parentFile) { "File's `parentFile` must not be null." }
  fileParent.mkdirs()
}

/**
 * Conditionally applies the provided transform function to the receiver
 * object if the predicate is true, then returns the result of that transform.
 * If the predicate is false, the receiver object itself is returned.
 *
 * @param predicate The predicate to determine whether to apply the transform function.
 * @param transform The transform function to apply to the receiver object.
 * @return The result of the transform function if the
 *   predicate is true, or the receiver object itself otherwise.
 * @since 0.1.0
 */
inline fun <T> T.letIf(predicate: Boolean, transform: (T) -> T): T {
  return if (predicate) transform(this) else this
}
