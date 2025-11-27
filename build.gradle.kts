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
    applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
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

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(21)

  compilerOptions {
    freeCompilerArgs.addAll(
      "-Xunused-return-value=full",
      "-Xcontext-parameters",
    )
  }
}
