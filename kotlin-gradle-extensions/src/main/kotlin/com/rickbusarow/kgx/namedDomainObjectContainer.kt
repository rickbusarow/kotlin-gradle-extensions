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

import com.rickbusarow.kgx.names.DomainObjectName
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.internal.Actions

/**
 * Registers or retrieves a [S] by name and type, then configures it.
 *
 * @param name The name of the new object.
 * @param configurationAction The configuration action.
 * @return A provider for the new object [S].
 * @since 0.1.6
 */
public inline fun <reified S : Any> NamedDomainObjectContainer<S>.registerOnce(
  name: DomainObjectName<S>,
  configurationAction: Action<in S> = Actions.doNothing()
): NamedDomainObjectProvider<S> =
  if (names.contains(name.value)) {
    named(name, configurationAction)
  } else {
    register(name, configurationAction)
  }

/**
 * Registers a new object [S] and configures it.
 *
 * @param name The name of the object.
 * @param configurationAction The configuration action.
 * @return A provider for the new object [S].
 * @since 0.1.6
 */
public inline fun <reified S : Any> NamedDomainObjectContainer<S>.register(
  name: DomainObjectName<S>,
  configurationAction: Action<in S> = Actions.doNothing()
): NamedDomainObjectProvider<S> = register(name.value, configurationAction)
