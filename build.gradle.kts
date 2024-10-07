import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.0.0"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "id.medihause"
version = "1.0.1-kc26"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.keycloak:keycloak-services:26.0.0")
    implementation("org.keycloak:keycloak-server-spi:26.0.0")
    implementation("org.keycloak:keycloak-server-spi-private:26.0.0")
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks {
    val shadowJar by existing(ShadowJar::class) {
        dependencies {
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib:2.0.0"))
        }
        dependsOn(build)
        archiveFileName.set("keycloak-totp-api.jar")
    }
}