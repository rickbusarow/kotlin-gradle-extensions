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
import com.rickbusarow.kgx.javaExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.testing.Test
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugin.devel.plugins.JavaGradlePluginPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel

abstract class GradleTestConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.plugins.applyOnce("idea")

    val gradleTestSourceSet = target.javaExtension
      .sourceSets
      .register(GRADLE_TEST) { ss ->

        val main = target.javaSourceSet(SourceSet.MAIN_SOURCE_SET_NAME)

        ss.compileClasspath += main.output
        ss.runtimeClasspath += main.output

        listOf(
          ss.implementationConfigurationName to main.implementationConfigurationName,
          ss.runtimeOnlyConfigurationName to main.runtimeOnlyConfigurationName
        ).forEach { (gradleTest, mainConfig) ->

          target.configurations.named(gradleTest) {
            it.extendsFrom(target.configurations.getByName(mainConfig))
          }
        }
      }

    // The `compileOnlyApi` configuration is added by the `java-library` plugin,
    // which is applied by the kotlin-jvm plugin.
    target.pluginManager.withPlugin("java-library") {
      val ss = gradleTestSourceSet.get()

      val main = target.javaSourceSet(SourceSet.MAIN_SOURCE_SET_NAME)
      target.configurations.getByName(ss.compileOnlyConfigurationName)
        .extendsFrom(target.configurations.getByName(main.compileOnlyApiConfigurationName))
    }

    // Tells the `java-gradle-plugin` plugin to inject its TestKit logic
    // into the `gradleTest` source set.
    configurePluginTestKitSourceSet(target, gradleTestSourceSet)

    val gradleTestTask = target.tasks
      .register(GRADLE_TEST, Test::class.java) { task ->

        task.group = "verification"
        task.description = "tests the '$GRADLE_TEST' source set"

        val javaSourceSet = target.javaSourceSet(GRADLE_TEST)

        task.testClassesDirs = javaSourceSet.output.classesDirs
        task.classpath = javaSourceSet.runtimeClasspath
        task.inputs.files(javaSourceSet.allSource)

        task.dependsOn(target.rootProject.tasks.named(PUBLISH_TO_BUILD_M2))
      }

    // Make `check` depend upon `gradleTest`
    target.tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME).dependsOn(gradleTestTask)

    // Make the IDE treat `src/gradleTest/[java|kotlin]` as a test source directory.
    target.extensions.configure(IdeaModel::class.java) { idea ->
      idea.module { module ->
        module.testSources.from(
          gradleTestSourceSet.map { it.allSource.srcDirs }
        )
      }
    }
  }

  private fun configurePluginTestKitSourceSet(
    target: Project,
    gradleTestSourceSet: Provider<SourceSet>
  ) {
    target.plugins.withType(JavaGradlePluginPlugin::class.java).configureEach {
      target.extensions.configure(GradlePluginDevelopmentExtension::class.java) { pluginDev ->
        pluginDev.testSourceSets(gradleTestSourceSet.get())
      }
    }
  }

  private fun Project.javaSourceSet(name: String): SourceSet = extensions
    .getByType(JavaPluginExtension::class.java)
    .sourceSets
    .getByName(name)

  companion object {
    private const val GRADLE_TEST = "gradleTest"

    internal const val BUILD_M2 = "buildM2"
    const val PUBLISH_TO_BUILD_M2 = "publishToBuildM2"
  }
}
