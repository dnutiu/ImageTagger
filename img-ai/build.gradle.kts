plugins {
    id("java")
    kotlin("jvm") version "1.8.22"
    id("org.javamodularity.moduleplugin") version "1.8.12"

}

group = "dev.nuculabs.imagetagger.ai"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.microsoft.onnxruntime:onnxruntime:1.17.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}