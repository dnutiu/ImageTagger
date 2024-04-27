plugins {
    id("java")
    kotlin("jvm") version "1.8.22"
    id("org.javamodularity.moduleplugin") version "1.8.12"
}

group = "dev.nuculabs.imagetagger.core"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}