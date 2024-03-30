package dev.nuculabs.imagetagger.ui.controls
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.TextArea
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import java.io.File
import java.io.IOException
import java.util.logging.Logger


/**
 * This class is used to create a custom control for the image prediction entry.
 */
class ImageTagsEntryControl
    (imagePath: String, predictions: List<String>) : HBox() {
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
    }

    /**
     * Getter for the image view.
     *
     * @param predictions The prediction list.
     */
    fun setText(predictions: List<String>) {
        predictedImageTags.text =  predictions.joinToString { it }
    }

    /**
     * Setter for setting the image.
     */
    fun setImage(imagePath: String) {
        File(imagePath).inputStream().use {
            imageView.image = Image(it, 244.0, 244.0, true, true)
            imageView.isCache = true
        }
    }
}