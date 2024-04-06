package dev.nuculabs.imagetagger.ui

import dev.nuculabs.imagetagger.ai.ImageTagsPrediction
import dev.nuculabs.imagetagger.ui.controls.ApplicationMenuBar
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import java.awt.Taskbar
import java.awt.Toolkit
import java.util.logging.Logger

class MainPage : Application() {
    private val logger: Logger = Logger.getLogger("MainPage")
    private var imageTagger: ImageTagsPrediction? = null

    private lateinit var fxmlLoader: FXMLLoader

    private lateinit var mainStage: Stage

    override fun start(stage: Stage) {
        // Initial resource loading
        fxmlLoader = FXMLLoader(MainPage::class.java.getResource("main-window-view.fxml"))
        mainStage = stage

        imageTagger = ImageTagsPrediction.getInstance()
        setUpApplicationIcon()

        // Load the FXML.
        val scene = Scene(fxmlLoader.load(), 640.0, 760.0)

        // Initialize the controller.
        val mainPageController = fxmlLoader.getController<MainPageController>()
        mainPageController.initialize()

        // Set up the stage.
        stage.title = "Image Tagger"
        stage.scene = scene
        stage.minWidth = 640.0
        stage.minHeight = 760.0

        // Add menu bar
        (scene.root as BorderPane).children.add(ApplicationMenuBar(mainPageController))

        stage.show()
    }

    /**
     * Loads and sets up the main application icon.
     */
    private fun setUpApplicationIcon() {
        // Add main icon
        try {
            // This is only needed for MacOS.
            val defaultToolkit = Toolkit.getDefaultToolkit()
            val taskbar = Taskbar.getTaskbar()
            taskbar.iconImage = defaultToolkit.getImage(MainPage::class.java.getResource("image-analysis.png"))
        } catch (e: UnsupportedOperationException) {
            logger.fine("Failed to setup application icon.")
        }

        val mainIcon = Image(MainPage::class.java.getResourceAsStream("image-analysis.png"))
        mainStage.icons.add(mainIcon)
    }

    override fun stop() {
        logger.info("Stop called.")
        val controller = fxmlLoader.getController<MainPageController>()
        controller.shutdown()
        imageTagger?.close()
    }
}

fun main(args: Array<String>) {
    Application.launch(MainPage::class.java, *args)
}