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

import builds.VERSION_NAME
import com.github.gmazzo.gradle.plugins.BuildConfigTask
import com.rickbusarow.kgx.dependsOn

plugins {
  id("module")
  id(
    libs
      .plugins
      .integration
      .test
      .get()
      .pluginId
  )
  alias(libs.plugins.buildconfig)
  idea
}

val artifactId = "kotlin-gradle-extensions"

module {
  published(
    artifactId = artifactId,
    pomDescription = "Common utilities for Gradle"
  )
  poko()
}

buildConfig {

  this@buildConfig.sourceSets.named(java.sourceSets.integration.name) {

    this@named.packageName(builds.GROUP)
    this@named.className("BuildConfig")

    this@named.buildConfigField(
      type = "String",
      name = "version",
      value = "\"${VERSION_NAME}\""
    )
    this@named.buildConfigField(
      type = "String",
      name = "mavenArtifact",
      value = provider { "\"${builds.GROUP}:$artifactId:${VERSION_NAME}\"" }
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

idea {
  module {
    java.sourceSets.integration {
      this@module.testSources.from(allSource.srcDirs)
    }
  }
}

dependencies {

  api(project(":names"))

  compileOnly(gradleApi())

  compileOnly(libs.kotlin.gradle.plugin)
  compileOnly(libs.kotlin.gradle.plugin.api)

  integrationImplementation(gradleTestKit())

  testImplementation(libs.junit.engine)
  testImplementation(libs.junit.jupiter)
  testImplementation(libs.junit.jupiter.api)
  testImplementation(libs.kotest.assertions.shared)
  testImplementation(libs.kotlin.gradle.plugin)
  testImplementation(libs.kotlin.gradle.plugin.api)
}

tasks.named("integrationTest").dependsOn("publishToMavenLocalNoDokka")
