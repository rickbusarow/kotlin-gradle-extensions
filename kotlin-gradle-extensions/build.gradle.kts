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

import com.github.gmazzo.gradle.plugins.BuildConfigTask
import com.rickbusarow.lattice.config.latticeProperties

plugins {
  // id("module")
  id("com.rickbusarow.lattice.kotlin-jvm")
  alias(libs.plugins.buildconfig)
  idea
}

val artifactId = "kotlin-gradle-extensions"

lattice {
  publishing {
    publishMaven(
      artifactId = artifactId,
      pomDescription = "Common utilities for Gradle"
    )
  }
  poko()
  gradleTests()
}

buildConfig {

  this@buildConfig.sourceSets.named("gradleTest") {

    this@named.packageName(group as String)
    this@named.className("BuildConfig")

    val VERSION_NAME = latticeProperties.versionName.get()

    this@named.buildConfigField(
      type = "String",
      name = "version",
      value = "\"${VERSION_NAME}\""
    )
    this@named.buildConfigField(
      type = "String",
      name = "mavenArtifact",
      value = provider { "\"${group as String}:$artifactId:${VERSION_NAME}\"" }
    )
    this@named.buildConfigField(
      type = "String",
      name = "kotlinVersion",
      value = "\"${libs.versions.kotlin.get()}\""
    )
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

  "gradleTestImplementation"(gradleTestKit())

  testImplementation(libs.junit.engine)
  testImplementation(libs.junit.jupiter)
  testImplementation(libs.junit.jupiter.api)
  testImplementation(libs.kotest.assertions.core.jvm)
  testImplementation(libs.kotest.assertions.shared)
  testImplementation(libs.kotlin.gradle.plugin)
  testImplementation(libs.kotlin.gradle.plugin.api)
}

// tasks.named("integrationTest").dependsOn("publishToMavenLocalNoDokka")
