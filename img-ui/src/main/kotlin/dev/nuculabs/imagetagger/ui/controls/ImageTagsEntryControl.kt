package dev.nuculabs.imagetagger.ui.controls

import dev.nuculabs.imagetagger.ui.alerts.ErrorAlert
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.util.logging.Logger


/**
 * This class is used to create a custom control for the image prediction entry.
 */
class ImageTagsEntryControl(private val imagePath: String, predictions: List<String>) : HBox() {
    private val logger: Logger = Logger.getLogger("ImageTagsEntryControl")

    /**
     * The image view for the image prediction entry.
     */
    @FXML
    private lateinit var imageView: ImageView

    /**
     * The text area for the image prediction entry.
     */
    @FXML
    private lateinit var predictedImageTags: TextArea

    /**
     * The file name label.
     */
    @FXML
    private lateinit var fileNameLabel: Label

    init {
        val resource = ImageTagsEntryControl::class.java.getResource("image-tags-entry.fxml")
        logger.fine("Using resource URL: $resource")
        val fxmlLoader = FXMLLoader(resource)
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
        setImage(imagePath)
        setText(predictions)

        setupEventHandlers()
    }

    /**
     * Sets up event handlers.
     */
    private fun setupEventHandlers() {
        // Open image on double-click.
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            if (it.clickCount >= 2) {
                onOpenImageClick()
            }
            it.consume()
        }
    }

    /**
     * Setter for predicted tags text.
     *
     * @param predictions The prediction list.
     */
    private fun setText(predictions: List<String>) {
        predictedImageTags.text = predictions.joinToString { it }
    }

    /**
     * Setter for setting the image.
     */
    private fun setImage(imagePath: String) {
        val file = File(imagePath)
        file.inputStream().use {
            imageView.image = Image(it, 244.0, 244.0, true, true)
            imageView.isCache = true
        }
        fileNameLabel.text = "File: ${file.name}"
    }

    /**
     * Opens the image in the user's default image viewing application.
     * If the operation fails it will display an error alert.
     */
    fun onOpenImageClick() {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(File(imagePath))
            }
        } else {
            logger.severe("Cannot open image $imagePath. Desktop action not supported!")
            ErrorAlert("Can't open file: $imagePath\nOperation is not supported!")
        }
    }
}