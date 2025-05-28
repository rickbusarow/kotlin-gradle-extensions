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
    create("builds.ben-manes") {
      id = "builds.ben-manes"
      implementationClass = "builds.BenManesVersionsPlugin"
    }
    create("builds.check") {
      id = "builds.check"
      implementationClass = "builds.CheckPlugin"
    }
    create("builds.clean") {
      id = "builds.clean"
      implementationClass = "builds.CleanPlugin"
    }
    create("builds.dependency-guard") {
      id = "builds.dependency-guard"
      implementationClass = "builds.DependencyGuardConventionPlugin"
    }
    create("builds.detekt") {
      id = "builds.detekt"
      implementationClass = "builds.DetektConventionPlugin"
    }
    create("builds.dokka") {
      id = "builds.dokka"
      implementationClass = "builds.DokkaConventionPlugin"
    }
    create("builds.dokka-versioning") {
      id = "builds.dokka-versioning"
      implementationClass = "builds.DokkaVersionArchivePlugin"
    }
    create("builds.github-release") {
      id = "builds.github-release"
      implementationClass = "builds.GitHubReleasePlugin"
    }
    create("builds.gradle-tests") {
      id = "builds.gradle-tests"
      implementationClass = "builds.GradleTestConventionPlugin"
    }
    create("builds.kotlin") {
      id = "builds.kotlin"
      implementationClass = "builds.KotlinJvmConventionPlugin"
    }
    create("builds.ktlint") {
      id = "builds.ktlint"
      implementationClass = "builds.KtLintConventionPlugin"
    }
    create("builds.spotless") {
      id = "builds.spotless"
      implementationClass = "builds.SpotlessConventionPlugin"
    }
    create("builds.test") {
      id = "builds.test"
      implementationClass = "builds.TestConventionPlugin"
    }
  }
}

dependencies {

  api(libs.rickBusarow.doks)
  api(libs.rickBusarow.ktlint)

  api(project(path = ":core"))

  // Expose the generated version catalog API to the plugins.
  compileOnly(files(libs::class.java.superclass.protectionDomain.codeSource.location))

  compileOnly(gradleApi())

  implementation(libs.benManes.versions)
  implementation(libs.breadmoirai.github.release)
  implementation(libs.detekt.gradle)
  implementation(libs.diffplug.spotless)
  implementation(libs.dokka.core)
  implementation(libs.dokka.gradle)
  implementation(libs.dokka.versioning)
  implementation(libs.dropbox.dependencyGuard)
  implementation(libs.kotlin.gradle.plugin)
  implementation(libs.kotlin.gradle.plugin.api)
  implementation(libs.kotlinx.binaryCompatibility)
  implementation(libs.rickBusarow.kgx)
  implementation(libs.vanniktech.publish)
}
