import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.lwjgl.Lwjgl.Addons.`joml 1․10․7`
import org.lwjgl.Lwjgl.Addons.`joml-primitives 1․10․0`
import org.lwjgl.Lwjgl.Module.bgfx
import org.lwjgl.Lwjgl.Module.glfw
import org.lwjgl.Lwjgl.Module.stb
import org.lwjgl.lwjgl

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.lwjgl)
  application
}

group = "net.japanesehunter"
version = "1.0-SNAPSHOT"

application {
  mainClass.set("MainKt")

  if (System.getProperty("os.name").lowercase().contains("mac")) {
    applicationDefaultJvmArgs =
      listOf(
        "-XstartOnFirstThread",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--enable-native-access=ALL-UNNAMED",
      )
  }
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotest.assertions.core)
  testImplementation(libs.kotest.property)

  implementation(libs.kotlin.logging)
  implementation(libs.logback.classic)
  implementation(libs.ktor.io)

  // Arrow
  implementation(libs.arrow.core)
  implementation(libs.arrow.fx.coroutines)
  implementation(libs.arrow.resilience)
  implementation(libs.suspendapp)

  lwjgl {
    implementation(
      bgfx,
      glfw,
      stb,
    )
  }
  implementation(`joml 1․10․7`)
  implementation(`joml-primitives 1․10․0`)
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()

  testLogging {
    showStandardStreams = true

    events =
      setOf(
        TestLogEvent.PASSED,
        TestLogEvent.SKIPPED,
        TestLogEvent.FAILED,
//        TestLogEvent.STANDARD_OUT,
//        TestLogEvent.STANDARD_ERROR,
      )

    exceptionFormat = TestExceptionFormat.FULL
    showExceptions = true
    showCauses = false
    showStackTraces = true
  }
}

kotlin {
  jvmToolchain(21)

  compilerOptions {
    freeCompilerArgs.add("-Xcontext-parameters")
  }
}
