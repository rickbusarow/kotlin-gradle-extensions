/*
 * Copyright (C) 2025 Rick Busarow
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

import builds.GROUP

plugins {
  id("root")
  alias(libs.plugins.doks)
}

doks {
  dokSet {
    docs("README.md", "CHANGELOG.md")

    rule("maven-with-version") {
      regex = maven(GROUP)
      replacement = "$1:$2:${libs.versions.rickBusarow.kgx.get().escapeReplacement()}"
    }
    rule("kgx-group") {
      regex = "com\\.(?:rickbusarow|square|squareup)\\.kgx"
      replacement = GROUP
    }
  }
}
