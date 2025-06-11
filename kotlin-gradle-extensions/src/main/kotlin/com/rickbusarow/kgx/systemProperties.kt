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

import org.gradle.api.GradleException
import org.gradle.api.provider.ProviderFactory

/**
 * Retrieves a system property named [name]. If the property is defined and its value is of type
 * [T], that value is returned. If the property is defined and is not of type [T], an exception is
 * thrown. Returns [defaultValue] if the property is not defined.
 *
 * @param name the name of the property to retrieve
 * @param defaultValue the value to return if the property is not defined
 * @since 0.1.9
 * @throws GradleException if the property is found but is not assignable to [T]
 */
public fun ProviderFactory.getSystemProperty(name: String, defaultValue: String): String =
  getSystemPropertyOrNull(name) ?: defaultValue

/**
 * Retrieves a system property named [name]. If the property is defined and its value is of type
 * [T], that value is returned. If the property is defined and is not of type [T], an exception is
 * thrown. Returns [defaultValue] if the property is not defined.
 *
 * @param name the name of the property to retrieve
 * @param defaultValue the value to return if the property is not defined
 * @since 0.1.9
 * @throws GradleException if the property is found but is not assignable to [T]
 */
public inline fun ProviderFactory.systemProperty(
  name: String,
  defaultValue: () -> String
): String = getSystemPropertyOrNull(name) ?: defaultValue()

/**
 * Retrieves a system property named [name]. If the property is defined and its value is of type
 * [T], that value is returned. If the property is defined and is not of type [T], an exception is
 * thrown. Returns null if the property is not defined.
 *
 * @param name the name of the property to retrieve
 * @since 0.1.9
 * @throws GradleException if the property is found but is not assignable to [T]
 */
public fun ProviderFactory.getSystemPropertyOrNull(name: String): String? {
  return systemProperty(name).orNull
}
