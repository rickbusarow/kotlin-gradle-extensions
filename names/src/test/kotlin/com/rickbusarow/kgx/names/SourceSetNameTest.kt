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

package com.rickbusarow.kgx.names

import com.rickbusarow.kgx.names.DomainObjectName.Companion.domainObjectName
import com.rickbusarow.kgx.names.SourceSetName.Companion.addPrefix
import com.rickbusarow.kgx.names.SourceSetName.Companion.addSuffix
import com.rickbusarow.kgx.names.SourceSetName.Companion.anvilConfig
import com.rickbusarow.kgx.names.SourceSetName.Companion.apiConfig
import com.rickbusarow.kgx.names.SourceSetName.Companion.asSourceSetName
import com.rickbusarow.kgx.names.SourceSetName.Companion.compileOnlyConfig
import com.rickbusarow.kgx.names.SourceSetName.Companion.implementationConfig
import com.rickbusarow.kgx.names.SourceSetName.Companion.isTestFixtures
import com.rickbusarow.kgx.names.SourceSetName.Companion.kaptConfig
import com.rickbusarow.kgx.names.SourceSetName.Companion.kspConfig
import com.rickbusarow.kgx.names.SourceSetName.Companion.removePrefix
import com.rickbusarow.kgx.names.SourceSetName.Companion.removeSuffix
import com.rickbusarow.kgx.names.SourceSetName.Companion.runtimeOnlyConfig
import com.rickbusarow.kgx.names.SourceSetName.Companion.sourceSetName
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SourceSetNameTest {
  val fooAndroidTest = SourceSetName("fooAndroidTest")
  val fooCommonMain = SourceSetName("fooCommonMain")
  val fooCommonTest = SourceSetName("fooCommonTest")
  val fooMain = SourceSetName("fooMain")
  val fooTest = SourceSetName("fooTest")

  val androidTestFoo = SourceSetName("androidTestFoo")
  val commonMainFoo = SourceSetName("commonMainFoo")
  val commonTestFoo = SourceSetName("commonTestFoo")
  val mainFoo = SourceSetName("mainFoo")
  val testFoo = SourceSetName("testFoo")

  @Test
  fun `SourceSetName delegate`() {

    val ss by sourceSetName()

    ss.value shouldBe "ss"
  }

  @Test
  fun `isTestFixtures`() {

    SourceSetName.testFixtures.isTestFixtures() shouldBe true
    SourceSetName.main.isTestFixtures() shouldBe false
  }

  @Nested
  inner class `removePrefix functions` {
    @Test
    fun `removePrefix with a String parameter`() {

      val prefix = "foo"

      fooMain.removePrefix(prefix) shouldBe "main".asSourceSetName()
      fooTest.removePrefix(prefix) shouldBe "test".asSourceSetName()
      fooAndroidTest.removePrefix(prefix) shouldBe "androidTest".asSourceSetName()
      fooCommonMain.removePrefix(prefix) shouldBe "commonMain".asSourceSetName()
      fooCommonTest.removePrefix(prefix) shouldBe "commonTest".asSourceSetName()
    }

    @Test
    fun `removePrefix with a SourceSetName parameter`() {

      val prefix = SourceSetName("foo")

      fooMain.removePrefix(prefix) shouldBe "main".asSourceSetName()
      fooTest.removePrefix(prefix) shouldBe "test".asSourceSetName()
      fooAndroidTest.removePrefix(prefix) shouldBe "androidTest".asSourceSetName()
      fooCommonMain.removePrefix(prefix) shouldBe "commonMain".asSourceSetName()
      fooCommonTest.removePrefix(prefix) shouldBe "commonTest".asSourceSetName()
    }

    @Test
    fun `removePrefix with a TaskName parameter`() {

      val prefix = TaskName("foo")

      fooMain.removePrefix(prefix) shouldBe "main".asSourceSetName()
      fooTest.removePrefix(prefix) shouldBe "test".asSourceSetName()
      fooAndroidTest.removePrefix(prefix) shouldBe "androidTest".asSourceSetName()
      fooCommonMain.removePrefix(prefix) shouldBe "commonMain".asSourceSetName()
      fooCommonTest.removePrefix(prefix) shouldBe "commonTest".asSourceSetName()
    }

    @Test
    fun `removePrefix with a ConfigurationName parameter`() {

      val prefix = ConfigurationName("foo")

      fooMain.removePrefix(prefix) shouldBe "main".asSourceSetName()
      fooTest.removePrefix(prefix) shouldBe "test".asSourceSetName()
      fooAndroidTest.removePrefix(prefix) shouldBe "androidTest".asSourceSetName()
      fooCommonMain.removePrefix(prefix) shouldBe "commonMain".asSourceSetName()
      fooCommonTest.removePrefix(prefix) shouldBe "commonTest".asSourceSetName()
    }

    @Test
    fun `removePrefix with an arbitrary DomainObjectName parameter`() {

      val prefix = domainObjectName<String>("foo")

      fooMain.removePrefix(prefix) shouldBe "main".asSourceSetName()
      fooTest.removePrefix(prefix) shouldBe "test".asSourceSetName()
      fooAndroidTest.removePrefix(prefix) shouldBe "androidTest".asSourceSetName()
      fooCommonMain.removePrefix(prefix) shouldBe "commonMain".asSourceSetName()
      fooCommonTest.removePrefix(prefix) shouldBe "commonTest".asSourceSetName()
    }
  }

  @Nested
  inner class `removeSuffix functions` {
    @Test
    fun `removeSuffix with a String parameter`() {

      val suffix = "foo"

      mainFoo.removeSuffix(suffix) shouldBe "main".asSourceSetName()
      testFoo.removeSuffix(suffix) shouldBe "test".asSourceSetName()
      androidTestFoo.removeSuffix(suffix) shouldBe "androidTest".asSourceSetName()
      commonMainFoo.removeSuffix(suffix) shouldBe "commonMain".asSourceSetName()
      commonTestFoo.removeSuffix(suffix) shouldBe "commonTest".asSourceSetName()
    }

    @Test
    fun `removeSuffix with a SourceSetName parameter`() {

      val suffix = SourceSetName("foo")

      mainFoo.removeSuffix(suffix) shouldBe "main".asSourceSetName()
      testFoo.removeSuffix(suffix) shouldBe "test".asSourceSetName()
      androidTestFoo.removeSuffix(suffix) shouldBe "androidTest".asSourceSetName()
      commonMainFoo.removeSuffix(suffix) shouldBe "commonMain".asSourceSetName()
      commonTestFoo.removeSuffix(suffix) shouldBe "commonTest".asSourceSetName()
    }

    @Test
    fun `removeSuffix with a TaskName parameter`() {

      val suffix = TaskName("foo")

      mainFoo.removeSuffix(suffix) shouldBe "main".asSourceSetName()
      testFoo.removeSuffix(suffix) shouldBe "test".asSourceSetName()
      androidTestFoo.removeSuffix(suffix) shouldBe "androidTest".asSourceSetName()
      commonMainFoo.removeSuffix(suffix) shouldBe "commonMain".asSourceSetName()
      commonTestFoo.removeSuffix(suffix) shouldBe "commonTest".asSourceSetName()
    }

    @Test
    fun `removeSuffix with a ConfigurationName parameter`() {

      val suffix = ConfigurationName("foo")

      mainFoo.removeSuffix(suffix) shouldBe "main".asSourceSetName()
      testFoo.removeSuffix(suffix) shouldBe "test".asSourceSetName()
      androidTestFoo.removeSuffix(suffix) shouldBe "androidTest".asSourceSetName()
      commonMainFoo.removeSuffix(suffix) shouldBe "commonMain".asSourceSetName()
      commonTestFoo.removeSuffix(suffix) shouldBe "commonTest".asSourceSetName()
    }

    @Test
    fun `removeSuffix with an arbitrary DomainObjectName parameter`() {

      val suffix = domainObjectName<String>("foo")

      mainFoo.removeSuffix(suffix) shouldBe "main".asSourceSetName()
      testFoo.removeSuffix(suffix) shouldBe "test".asSourceSetName()
      androidTestFoo.removeSuffix(suffix) shouldBe "androidTest".asSourceSetName()
      commonMainFoo.removeSuffix(suffix) shouldBe "commonMain".asSourceSetName()
      commonTestFoo.removeSuffix(suffix) shouldBe "commonTest".asSourceSetName()
    }
  }

  @Nested
  inner class `SourceSetName factory functions` {
    @Test fun `commonJvmTest`() {
      SourceSetName.commonJvmTest shouldBe SourceSetName("commonJvmTest")
    }

    @Test fun `commonJvm`() {
      SourceSetName.commonJvm shouldBe SourceSetName("commonJvm")
    }

    @Test fun `testFixtures`() {
      SourceSetName.testFixtures shouldBe SourceSetName("testFixtures")
    }

    @Test fun `release`() {
      SourceSetName.release shouldBe SourceSetName("release")
    }

    @Test fun `debug`() {
      SourceSetName.debug shouldBe SourceSetName("debug")
    }

    @Test fun `androidTest`() {
      SourceSetName.androidTest shouldBe SourceSetName("androidTest")
    }

    @Test fun `commonMain`() {
      SourceSetName.commonMain shouldBe SourceSetName("commonMain")
    }

    @Test fun `commonTest`() {
      SourceSetName.commonTest shouldBe SourceSetName("commonTest")
    }

    @Test fun `main`() {
      SourceSetName.main shouldBe SourceSetName("main")
    }

    @Test fun `test`() {
      SourceSetName.test shouldBe SourceSetName("test")
    }
  }

  @Nested
  inner class `SourceSetName constants` {
    @Test fun `commonJvmTest`() {
      SourceSetName.commonJvmTest.value shouldBe "commonJvmTest"
    }

    @Test fun `commonJvm`() {
      SourceSetName.commonJvm.value shouldBe "commonJvm"
    }

    @Test fun `testFixtures`() {
      SourceSetName.testFixtures.value shouldBe "testFixtures"
    }

    @Test fun `release`() {
      SourceSetName.release.value shouldBe "release"
    }

    @Test fun `debug`() {
      SourceSetName.debug.value shouldBe "debug"
    }

    @Test fun `androidTest`() {
      SourceSetName.androidTest.value shouldBe "androidTest"
    }

    @Test fun `commonMain`() {
      SourceSetName.commonMain.value shouldBe "commonMain"
    }

    @Test fun `commonTest`() {
      SourceSetName.commonTest.value shouldBe "commonTest"
    }

    @Test fun `main`() {
      SourceSetName.main.value shouldBe "main"
    }

    @Test fun `test`() {
      SourceSetName.test.value shouldBe "test"
    }
  }

  @Nested
  inner class `add prefix` {
    @Test
    fun `addPrefix with a String param returns a String`() {

      SourceSetName.main.addPrefix("foo") shouldBe "fooMain"
      SourceSetName.test.addPrefix("foo") shouldBe "fooTest"
      SourceSetName.androidTest.addPrefix("foo") shouldBe "fooAndroidTest"
      SourceSetName.commonMain.addPrefix("foo") shouldBe "fooCommonMain"
      SourceSetName.commonTest.addPrefix("foo") shouldBe "fooCommonTest"
    }

    @Test
    fun `addPrefix with a TaskName returns a TaskName`() {

      SourceSetName.main.addPrefix(TaskName("foo")) shouldBe TaskName("fooMain")
      SourceSetName.test.addPrefix(TaskName("foo")) shouldBe TaskName("fooTest")
      SourceSetName.androidTest.addPrefix(TaskName("foo")) shouldBe TaskName("fooAndroidTest")
      SourceSetName.commonMain.addPrefix(TaskName("foo")) shouldBe TaskName("fooCommonMain")
      SourceSetName.commonTest.addPrefix(TaskName("foo")) shouldBe TaskName("fooCommonTest")
    }

    @Test
    fun `addPrefix with a ConfigurationName returns a ConfigurationName`() {

      SourceSetName
        .main
        .addPrefix(ConfigurationName("foo")) shouldBe ConfigurationName("fooMain")
      SourceSetName
        .test
        .addPrefix(ConfigurationName("foo")) shouldBe ConfigurationName("fooTest")
      SourceSetName
        .androidTest
        .addPrefix(ConfigurationName("foo")) shouldBe ConfigurationName("fooAndroidTest")
      SourceSetName
        .commonMain
        .addPrefix(ConfigurationName("foo")) shouldBe ConfigurationName("fooCommonMain")
      SourceSetName
        .commonTest
        .addPrefix(ConfigurationName("foo")) shouldBe ConfigurationName("fooCommonTest")
    }

    @Test
    fun `addPrefix with a SourceSetName returns a SourceSetName`() {

      SourceSetName
        .main
        .addPrefix(SourceSetName("foo")) shouldBe fooMain
      SourceSetName
        .test
        .addPrefix(SourceSetName("foo")) shouldBe fooTest
      SourceSetName
        .androidTest
        .addPrefix(SourceSetName("foo")) shouldBe fooAndroidTest
      SourceSetName
        .commonMain
        .addPrefix(SourceSetName("foo")) shouldBe fooCommonMain
      SourceSetName
        .commonTest
        .addPrefix(SourceSetName("foo")) shouldBe fooCommonTest
    }
  }

  @Nested
  inner class `add suffix` {
    @Test
    fun `addSuffix with a String returns a String`() {

      SourceSetName.main.addSuffix("foo") shouldBe "mainFoo"
      SourceSetName.test.addSuffix("foo") shouldBe "testFoo"
      SourceSetName.androidTest.addSuffix("foo") shouldBe "androidTestFoo"
      SourceSetName.commonMain.addSuffix("foo") shouldBe "commonMainFoo"
      SourceSetName.commonTest.addSuffix("foo") shouldBe "commonTestFoo"
    }

    @Test
    fun `addSuffix with a TaskName returns a TaskName`() {

      SourceSetName.main.addSuffix(TaskName("foo")) shouldBe TaskName("mainFoo")
      SourceSetName.test.addSuffix(TaskName("foo")) shouldBe TaskName("testFoo")
      SourceSetName.androidTest.addSuffix(TaskName("foo")) shouldBe TaskName("androidTestFoo")
      SourceSetName.commonMain.addSuffix(TaskName("foo")) shouldBe TaskName("commonMainFoo")
      SourceSetName.commonTest.addSuffix(TaskName("foo")) shouldBe TaskName("commonTestFoo")
    }

    @Test
    fun `addSuffix with a ConfigurationName returns a ConfigurationName`() {

      SourceSetName
        .main
        .addSuffix(ConfigurationName("foo")) shouldBe ConfigurationName("mainFoo")
      SourceSetName
        .test
        .addSuffix(ConfigurationName("foo")) shouldBe ConfigurationName("testFoo")
      SourceSetName
        .androidTest
        .addSuffix(ConfigurationName("foo")) shouldBe ConfigurationName("androidTestFoo")
      SourceSetName
        .commonMain
        .addSuffix(ConfigurationName("foo")) shouldBe ConfigurationName("commonMainFoo")
      SourceSetName
        .commonTest
        .addSuffix(ConfigurationName("foo")) shouldBe ConfigurationName("commonTestFoo")
    }

    @Test
    fun `addSuffix with a SourceSetName returns a SourceSetName`() {

      SourceSetName
        .main
        .addSuffix(SourceSetName("foo")) shouldBe SourceSetName("mainFoo")
      SourceSetName
        .test
        .addSuffix(SourceSetName("foo")) shouldBe SourceSetName("testFoo")
      SourceSetName
        .androidTest
        .addSuffix(SourceSetName("foo")) shouldBe SourceSetName("androidTestFoo")
      SourceSetName
        .commonMain
        .addSuffix(SourceSetName("foo")) shouldBe SourceSetName("commonMainFoo")
      SourceSetName
        .commonTest
        .addSuffix(SourceSetName("foo")) shouldBe SourceSetName("commonTestFoo")
    }
  }

  @Nested
  inner class `base config factory extensions` {
    @Test
    fun `main configs`() {

      SourceSetName.main.apiConfig() shouldBe ConfigurationName.api
      SourceSetName.main.apiConfig().value shouldBe "api"

      SourceSetName.main.compileOnlyConfig() shouldBe ConfigurationName.compileOnly
      SourceSetName.main.compileOnlyConfig().value shouldBe "compileOnly"

      SourceSetName.main.implementationConfig() shouldBe ConfigurationName.implementation
      SourceSetName.main.implementationConfig().value shouldBe "implementation"

      SourceSetName.main.runtimeOnlyConfig() shouldBe ConfigurationName.runtimeOnly
      SourceSetName.main.runtimeOnlyConfig().value shouldBe "runtimeOnly"

      SourceSetName.main.kaptConfig() shouldBe ConfigurationName.kapt
      SourceSetName.main.kaptConfig().value shouldBe "kapt"

      SourceSetName.main.anvilConfig() shouldBe ConfigurationName.anvil
      SourceSetName.main.anvilConfig().value shouldBe "anvil"

      SourceSetName.main.kspConfig() shouldBe ConfigurationName.ksp
      SourceSetName.main.kspConfig().value shouldBe "ksp"
    }

    @Test
    fun `test configs`() {

      SourceSetName.test.apiConfig().value shouldBe "testApi"

      SourceSetName.test.compileOnlyConfig().value shouldBe "testCompileOnly"

      SourceSetName.test.implementationConfig().value shouldBe "testImplementation"

      SourceSetName.test.runtimeOnlyConfig().value shouldBe "testRuntimeOnly"

      SourceSetName.test.kaptConfig().value shouldBe "kaptTest"

      SourceSetName.test.anvilConfig().value shouldBe "anvilTest"

      SourceSetName.test.kspConfig().value shouldBe "kspTest"
    }
  }
}
