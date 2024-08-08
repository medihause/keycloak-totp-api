plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
}

group = "id.medihause"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.keycloak:keycloak-services:25.0.2")
    implementation("org.keycloak:keycloak-server-spi:25.0.2")
    implementation("org.keycloak:keycloak-server-spi-private:25.0.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
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