import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.25"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "id.medihause"
version = "1.1.0-kc26"

repositories { mavenCentral() }

dependencies {
    // Fourni par Keycloak au runtime -> compileOnly
    compileOnly("org.keycloak:keycloak-services:26.0.0")
    compileOnly("org.keycloak:keycloak-server-spi:26.0.0")
    compileOnly("org.keycloak:keycloak-server-spi-private:26.0.0")
    compileOnly("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")

    // Keycloak embarque déjà Jackson -> compileOnly (évite conflits de version)
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")

    // Kotlin, Keycloak ne l’embarque pas -> à inclure dans ton jar final
    implementation(kotlin("stdlib"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

kotlin {
    jvmToolchain(21)
}

tasks {
    val shadowJar by existing(ShadowJar::class) {
        // on n’embarque que Kotlin stdlib (et éventuellement kotlin-reflect si tu en as besoin)
        dependencies {
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib.*"))
        }
        archiveFileName.set("keycloak-totp-api-${project.version}.jar")
    }

    // évite un dependsOn(build) (ça peut boucler / relancer), on préfère :
    shadowJar { dependsOn("classes") }
}
