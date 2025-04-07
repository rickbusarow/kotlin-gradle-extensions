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

pluginManagement {
  val allowMavenLocal = providers
    .gradleProperty("kgx.allow-maven-local")
    .orNull
    .toBoolean()

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
  id("com.gradle.develocity") version "3.19.2"
}

val inGHA = !System.getenv("GITHUB_ACTIONS").isNullOrEmpty()

develocity {
  buildScan {

    uploadInBackground = true

    termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
    termsOfUseAgree = "yes"

    capture {
      testLogging = true
      buildLogging = true
      fileFingerprints = true
    }

    obfuscation {
      hostname { "<hostName>" }
      ipAddresses { listOf("<ip address>") }
      username { "<username>" }
    }

    tag(if (inGHA) "GitHub-Actions" else "Local")

    if (inGHA) {
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
  .orNull
  .toBoolean()

dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositories {
    if (allowMavenLocal) {
      logger.lifecycle("${rootProject.name} -- allowing mavenLocal for dependencies")
      mavenLocal()
    }
    google()
    mavenCentral()
  }
}

rootProject.name = "kgx"

include(
  ":names",
  ":kotlin-gradle-extensions"
)
