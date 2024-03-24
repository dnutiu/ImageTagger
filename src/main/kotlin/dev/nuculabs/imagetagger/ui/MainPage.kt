package dev.nuculabs.imagetagger.ui

import dev.nuculabs.imagetagger.ai.ImageTagsPrediction
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import java.util.logging.Logger

class MainPage : Application() {
    private val logger: Logger = Logger.getLogger("MainPage")

    private lateinit var fxmlLoader: FXMLLoader

    override fun start(stage: Stage) {
        ImageTagsPrediction.getInstance()
        fxmlLoader = FXMLLoader(MainPage::class.java.getResource("main-window-view.fxml"))
        // Load the FXML.
        val scene = Scene(fxmlLoader.load(), 640.0, 760.0)

        // Initialize the controller.
        fxmlLoader.getController<MainPageController>().initialize()

        // Set up the stage.
        stage.title = "Image Tagger"
        stage.scene = scene
        stage.minWidth = 640.0
        stage.minHeight = 760.0

        // Add main icon
        stage.icons.add(Image(MainPage::class.java.getResourceAsStream("image-analysis.png")));
        stage.show()
    }

    override fun stop() {
        logger.info("Stop called.")
        val controller = fxmlLoader.getController<MainPageController>()
        controller.shutdown()
    }
}

fun main(args: Array<String>) {
    Application.launch(MainPage::class.java, *args)
}