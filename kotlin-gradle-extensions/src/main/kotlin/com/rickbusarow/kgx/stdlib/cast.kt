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

package com.rickbusarow.kgx.stdlib

import kotlin.reflect.KClass

@PublishedApi
internal inline fun <reified T : Any, reified R : T> T.castNamed(name: String): R {
  return castNamed(name, R::class)
}

@PublishedApi
internal inline fun <reified T : Any, reified R : T> T.castNamed(
  name: String,
  expectedType: KClass<R>
): R {
  val thisVal = this

  return thisVal as? R ?: throw ClassCastException(
    """
      Property '$name' cannot be cast to the requested type.
                value: $thisVal
          actual type: ${thisVal::class.qualifiedName}
        required type: ${expectedType.qualifiedName}
    """.trimIndent()
  )
}
