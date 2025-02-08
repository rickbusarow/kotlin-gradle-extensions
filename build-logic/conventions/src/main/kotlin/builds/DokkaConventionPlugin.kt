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

package builds

import com.rickbusarow.kgx.applyOnce
import com.rickbusarow.kgx.isRootProject
import com.rickbusarow.kgx.library
import com.rickbusarow.kgx.libsCatalog
import com.rickbusarow.kgx.project
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.AbstractDokkaTask
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin
import org.jetbrains.kotlin.gradle.plugin.extraProperties

abstract class DokkaConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {

    target.extraProperties.apply {
      set("org.jetbrains.dokka.experimental.gradle.pluginMode", "V2Enabled")
      set("org.jetbrains.dokka.experimental.gradle.pluginMode.noWarn", "true")
    }

    target.plugins.applyOnce("org.jetbrains.dokka")

    target.extensions.configure(DokkaExtension::class.java) { dokka ->

      val fullModuleName = target.path.removePrefix(":")
      dokka.moduleName.set(fullModuleName)

      if (target.isRootProject()) {
        target.subprojects { sub ->
          target.dependencies.add("dokka", target.dependencies.project(sub.path))
        }
        return@configure
      }

      dokka.dokkaSourceSets.named("main") { main ->
        main.documentedVisibilities(
          VisibilityModifier.Public,
          VisibilityModifier.Private,
          VisibilityModifier.Protected,
          VisibilityModifier.Internal,
          VisibilityModifier.Package
        )

        main.languageVersion.set(target.KOTLIN_API)
        main.jdkVersion.set(target.JVM_TARGET_INT)

        // include all project sources when resolving kdoc samples
        main.samples.setFrom(target.fileTree(target.file("src")))

        val readmeFile = target.file("README.md")

        if (readmeFile.exists()) {
          main.includes.from(readmeFile)
        }

        main.sourceLink { spec ->
          spec.localDirectory.set(target.file("src/main"))
          spec.remoteUrl(
            "https://github.com/rickbusarow/kotlin-gradle-extensions" +
              "/blob/main/${target.path}/src/main"
          )
          spec.remoteLineSuffix.set("#L")
        }
      }

      target.dependencies.add("dokkaPlugin", target.libsCatalog.library("dokka-versioning"))

      val versionName = target.VERSION_NAME

      target.tasks.withType(AbstractDokkaTask::class.java).configureEach { task ->

        task.pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
          version = versionName
          olderVersionsDir = target.rootDir.resolve("dokka-archive")
          renderVersionsNavigationOnAllPages = true
        }
      }
    }
  }
}
