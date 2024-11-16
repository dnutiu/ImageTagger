plugins {
    id("java")
    kotlin("jvm") version "1.9.25"
    id("org.javamodularity.moduleplugin") version "1.8.12"
}

group = "dev.nuculabs.imagetagger.core"
version = "1.4"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.drewnoakes:metadata-extractor:2.19.0")
}

tasks.test {
    useJUnitPlatform()
}