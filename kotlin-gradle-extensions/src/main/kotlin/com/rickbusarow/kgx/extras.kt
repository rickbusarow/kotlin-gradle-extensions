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

import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.ExtraPropertiesExtension

/**
 * The extra properties extension in this object's extension container.
 *
 * @see [ExtensionContainer.getExtraProperties]
 * @since 0.1.0
 */
public val ExtensionAware.extras: ExtraPropertiesExtension
  get() = extensions.extraProperties

/**
 * A safe version of [get][ExtraPropertiesExtension.get], since `get` will throw an
 * [UnknownPropertyException][ExtraPropertiesExtension.UnknownPropertyException] if the property
 * wasn't previously defined.
 *
 * @since 0.1.0
 */
public fun ExtraPropertiesExtension.getOrNull(
  name: String
): Any? = if (has(name)) get(name) else null

/**
 * A safe version of [get][ExtraPropertiesExtension.get], since `get` will throw an
 * [UnknownPropertyException][ExtraPropertiesExtension.UnknownPropertyException] if the property
 * wasn't previously defined.
 *
 * @since 0.1.0
 * @throws ClassCastException if a property named [name] exists, but is not of type T
 */
public inline fun <reified T> ExtraPropertiesExtension.getOrNullAs(name: String): T? {
  val existing = getOrNull(name) ?: return null
  return existing as T
}

/**
 * A typed version of [get][ExtraPropertiesExtension.get], since `get` just returns `Any`/`Object`.
 *
 * @param name the name of the property to retrieve
 * @since 0.1.9
 * @throws ExtraPropertiesExtension.UnknownPropertyException if the property wasn't previously
 *   defined.
 * @throws ClassCastException if a property named [name] exists, but is not of type T
 */
public inline fun <reified T> ExtraPropertiesExtension.getAs(name: String): T = get(name) as T

/**
 * Returns a value for [name] if one is already in the extra properties. If the name is not present,
 * a new value will be created using [default], and that value will be added to the properties.
 *
 * @since 0.1.0
 * @throws ClassCastException if a property named [name] exists, but is not of type T
 */
public inline fun <reified T : Any> ExtraPropertiesExtension.getOrPut(
  name: String,
  default: () -> T
): T = synchronized(this) {
  if (!has(name)) {
    set(name, default())
  }
  getAs<T>(name)
}
