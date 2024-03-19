package com.nuculabs.dev.imagetagger.ui

import com.nuculabs.dev.imagetagger.tag_prediction.ImageTagsPrediction
import javafx.fxml.FXML
import javafx.scene.control.Label
import java.io.File

class HelloController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        val imageTagsPrediction = ImageTagsPrediction.getInstance()
        val testTags = imageTagsPrediction.predictTags(File("/home/denis/Pictures/not_in_train/0a1a1e8bafbcdb00d34d87f35f0f4b9f.jpg").inputStream())

        welcomeText.text = testTags.joinToString { it }
    }
}