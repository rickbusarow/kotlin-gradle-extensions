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

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  kotlin("jvm")
  alias(libs.plugins.google.ksp)
  id("java-gradle-plugin")
}

gradlePlugin {
  plugins {
    create("builds.ktlint") {
      id = "builds.ktlint"
      implementationClass = "builds.ktlint.KtLintConventionPlugin"
    }
  }
}

// fixes the error
// 'Entry classpath.index is a duplicate but no duplicate handling strategy has been set.'
// when executing a Jar task
// https://github.com/gradle/gradle/issues/17236
tasks.withType<Jar>().configureEach {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {

  api(libs.ktlint.core)
  api(libs.ktlint.ruleset.standard)
  api(libs.slf4j.api)

  api(project(":core"))

  compileOnly(gradleApi())

  implementation(libs.google.auto.service.annotations)
  implementation(libs.jetbrains.markdown)
  implementation(libs.jmailen.kotlinter)
  implementation(libs.kotlin.compiler)
  implementation(libs.kotlin.reflect)
  implementation(libs.rickBusarow.ktrules)

  ksp(libs.zacSweers.auto.service.ksp)
}
