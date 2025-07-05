package dev.nuculabs.imagetagger.ui.controls

import dev.nuculabs.imagetagger.catalog.SessionCatalog
import dev.nuculabs.imagetagger.core.AnalyzedImage
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import java.io.File
import java.io.IOException
import java.util.logging.Logger

class ImageThumbnail(val analyzedImage: AnalyzedImage, private val catalog: SessionCatalog) : HBox() {
    private val logger: Logger = Logger.getLogger("ImageThumbnail")

    /**
     * The image view for the image prediction entry.
     */
    @FXML
    private lateinit var imageView: ImageView

    init {
        val resource = ImageTagsEntryControl::class.java.getResource("image-thumbnail.fxml")
        logger.fine("Using resource URL: $resource")
        val fxmlLoader = FXMLLoader(resource)
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
        setImage()
        setupImageClickHandler()
    }

    /**
     * Setter for setting the image.
     */
    private fun setImage() {
        val file = File(analyzedImage.absolutePath())
        file.inputStream().use {
            imageView.image = Image(it, 244.0, 244.0, true, true)
            imageView.isCache = true
        }
        if (catalog.selectedImage.get() == null) {
            updateCurrentlySelectedImage()
        }
    }

    /**
     * Setup image event click event handler.
     * When clicked update the currently selected image.
     */
    private fun setupImageClickHandler() {
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            updateCurrentlySelectedImage()
            it.consume()
        }
    }

    /**
     * Updates the currently selected image.
     */
    private fun updateCurrentlySelectedImage() {
        val file = File(analyzedImage.absolutePath())
        file.inputStream().use {
            val image = Image(it, 1200.0, 800.0, true, true)
            catalog.selectedAnalyzedImage.set(analyzedImage)
            catalog.selectedImage.set(image)
        }
    }
}