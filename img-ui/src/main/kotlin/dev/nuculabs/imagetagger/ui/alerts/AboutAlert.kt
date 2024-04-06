package dev.nuculabs.imagetagger.ui.alerts

import javafx.scene.control.Alert
import javafx.scene.layout.Region

/**
 * Represents the alert shown when the user clicks on About.
 */
class AboutAlert : Alert(AlertType.INFORMATION) {
    init {
        title = "About ImageTagger"
        headerText = ""
        contentText = "Image Tagger is an application that predicts an image's tags using deep-learning. " +
                "It is useful for photographers who want to improve their workflow by auto-tagging images.\n\n" +
                "Author: Denis-Cosmin Nutiu\n\n" +
                "Website: blog.nuculabs.dev\n" +
                "Github: https://github.com/dnutiu/ImageTagger"
        dialogPane.minHeight = Region.USE_PREF_SIZE
        show()
    }
}