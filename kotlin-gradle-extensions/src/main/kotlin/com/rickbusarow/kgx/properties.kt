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

import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * Retrieves a property named [name]. If the property is defined and its value is of
 * type [T], that value is returned. If the property is defined and is not of type [T],
 * an exception is thrown. If the property is not defined, [defaultValue] is returned.
 *
 * @param name the name of the property to retrieve
 * @param defaultValue the value to return if the property is not defined
 * @throws GradleException if the property is found but is not assignable to [T]
 */
inline fun <reified T> Project.property(
  name: String,
  defaultValue: T
): T {
  return propertyOrNull<T>(name) ?: defaultValue
}

/**
 * Retrieves a property named [name]. If the property is defined and its value is of
 * type [T], that value is returned. If the property is defined and is not of type [T],
 * an exception is thrown. If the property is not defined, [defaultValue] is returned.
 *
 * @param name the name of the property to retrieve
 * @param defaultValue the value to return if the property is not defined
 * @throws GradleException if the property is found but is not assignable to [T]
 */
inline fun <reified T> Project.property(
  name: String,
  defaultValue: () -> T
): T {
  return propertyOrNull<T>(name) ?: defaultValue()
}

/**
 * Retrieves a property named [name]. If the property is defined and its value is of
 * type [T], that value is returned. If the property is defined and is not of type
 * [T], an exception is thrown. If the property is not defined, `null` is returned.
 *
 * @param name the name of the property to retrieve
 * @throws GradleException if the property is found but is not assignable to [T]
 */
inline fun <reified T> Project.propertyOrNull(name: String): T? {

  val found = findProperty(name)

  if (found != null) {
    if (found is T) return found
    throw GradleException(
      "Property '$name' is of type ${found::class.qualifiedName}, " +
        "which is not assignable to the requested type of ${T::class.java.simpleName}"
    )
  }

  return null
}
