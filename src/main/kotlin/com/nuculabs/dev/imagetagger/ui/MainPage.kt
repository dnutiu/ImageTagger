package com.nuculabs.dev.imagetagger.ui

import com.nuculabs.dev.imagetagger.tag_prediction.ImageTagsPrediction
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MainPage : Application() {
    override fun start(stage: Stage) {
        ImageTagsPrediction.getInstance()

        val fxmlLoader = FXMLLoader(MainPage::class.java.getResource("main-window-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 640.0, 760.0)
        stage.title = "Image Tagger"
        stage.scene = scene
        stage.minWidth = 640.0
        stage.minHeight = 760.0
        stage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(MainPage::class.java, *args)
}