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

import builds.VERSION_NAME
import com.github.gmazzo.buildconfig.BuildConfigTask

plugins {
  id("module")
  id("builds.gradle-tests")
  alias(libs.plugins.buildconfig)
}

val artifactId = "kotlin-gradle-extensions"

module {
  published(
    artifactId = artifactId,
    pomDescription = "Common utilities for Gradle"
  )
}

buildConfig {

  this@buildConfig.sourceSets.named(java.sourceSets.gradleTest.name) {

    packageName(builds.GROUP)
    className("BuildConfig")

    buildConfigField(
      type = "java.io.File",
      name = "localBuildM2Dir",
      value = rootProject.layout.buildDirectory.dir("m2").map { "File(\"${it}\")" }
    )
    buildConfigField(type = "String", name = "version", value = VERSION_NAME)
    buildConfigField(
      name = "mavenArtifact",
      value = provider { "${builds.GROUP}:$artifactId:${VERSION_NAME}" }
    )
    buildConfigField(name = "kotlinGradle", value = libs.versions.kotlin.gradle.get())
    buildConfigField(name = "kotlinLibraries", value = libs.versions.kotlin.libraries.get())
  }
}

rootProject.tasks.named("prepareKotlinBuildScriptModel") {
  dependsOn(tasks.withType(BuildConfigTask::class.java))
}

dependencies {

  api(project(":names"))

  compileOnly(gradleApi())

  compileOnly(libs.kotlin.gradle.plugin)
  compileOnly(libs.kotlin.gradle.plugin.api)

  gradleTestImplementation(gradleTestKit())

  gradleTestImplementation(libs.junit.engine)
  gradleTestImplementation(libs.junit.jupiter)
  gradleTestImplementation(libs.junit.jupiter.api)
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

  testImplementation(libs.junit.engine)
  testImplementation(libs.junit.jupiter)
  testImplementation(libs.junit.jupiter.api)
  testImplementation(libs.kotest.assertions.core.jvm)
  testImplementation(libs.kotest.assertions.shared)
  testImplementation(libs.kotlin.gradle.plugin)
  testImplementation(libs.kotlin.gradle.plugin.api)
}
