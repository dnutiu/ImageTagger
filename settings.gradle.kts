plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "ImageTagger-Solution"
include("img-ui")
include("img-ai")
include("img-core")

