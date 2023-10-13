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

package com.rickbusarow.kgx.stdlib

import java.util.Locale

/**
 * Decapitalizes the first character of this [String] using the specified [locale].
 *
 * @param locale The [Locale] to be used for decapitalization. Defaults to [Locale.US].
 * @receiver The original String.
 * @return The string with the first character decapitalized.
 * @since 0.1.6
 */
internal fun String.decapitalize(locale: Locale = Locale.US): String =
  replaceFirstChar { it.lowercase(locale) }

/**
 * Capitalizes the first character of this [String] using the specified [locale].
 *
 * @param locale The [Locale] to be used for capitalization. Defaults to [Locale.US].
 * @receiver The original String.
 * @return The string with the first character capitalized.
 * @since 0.1.6
 */
internal fun String.capitalize(locale: Locale = Locale.US): String =
  replaceFirstChar { it.uppercase(locale) }
