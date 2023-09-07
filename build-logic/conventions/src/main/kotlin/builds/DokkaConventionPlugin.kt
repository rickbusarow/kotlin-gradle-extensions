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
import com.rickbusarow.kgx.dependency
import com.rickbusarow.kgx.libsCatalog
import com.rickbusarow.ktlint.KtLintTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
import org.jetbrains.dokka.gradle.AbstractDokkaTask
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

abstract class DokkaConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {

    target.plugins.applyOnce("org.jetbrains.dokka")

    target.tasks.withType(AbstractDokkaLeafTask::class.java).configureEach { task ->

      // Dokka doesn't support configuration caching
      task.notCompatibleWithConfigurationCache("Dokka doesn't support configuration caching")

      // Dokka uses their outputs but doesn't explicitly depend upon them.
      task.mustRunAfter(target.tasks.withType(KotlinCompile::class.java))
      task.mustRunAfter(target.tasks.withType(KtLintTask::class.java))

      val fullModuleName = target.path.removePrefix(":")
      task.moduleName.set(fullModuleName)

      if (target != target.rootProject) {
        task.dokkaSourceSets.getByName("main") { builder ->

          builder.documentedVisibilities.set(
            setOf(
              DokkaConfiguration.Visibility.PUBLIC,
              DokkaConfiguration.Visibility.PRIVATE,
              DokkaConfiguration.Visibility.PROTECTED,
              DokkaConfiguration.Visibility.INTERNAL,
              DokkaConfiguration.Visibility.PACKAGE
            )
          )

          builder.languageVersion.set(target.KOTLIN_API)
          builder.jdkVersion.set(target.JVM_TARGET_INT)

          // include all project sources when resolving kdoc samples
          builder.samples.setFrom(target.fileTree(target.file("src")))

          val readmeFile = target.file("README.md")

          if (readmeFile.exists()) {
            builder.includes.from(readmeFile)
          }

          builder.sourceLink { sourceLinkBuilder ->
            sourceLinkBuilder.localDirectory.set(target.file("src/main"))

            val modulePath = target.path.replace(":", "/")
              .replaceFirst("/", "")

            // URL showing where the source code can be accessed through the web browser
            sourceLinkBuilder.remoteUrl.set(
              URL(
                "https://github.com/rbusarow/kotlin-gradle-extensions" +
                  "/blob/main/$modulePath/src/main"
              )
            )
            // Suffix which is used to append the line number to the URL. Use #L for GitHub
            sourceLinkBuilder.remoteLineSuffix.set("#L")
          }
        }
      }
    }

    target.dependencies.add("dokkaPlugin", target.libsCatalog.dependency("dokka-versioning"))

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
