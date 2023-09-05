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

import com.rickbusarow.ktlint.KtLintTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Sync
import org.gradle.language.base.plugins.LifecycleBasePlugin
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
              URL("https://github.com/rbusarow/doks/blob/main/$modulePath/src/main")
            )
            // Suffix which is used to append the line number to the URL. Use #L for GitHub
            sourceLinkBuilder.remoteLineSuffix.set("#L")
          }
        }
      }
    }

    target.dependencies.add("dokkaPlugin", target.libsCatalog.dependency("dokka-versioning"))

    val dokkaArchiveDir = target.rootDir.resolve("dokka-archive")

    val versionName = target.VERSION_NAME
    val versionWithoutSnapshot = versionName.removeSuffix("-SNAPSHOT")

    target.tasks.withType(AbstractDokkaTask::class.java).configureEach { task ->

      task.pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
        version = versionName
        olderVersionsDir = target.rootDir.resolve("dokka-archive")
        renderVersionsNavigationOnAllPages = true
      }
    }

    // This creates a file `$rootDir/dokka-archive.zip`.
    //
    // ### before `dokkaHtmlMultiModule`
    //
    // The archive is unzipped into `$rootDir/build/tmp/dokka-archive` and that directory is included as
    // the source for the versioning plugin.
    //
    // ### after `dokkaHtmlMultiModule`
    //
    // If the current project version isn't a snapshot, the new Dokka output is synced into a subfolder of
    // `$rootDir/build/tmp/dokka-archive`.
    // The full `$rootDir/build/tmp/dokka-archive` directory is zipped into `$rootDir/dokka-archive.zip`.
    // The names of each archived version are written to `$rootDir/dokka-archive-versions.txt`.
    if (target == target.rootProject) {

      val disableDokkaArchiveAutoFormat =
        target.tasks.register("disableDokkaArchiveAutoFormat", BuildLogicTask::class.java) { task ->

          task.group = "dokka versioning"
          task.description =
            "writes a simple .editorconfig which disables all formatting in the directory"

          // only run if the `/dokka-archive` directory exists with sub-folders. Each version of Dokka
          // docs would have its own folder like ["/dokka-archive/0.10.0", "/dokka-archive/0.10.1"].
          task.enabled = dokkaArchiveDir.isDirectoryWithFiles { it.isDirectory }

          val editorConfigFile = dokkaArchiveDir.resolve(".editorconfig")

          task.outputs.file(editorConfigFile)

          val relativeFile = editorConfigFile.relativeTo(target.rootDir)

          task.doLast {

            val content = """
              # Disable auto-format for any files copied to /dokka-archive/
              # These are generated by Dokka and the formatting is all over the place,
              # so an auto-format like the one from the IDE commit dialog can create a huge diff.
              # This behavior is also disabled by adding the following to the root .editorconfig:
              #
              # [**/dokka-archive/**]
              # ij_formatter_enabled = false
              #
              root = true
              [*]
              ij_formatter_enabled = false
            """.trimIndent()
              .normaliseLineSeparators()

            if (editorConfigFile.exists()) {

              val actual = editorConfigFile.readText()
                .mapLines { it.trim() }
                .trim()
                .normaliseLineSeparators()

              check(actual == content) {
                val actualIndented = actual.lines().joinToString("\n\t", "\t")
                val expectedIndented = content.lines().joinToString("\n\t", "\t")

                """
                |The ${relativeFile.path} file exists, but with unexpected content.
                |
                |file: file://${editorConfigFile.path}
                |
                |expected content:
                |
                |$expectedIndented
                |
                |actual content:
                |
                |$actualIndented
                |
                |If the existing file is valid, update this task's definition.
                """.replaceIndentByMargin()
              }
            }

            editorConfigFile.parentFile.mkdirs()
            editorConfigFile.writeText(content)
          }
        }

      target.tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME) { task ->
        task.dependsOn(disableDokkaArchiveAutoFormat)
      }

      val syncDokkaToDokkaArchive =
        target.tasks.register("syncDokkaToDokkaArchive", Sync::class.java) { task ->
          task.group = "dokka versioning"
          task.description =
            "sync the Dokka output for the current version to /dokka-archive/$versionWithoutSnapshot"
          task.from(target.rootProject.buildDir().resolve("dokka/htmlMultiModule")) {
            // Don't copy the `older/` directory into the archive, because all navigation is done using
            // the root version's copy.  Archived `older/` directories just waste space.
            it.exclude("older/**")
          }
          task.into(target.rootProject.file("dokka-archive/$versionWithoutSnapshot"))

          task.enabled = versionWithoutSnapshot == versionName

          target.tasks.matchingName("dokkaHtmlMultiModule")
            .forEach { task.mustRunAfter(it) }
          task.dependsOn(disableDokkaArchiveAutoFormat)
        }

      target.tasks.matchingName("dokkaHtmlMultiModule").configureEach { task ->
        task.finalizedBy(syncDokkaToDokkaArchive)
      }
    }
  }
}
