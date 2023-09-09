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

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

buildscript {
  dependencies {
    classpath(libs.kotlin.gradle.plugin)
    classpath(libs.vanniktech.publish)
  }
}

plugins {
  base
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.moduleCheck)
  alias(libs.plugins.ktlint) apply false
}

moduleCheck {
  deleteUnused = true
  checks.sortDependencies = true
}

val kotlinApiVersion = libs.versions.kotlinApi.get()
val ktlintPluginId = libs.plugins.ktlint.get().pluginId

allprojects ap@{

  val innerProject = this@ap

  apply(plugin = ktlintPluginId)
  dependencies {
    "ktlint"(rootProject.libs.rickBusarow.ktrules)
  }

  if (innerProject != rootProject) {
    rootProject.tasks.named("ktlintCheck") {
      dependsOn(innerProject.tasks.named("ktlintCheck"))
    }
    rootProject.tasks.named("ktlintFormat") {
      dependsOn(innerProject.tasks.named("ktlintFormat"))
    }
  }

  plugins.withType(KotlinBasePlugin::class.java).configureEach {
    extensions.configure(KotlinJvmProjectExtension::class.java) {
      jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jdk.get()))
      }
    }
  }
  plugins.withType(JavaPlugin::class.java).configureEach {
    extensions.configure(JavaPluginExtension::class.java) {
      sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetBuildLogic.get())
    }

    configurations.named("compileClasspath") {
      // Github-release bundles Gradle, which confuses the IDE when trying to view Gradle source or
      // javadoc.
      exclude(group = "org.gradle")
    }
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {

      apiVersion = kotlinApiVersion

      jvmTarget = libs.versions.jvmTargetBuildLogic.get()

      freeCompilerArgs = freeCompilerArgs + listOf(
        "-opt-in=kotlin.RequiresOptIn"
      )
    }
  }
  tasks.withType<Test>().configureEach {
    useJUnitPlatform()
  }
}
