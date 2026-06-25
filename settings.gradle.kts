rootProject.name = "spring-petclinic"

pluginManagement {
  val kotlin_repo_url: String? by settings
  repositories {
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
    gradlePluginPortal()
    kotlin_repo_url?.also { maven { url = uri(it) } }
  }

  val kotlin_version: String? by settings
  plugins {
    kotlin("jvm") version (kotlin_version ?: "2.3.0-Beta2")
    kotlin("plugin.spring") version (kotlin_version ?: "2.3.0-Beta2")
  }
}
