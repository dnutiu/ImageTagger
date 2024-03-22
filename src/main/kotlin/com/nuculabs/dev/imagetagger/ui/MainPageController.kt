package com.nuculabs.dev.imagetagger.ui

import com.nuculabs.dev.imagetagger.tag_prediction.ImageTagsPrediction
import com.nuculabs.dev.imagetagger.ui.controls.ImageTagsEntryControl
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.ProgressBar
import javafx.scene.control.Separator
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import java.io.File
import java.util.logging.Logger
import javax.imageio.ImageIO


class MainPageController {

    private val logger: Logger = Logger.getLogger("MainPageController")

    @FXML
    private lateinit var progressBar: ProgressBar

    @FXML
    private lateinit var verticalBox: VBox

    /**
     * Prompts the user to select files then predicts tags for the selected image files.
     */
    @FXML
    private fun onTagImagesButtonClick() {
        val imageTagsPrediction = ImageTagsPrediction.getInstance()

        val fileChooser = FileChooser()
        fileChooser.title = "Choose images"
        val filePaths = fileChooser.showOpenMultipleDialog(null) ?: return

        progressBar.isVisible = true

        progressBar.progress = 0.0
        // Create a new thread to predict the images.
        val thread = Thread {
            val filePathsTotal = filePaths.count()
            logger.info("Analyzing $filePathsTotal files")
            filePaths.forEachIndexed { index, filePath ->
                try {
                    // Get predictions for the image.
                    val imageFile = ImageIO.read(File(filePath.absolutePath))
                    val tags: List<String> = imageTagsPrediction.predictTags(imageFile)
                    Platform.runLater {
                        // Add image and prediction to the view.
                        verticalBox.children.add(ImageTagsEntryControl(filePath.absolutePath, tags))
                        verticalBox.children.add(Separator())
                        progressBar.progress = (((index + 1) * 100) / filePathsTotal).toDouble() / 100.0
                        logger.info("Progress ${progressBar.progress}")
                    }
                } catch (e: Exception) {
                    logger.warning("Error while predicting images $e")
                }
            }
            Platform.runLater {
                progressBar.isVisible = false
            }
        }
        thread.start()
    }
}