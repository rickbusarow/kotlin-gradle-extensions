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

import builds.GradleTestConventionPlugin.Companion.PUBLISH_TO_BUILD_M2
import com.rickbusarow.kgx.dependsOn
import com.rickbusarow.kgx.extras
import com.rickbusarow.kgx.getOrNullAs
import com.rickbusarow.kgx.registerOnce
import com.vanniktech.maven.publish.GradlePlugin
import com.vanniktech.maven.publish.JavadocJar.Dokka
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost.Companion.DEFAULT
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.api.tasks.bundling.Jar
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.plugins.signing.Sign
import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask

interface PublishingExtension {
  fun Project.published(
    artifactId: String,
    pomDescription: String
  ) {
    published(
      groupId = GROUP,
      artifactId = artifactId,
      pomDescription = pomDescription
    )
  }

  fun Project.published(
    groupId: String,
    artifactId: String,
    pomDescription: String
  ) {

    plugins.apply("com.vanniktech.maven.publish.base")
    plugins.apply("builds.dokka")

    require(pluginManager.hasPlugin("org.jetbrains.kotlin.jvm"))

    configurePublish(
      artifactId = artifactId,
      pomDescription = pomDescription,
      groupId = groupId
    )
  }
}

private fun Project.configurePublish(
  artifactId: String,
  pomDescription: String,
  groupId: String
) {

  version = VERSION_NAME
  group = groupId

  @Suppress("UnstableApiUsage")
  extensions.configure(MavenPublishBaseExtension::class.java) { extension ->

    extension.publishToMavenCentral(DEFAULT, automaticRelease = true)

    extension.signAllPublications()

    @Suppress("UnstableApiUsage")
    extension.pom { mavenPom ->
      mavenPom.description.set(pomDescription)
      mavenPom.name.set(artifactId)

      mavenPom.url.set("https://www.github.com/rickbusarow/kotlin-gradle-extensions/")

      mavenPom.licenses { licenseSpec ->
        licenseSpec.license { license ->
          license.name.set("The Apache Software License, Version 2.0")
          license.url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
          license.distribution.set("repo")
        }
      }
      mavenPom.scm { scm ->
        scm.url.set("https://github.com/rickbusarow/kotlin-gradle-extensions/")
        scm.connection.set("scm:git:git://github.com/rickbusarow/kotlin-gradle-extensions.git")
        scm.developerConnection.set(
          "scm:git:ssh://git@github.com/rickbusarow/kotlin-gradle-extensions.git"
        )
      }
      mavenPom.developers { developerSpec ->
        developerSpec.developer { developer ->
          developer.id.set("rickbusarow")
          developer.name.set("Rick Busarow")
          developer.url.set("https://github.com/rickbusarow/")
        }
      }
    }

    when {
      // The plugin-publish plugin handles its artifacts
      pluginManager.hasPlugin("com.gradle.plugin-publish") -> {}

      // handle publishing plugins if they're not going to the plugin portal
      pluginManager.hasPlugin("java-gradle-plugin") -> {
        extension.configure(
          GradlePlugin(
            javadocJar = Dokka(taskName = "dokkaHtml"),
            sourcesJar = true
          )
        )
      }

      pluginManager.hasPlugin("com.github.johnrengelman.shadow") -> {
        extension.configure(
          KotlinJvm(javadocJar = Dokka(taskName = "dokkaHtml"), sourcesJar = true)
        )
        applyBinaryCompatibility()
      }

      else -> {
        extension.configure(
          KotlinJvm(javadocJar = Dokka(taskName = "dokkaHtml"), sourcesJar = true)
        )
        applyBinaryCompatibility()
      }
    }

    extensions.configure(PublishingExtension::class.java) { publishingExtension ->
      publishingExtension
        .publications
        .withType(MavenPublication::class.java)
        .configureEach { publication ->
          publication.artifactId = artifactId
          publication.pom.description.set(pomDescription)
          publication.groupId = groupId
        }
      publishingExtension.repositories.mavenLocal {
        it.name = GradleTestConventionPlugin.BUILD_M2
        it.setUrl(rootProject.layout.buildDirectory.dir("m2"))
      }
    }
  }

  registerCoordinatesStringsCheckTask(groupId = groupId, artifactId = artifactId)
  registerSnapshotVersionCheckTask()

  tasks.withType(PublishToMavenRepository::class.java).configureEach {
    it.notCompatibleWithConfigurationCache("See https://github.com/gradle/gradle/issues/13468")
  }
  tasks.withType(Jar::class.java).configureEach {
    it.notCompatibleWithConfigurationCache("")
  }
  tasks.withType(Sign::class.java).configureEach {
    it.notCompatibleWithConfigurationCache("")
    // skip signing for -SNAPSHOT publishing
    it.onlyIf { !(version as String).endsWith("SNAPSHOT") }
  }

  setUpPublishToBuildM2(this)
}

/**
 * Registers this [target]'s version of the `publishToBuildM2`
 * task and adds it as a dependency to the root project's version.
 */
private fun setUpPublishToBuildM2(target: Project) {

  val publishToBuildM2 = target.tasks.register(PUBLISH_TO_BUILD_M2) {
    it.group = "Publishing"
    it.description = "Delegates to the publishAllPublicationsToBuildM2Repository task " +
      "on projects where publishing is enabled."

    it.dependsOn("publishAllPublicationsToBuildM2Repository")

    // Don't generate javadoc for integration tests.
    target.extras["skipDokka"] = true
  }

  target.rootProject.tasks.named(PUBLISH_TO_BUILD_M2).dependsOn(publishToBuildM2)

  target.tasks.withType(AbstractDokkaLeafTask::class.java).configureEach {
    it.enabled = !target.skipDokka
  }
}

private val Project.skipDokka: Boolean
  get() = extras.getOrNullAs<Boolean>("skipDokka") ?: false

private fun Project.registerCoordinatesStringsCheckTask(
  groupId: String,
  artifactId: String
) {

  val checkTask = tasks.registerOnce(
    "checkMavenCoordinatesStrings",
    BuildLogicTask::class.java
  ) { task ->
    task.group = "publishing"
    task.description = "checks that the project's maven group and artifact ID are valid for Maven"

    task.doLast {

      val allowedRegex = "^[A-Za-z0-9_\\-.]+$".toRegex()

      check(groupId.matches(allowedRegex)) {

        val actualString = when {
          groupId.isEmpty() -> "<<empty string>>"
          else -> groupId
        }
        "groupId ($actualString) is not a valid Maven identifier ($allowedRegex)."
      }

      check(artifactId.matches(allowedRegex)) {

        val actualString =
          when {
            artifactId.isEmpty() -> "<<empty string>>"
            else -> artifactId
          }
        "artifactId ($actualString) is not a valid Maven identifier ($allowedRegex)."
      }
    }
  }

  tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME) { task ->
    task.dependsOn(checkTask)
  }
}

private fun Project.registerSnapshotVersionCheckTask() {
  tasks.registerOnce(
    "checkVersionIsSnapshot",
    BuildLogicTask::class.java
  ) { task ->
    task.group = "publishing"
    task.description = "ensures that the project version has a -SNAPSHOT suffix"
    val versionString = version as String
    task.doLast {
      val expected = "-SNAPSHOT"
      require(versionString.endsWith(expected)) {
        "The project's version name must be suffixed with `$expected` when checked in" +
          " to the main branch, but instead it's `$versionString`."
      }
    }
  }
  tasks.registerOnce("checkVersionIsNotSnapshot", BuildLogicTask::class.java) { task ->
    task.group = "publishing"
    task.description = "ensures that the project version does not have a -SNAPSHOT suffix"
    val versionString = version as String
    task.doLast {
      require(!versionString.endsWith("-SNAPSHOT")) {
        "The project's version name cannot have a -SNAPSHOT suffix, but it was $versionString."
      }
    }
  }
}
