import org.gradle.api.plugins.quality.Checkstyle
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
  java
  checkstyle
  id("org.springframework.boot") version "4.1.0"
  id("io.spring.dependency-management") version "1.1.7"
  id("org.graalvm.buildtools.native") version "1.1.2"
  id("org.cyclonedx.bom") version "3.2.4"
  id("io.spring.javaformat") version "0.0.47"
  id("io.spring.nohttp") version "0.0.11"
  kotlin("jvm")
  kotlin("plugin.spring")
}

gradle.startParameter.excludedTaskNames.addAll(listOf("checkFormatAot", "checkFormatAotTest"))

group = "org.springframework.samples"
version = "4.0.0-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}

repositories {
  mavenCentral()
  val kotlin_repo_url: String? by project
  kotlin_repo_url?.also { maven { url = uri(it) } }
}

val checkstyleVersion = "12.3.1"
val springJavaformatCheckstyleVersion = "0.0.47"
val webjarsLocatorLiteVersion = "1.1.3"
val webjarsFontawesomeVersion = "4.7.0"
val webjarsBootstrapVersion = "5.3.8"

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xreport-all-warnings",
            "-Xrender-internal-diagnostic-names",
            "-Xuse-fir-experimental-checkers"
        )
        extraWarnings.set(true)
        allWarningsAsErrors.set(false)
        project.findProperty("kotlin_additional_cli_options")?.let {
            freeCompilerArgs.addAll((it as String).trim().split(" ").toList())
        }
        getOverriddenKotlinLanguageVersion(project)?.also { languageVersion.set(KotlinVersion.fromVersion(it)) }
        getOverriddenKotlinApiVersion(project)?.also { apiVersion.set(KotlinVersion.fromVersion(it)) }
    }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-cache")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("javax.cache:cache-api")
  implementation("jakarta.xml.bind:jakarta.xml.bind-api")
  runtimeOnly("org.springframework.boot:spring-boot-starter-actuator")
  runtimeOnly("org.webjars:webjars-locator-lite:$webjarsLocatorLiteVersion")
  runtimeOnly("org.webjars.npm:bootstrap:$webjarsBootstrapVersion")
  runtimeOnly("org.webjars.npm:font-awesome:$webjarsFontawesomeVersion")
  runtimeOnly("com.github.ben-manes.caffeine:caffeine")
  runtimeOnly("com.h2database:h2")
  runtimeOnly("com.mysql:mysql-connector-j")
  runtimeOnly("org.postgresql:postgresql")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
  testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  testImplementation("org.springframework.boot:spring-boot-testcontainers")
  testImplementation("org.springframework.boot:spring-boot-docker-compose")
  testImplementation("org.testcontainers:testcontainers-junit-jupiter")
  testImplementation("org.testcontainers:testcontainers-mysql")
  checkstyle("io.spring.javaformat:spring-javaformat-checkstyle:$springJavaformatCheckstyleVersion")
  checkstyle("com.puppycrawl.tools:checkstyle:$checkstyleVersion")
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}

tasks.named("cyclonedxDirectBom") {
  dependsOn("compileJava")
  setProperty("skipConfigs", listOf(".*[Tt]est.*"))
}

checkstyle {
  configDirectory = project.file("src/checkstyle")
  configFile = file("src/checkstyle/nohttp-checkstyle.xml")
}

tasks.named<Checkstyle>("checkstyleNohttp") {
  configDirectory = project.file("src/checkstyle")
  configFile = file("src/checkstyle/nohttp-checkstyle.xml")
}

tasks.named("formatMain") {
  dependsOn("checkstyleMain")
  dependsOn("checkstyleNohttp")
}

tasks.named("formatTest") {
  dependsOn("checkstyleTest")
  dependsOn("checkstyleNohttp")
}

tasks.named("checkstyleAot") {
  enabled = false
}

tasks.named("checkstyleAotTest") {
  enabled = false
}

tasks.named("checkFormatAot") {
  enabled = false
}

tasks.named("checkFormatAotTest") {
  enabled = false
}

tasks.named("formatAot") {
  enabled = false
}

tasks.named("formatAotTest") {
  enabled = false
}
