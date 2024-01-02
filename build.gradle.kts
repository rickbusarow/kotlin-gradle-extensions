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

import com.rickbusarow.doks.DoksTask
import com.rickbusarow.kgx.mustRunAfter
import com.rickbusarow.lattice.config.latticeProperties
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.rickbusarow.lattice.root")
  // id("root")
  alias(libs.plugins.moduleCheck)
  alias(libs.plugins.doks)
}

moduleCheck {
  deleteUnused = true
  checks.sortDependencies = true
}

doks {
  dokSet {
    docs("README.md", "CHANGELOG.md")

    rule("maven-with-version") {
      regex = maven(group as String)
      replacement = "$1:$2:${latticeProperties.versionName.get().escapeReplacement()}"
    }
    rule("kgx-group") {
      regex = "com\\.(?:rickbusarow|square|squareup)\\.kgx"
      replacement = latticeProperties.group.get()
    }
  }
}

subprojects.map {
  it
    .tasks
    .withType(KotlinCompile::class.java)
    .mustRunAfter(tasks.withType(DoksTask::class.java))
}
