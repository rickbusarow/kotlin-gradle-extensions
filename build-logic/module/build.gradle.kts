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

plugins {
  kotlin("jvm")
  id("java-gradle-plugin")
}

gradlePlugin {
  plugins {
    create("composite") {
      id = "composite"
      implementationClass = "builds.CompositePlugin"
    }
    create("module") {
      id = "module"
      implementationClass = "builds.ModulePlugin"
    }
    create("root") {
      id = "root"
      implementationClass = "builds.RootPlugin"
    }
  }
}

dependencies {

  api("io.github.classgraph:classgraph:4.8.179")

  api(libs.breadmoirai.github.release) {
    exclude(group = "org.gradle")
  }
  api(libs.rickBusarow.doks)
  api(libs.rickBusarow.ktlint)

  api(project(":conventions"))

  compileOnly(gradleApi())

  implementation(libs.dependency.analysis.gradle.plugin) {
    exclude(group = "org.jetbrains.kotlin")
  }
  implementation(libs.detekt.gradle)
  implementation(libs.dokka.gradle)
  implementation(libs.kotlinx.binaryCompatibility)
  implementation(libs.rickBusarow.kgx)

  implementation(project(":artifacts"))
  implementation(project(":core"))
}
