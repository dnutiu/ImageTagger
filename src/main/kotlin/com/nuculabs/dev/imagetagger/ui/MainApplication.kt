package com.nuculabs.dev.imagetagger.ui

import com.nuculabs.dev.imagetagger.tag_prediction.ImageTagsPrediction
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MainApplication : Application() {
    override fun start(stage: Stage) {
        val imageTagsPrediction = ImageTagsPrediction.getInstance()

        val fxmlLoader = FXMLLoader(MainApplication::class.java.getResource("hello-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}