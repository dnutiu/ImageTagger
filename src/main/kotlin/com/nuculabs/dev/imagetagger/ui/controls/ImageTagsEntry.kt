package com.nuculabs.dev.imagetagger.ui.controls
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.TextArea
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import java.io.IOException

/**
 * This class is used to create a custom control for the image prediction entry.
 */
class ImageTagsEntry(imagePath: String, predictions: List<String>) : HBox() {
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
     * Constructor for the image prediction entry.
     *
     * @param imagePath   The image path.
     * @param predictions The prediction list.
     */
    init {
        val fxmlLoader = FXMLLoader(ImageTagsEntry::class.java.getResource("/controls/image-tags-entry.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)

        try {
            fxmlLoader.load<Any>()
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
    fun setImage(imagePath: String?) {
        imageView.image = Image(imagePath)
        imageView.resize(244.0, 244.0)
        imageView.fitHeight = 244.0
        imageView.fitWidth = 244.0
        imageView.isSmooth = true
        imageView.isCache = true
    }
}