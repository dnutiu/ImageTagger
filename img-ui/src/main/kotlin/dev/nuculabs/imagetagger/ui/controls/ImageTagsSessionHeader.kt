package dev.nuculabs.imagetagger.ui.controls

import javafx.fxml.FXMLLoader
import javafx.scene.layout.HBox
import java.io.IOException

class ImageTagsSessionHeader : HBox() {
    init {
        val fxmlLoader = FXMLLoader(
            ImageTagsSessionHeader::class.java.getResource("image-tags-session-header.fxml")
        )
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }
}