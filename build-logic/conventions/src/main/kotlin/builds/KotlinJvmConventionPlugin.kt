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

package builds

import com.rickbusarow.kgx.applyOnce
import com.rickbusarow.kgx.dependsOn
import com.vanniktech.maven.publish.MavenPublishBasePlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

abstract class KotlinJvmConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.plugins.applyOnce("org.jetbrains.kotlin.jvm")

    target.extensions.configure(KotlinJvmProjectExtension::class.java) { extension ->
      extension.jvmToolchain { toolChain ->
        toolChain.languageVersion.set(JavaLanguageVersion.of(target.JDK))
      }
    }

    target.tasks.withType(KotlinCompile::class.java).configureEach { task ->
      task.kotlinOptions {
        allWarningsAsErrors = false

        val kotlinMajor = target.KOTLIN_API
        languageVersion = kotlinMajor
        apiVersion = kotlinMajor

        jvmTarget = target.JVM_TARGET

        freeCompilerArgs = freeCompilerArgs + listOf(
          "-Xinline-classes",
          "-Xsam-conversions=class",
          "-opt-in=kotlin.ExperimentalStdlibApi",
          "-opt-in=kotlin.RequiresOptIn",
          "-opt-in=kotlin.contracts.ExperimentalContracts"
        )
      }
    }

    target.plugins.withType(MavenPublishBasePlugin::class.java).configureEach {
      target.extensions.configure(JavaPluginExtension::class.java) { extension ->
        extension.sourceCompatibility = JavaVersion.toVersion(target.JVM_TARGET)
      }
      target.tasks.withType(JavaCompile::class.java).configureEach { task ->
        task.options.release.set(target.JVM_TARGET_INT)
      }
    }

    target.tasks.register("buildTests") { it.dependsOn("testClasses") }
    target.tasks.register("buildAll").dependsOn(
      target.provider { target.javaExtension.sourceSets.map { it.classesTaskName } }
    )

    // fixes the error
    // 'Entry classpath.index is a duplicate but no duplicate handling strategy has been set.'
    // when executing a Jar task
    // https://github.com/gradle/gradle/issues/17236
    target.tasks.withType(Jar::class.java).configureEach { task ->
      task.duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
  }

  val org.gradle.api.Project.javaExtension: JavaPluginExtension
    get() = extensions.getByType(JavaPluginExtension::class.java)
}
