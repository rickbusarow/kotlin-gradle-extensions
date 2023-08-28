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

/** */
inline fun <E> List<E>.splitInclusive(predicate: (E) -> Boolean): List<List<E>> {

  val indices = buildList {
    add(0)

    for (index in (1 until lastIndex - 1)) {
      predicate(this@splitInclusive[index])
    }
  }
    .distinct()

  return buildList {
    for ((i, fromIndex) in indices.withIndex()) {
      if (i == indices.lastIndex) {
        add(this@splitInclusive.subList(fromIndex, this@splitInclusive.lastIndex))
      } else {
        add(this@splitInclusive.subList(fromIndex, indices[i + 1]))
      }
    }
  }
}
