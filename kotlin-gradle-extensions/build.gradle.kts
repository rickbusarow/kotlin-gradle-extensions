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
  id("com.rickbusarow.mahout.kotlin-jvm-module")
  id("com.rickbusarow.mahout.gradle-test")
  alias(libs.plugins.buildconfig)
}

val artifactId = "kotlin-gradle-extensions"

mahout {
  publishing {
    publishMaven(
      artifactId = artifactId,
      pomDescription = "Common utilities for Gradle"
    )
  }
  kotlin {
    explicitApi = true
  }
}

buildConfig {

  this@buildConfig.sourceSets.named(java.sourceSets.gradleTest.name) {

    packageName(mahoutProperties.group.get())
    className("BuildConfig")

    buildConfigField(
      name = "localBuildM2Dir",
      value = rootProject.layout.buildDirectory.dir("gradle-test-m2")
        .map { it.asFile.relativeToOrSelf(projectDir) }
    )
    buildConfigField(name = "version", value = mahoutProperties.versionName)

    buildConfigField(
      name = "mavenArtifact",
      value = mahoutProperties.group.zip(mahoutProperties.versionName) { group, version ->
        "$group:$artifactId:$version"
      }
    )
    buildConfigField(name = "kotlinGradle", value = libs.versions.kotlin.gradle)
    buildConfigField(name = "kotlinLibraries", value = libs.versions.kotlin.libraries)
  }
}

dependencies {

  api(project(":names"))

  compileOnly(gradleApi())

  compileOnly(libs.kotlin.gradle.plugin)
  compileOnly(libs.kotlin.gradle.plugin.api)

  gradleTestImplementation(libs.junit.jupiter)
  gradleTestImplementation(libs.junit.jupiter.api)
  gradleTestImplementation(libs.junit.platform.launcher)
  gradleTestImplementation(libs.kotest.assertions.core.jvm)
  gradleTestImplementation(libs.kotest.assertions.shared)
  gradleTestImplementation(libs.kotlin.gradle.plugin)
  gradleTestImplementation(libs.kotlin.gradle.plugin.api)
  gradleTestImplementation(libs.rickBusarow.kase)
  gradleTestImplementation(libs.rickBusarow.kase.gradle) {
    exclude(group = "com.rickbusarow.kgx")
  }
  gradleTestImplementation(libs.rickBusarow.kase.gradle.dsl) {
    exclude(group = "com.rickbusarow.kgx")
  }

  testImplementation(gradleApi())

  testImplementation(libs.junit.jupiter)
  testImplementation(libs.junit.jupiter.api)
  testImplementation(libs.kotest.assertions.core.jvm)
  testImplementation(libs.kotest.assertions.shared)
  testImplementation(libs.kotlin.gradle.plugin)
  testImplementation(libs.kotlin.gradle.plugin.api)
  testImplementation(libs.rickBusarow.kase)
}
