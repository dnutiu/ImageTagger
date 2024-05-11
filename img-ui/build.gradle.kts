plugins {
    id("java")
    id("application")
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group "com.nuculabs.dev"
version "1.3"

repositories {
    mavenCentral()
}


tasks.withType(JavaCompile::class.java) {
    options.encoding = "UTF-8"
}

application {
    mainModule = "dev.nuculabs.imagetagger.ui"
    mainClass = "dev.nuculabs.imagetagger.ui.MainPage"
}

kotlin {
    jvmToolchain(17)
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":img-ai"))
    implementation(project(":img-core"))
    implementation("org.controlsfx:controlsfx:11.1.2")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0") {
        exclude("org.openjfx")
    }
    implementation("net.synedra:validatorfx:0.4.0") {
        exclude("org.openjfx")
    }
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-fontawesome5-pack:12.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}

jlink {
    val buildDirectory = layout.buildDirectory.asFile.get().absolutePath
    imageZip = project.file("$buildDirectory/distributions/ImageTagger-${javafx.platform.classifier}.zip")
    options = listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        unixScriptTemplate = project.file("${layout.projectDirectory}/src/main/resources/unixExecutableScriptTemplate.txt")
        name = "ImageTagger"
    }
}

tasks.jlinkZip {
    group = "distribution"
}