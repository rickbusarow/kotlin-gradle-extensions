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

/**
 * Use of this API requires explicit opt-in due to the potential for eager initialization, which may
 * lead to side effects.
 *
 * @since 0.1.0
 */
@Retention(AnnotationRetention.BINARY)
@RequiresOptIn("Using this API may cause domain objects to be created eagerly.")
public annotation class EagerGradleApi
