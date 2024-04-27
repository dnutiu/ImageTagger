plugins {
    id("java")
    kotlin("jvm") version "1.8.22"
    id("org.javamodularity.moduleplugin") version "1.8.12"

}

var junitVersion = "5.10.0"
group = "dev.nuculabs.imagetagger.ai"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":img-core"))
    implementation("com.microsoft.onnxruntime:onnxruntime:1.17.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}