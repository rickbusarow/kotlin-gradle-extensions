/*
 * Copyright (C) 2024 Rick Busarow
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

package com.rickbusarow.kgx

import com.rickbusarow.kase.KaseMatrix
import com.rickbusarow.kase.files.HasWorkingDir
import com.rickbusarow.kase.files.TestLocation
import com.rickbusarow.kase.gradle.DefaultGradleTestEnvironment
import com.rickbusarow.kase.gradle.DslLanguage.KotlinDsl
import com.rickbusarow.kase.gradle.GradleDependencyVersion
import com.rickbusarow.kase.gradle.GradleRootProjectBuilder
import com.rickbusarow.kase.gradle.GradleTestEnvironmentFactory
import com.rickbusarow.kase.gradle.KaseGradleTest
import com.rickbusarow.kase.gradle.KotlinDependencyVersion
import com.rickbusarow.kase.gradle.TestVersions
import com.rickbusarow.kase.gradle.rootProject
import org.gradle.util.GradleVersion
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import java.io.File

@Execution(ExecutionMode.SAME_THREAD)
abstract class KgxGradleTest(
  override val kaseMatrix: KaseMatrix = KaseMatrix(gradleVersions + kotlinVersions)
) : KaseGradleTest<TestVersions, KgxGradleTestEnvironment, KgxGradleTestEnvironment.Factory> {

  companion object {
    val gradleVersions = setOf(
      "8.4",
      "8.5",
      GradleVersion.current().version
    ).map { GradleDependencyVersion(it) }
    val kotlinVersions = setOf(
      "1.8.22",
      "1.9.22",
      KotlinVersion.CURRENT.toString()
    ).map { KotlinDependencyVersion(it) }
  }
}

class KgxGradleTestEnvironment(
  versions: TestVersions,
  hasWorkingDir: HasWorkingDir,
  rootProject: GradleRootProjectBuilder
) : DefaultGradleTestEnvironment(
  gradleVersion = versions.gradle,
  dslLanguage = KotlinDsl(),
  hasWorkingDir = hasWorkingDir,
  rootProject = rootProject
) {

  class Factory : GradleTestEnvironmentFactory<TestVersions, KgxGradleTestEnvironment> {

    override val localM2Path: File
      get() = BuildConfig.localBuildM2Dir

    override fun createEnvironment(
      params: TestVersions,
      names: List<String>,
      location: TestLocation
    ): KgxGradleTestEnvironment {
      val workingDir = HasWorkingDir(names, location)

      return KgxGradleTestEnvironment(
        versions = params,
        hasWorkingDir = workingDir,
        rootProject = workingDir.rootProject(KotlinDsl()) {
          buildFile(buildFileDefault(params))
          settingsFile(settingsFileDefault(params))
        }
      )
    }
  }
}
