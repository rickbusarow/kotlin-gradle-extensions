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

pluginManagement {
  val allowMavenLocal = providers
    .gradleProperty("kgx.allow-maven-local")
    .orNull.toBoolean()

  repositories {
    if (allowMavenLocal) {
      logger.lifecycle("${rootProject.name} -- allowing mavenLocal for plugins")
      mavenLocal()
    }
    gradlePluginPortal()
    mavenCentral()
    google()
  }
  includeBuild("build-logic")
}

plugins {
  id("com.gradle.enterprise") version "3.13.4"
}

gradleEnterprise {
  buildScan {

    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"

    publishAlways()

    // https://docs.github.com/en/actions/learn-github-actions/variables#default-environment-variables

    tag(if (System.getenv("CI").isNullOrBlank()) "Local" else "CI")

    val gitHubActions = System.getenv("GITHUB_ACTIONS")?.toBoolean() ?: false

    if (gitHubActions) {
      // ex: `octocat/Hello-World` as in github.com/octocat/Hello-World
      val repository = System.getenv("GITHUB_REPOSITORY")!!
      val runId = System.getenv("GITHUB_RUN_ID")!!

      link(
        "GitHub Action Run",
        "https://github.com/$repository/actions/runs/$runId"
      )
    }
  }
}

val allowMavenLocal = providers
  .gradleProperty("kgx.allow-maven-local")
  .orNull.toBoolean()

dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositories {
    if (allowMavenLocal) {
      logger.lifecycle("${rootProject.name} -- allowing mavenLocal for dependencies")
      mavenLocal()
    }
    google()
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
  }
}

rootProject.name = "kotlin-gradle-extensions"

include(":kotlin-gradle-extensions")

// If this project is the real root of the build, copy the root project's properties file to included
// builds, to ensure that Gradle settings are identical and there's only 1 daemon.
// Note that with this copy, any changes to the included build's properties file will be overwritten.
if (gradle.parent == null) {
  (settings as org.gradle.initialization.DefaultSettings).includedBuilds
    .forEach { includedBuildSpec ->
      rootDir.resolve("gradle.properties")
        .copyTo(
          target = includedBuildSpec.rootDir.resolve("gradle.properties"),
          overwrite = true
        )
    }
}
