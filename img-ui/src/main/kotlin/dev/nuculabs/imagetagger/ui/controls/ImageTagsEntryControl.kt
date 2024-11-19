package dev.nuculabs.imagetagger.ui.controls

import dev.nuculabs.imagetagger.core.AnalyzedImage
import dev.nuculabs.imagetagger.ui.alerts.ErrorAlert
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import org.apache.commons.lang3.SystemUtils
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.logging.Logger


/**
 * This class is used to create a custom control for the image prediction entry.
 */
class ImageTagsEntryControl(private val image: AnalyzedImage) : HBox() {
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
     * Holds the tags.
     */
    private var tags: List<String> = ArrayList()

    /**
     * Sets the default image tags display mode.
     */
    private var tagsDisplayMode: ImageTagsDisplayMode = ImageTagsDisplayMode.Comma

    /**
     * The file name label.
     */
    @FXML
    private lateinit var fileNameLabel: Label

    /**
     * The copy tags button.
     */
    @FXML
    private lateinit var copyTagsButton: Button

    /**
     * Metadata related UI fields.
     */
    @FXML
    private lateinit var metadataVbox: VBox

    @FXML
    private lateinit var metadataTableView: TableView<ImageTagsEntryModel>

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
        setImage(image)
        if (image.hasError()) {
            setTags(listOf(image.errorMessage()))
            metadataVbox.isVisible = false
        } else {
            setTags(image.tags())
            setMetadata()
        }


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
        // Copy Tags Button
        copyTagsButton.setOnMouseClicked {
            onCopyTagsClick()
        }
    }

    /**
     * Setter for predicted tags text.
     *
     * @param predictions The prediction list.
     */
    fun setTags(predictions: List<String>) {
        tags = predictions
        updateTags()
    }

    /**
     * Sets the tags display mode.
     *
     * @param mode The image tags display mode.
     */
    fun setTagsDisplayMode(mode: ImageTagsDisplayMode) {
        tagsDisplayMode = mode
        updateTags()
    }

    /**
     * Updates the tags text.
     */
    private fun updateTags() {
        predictedImageTags.text = when (tagsDisplayMode) {
            ImageTagsDisplayMode.Comma -> {
                tags.joinToString { it }
            }
            ImageTagsDisplayMode.HashTags -> {
                tags.joinToString(separator = " ") {
                    "#${it}"
                }
            }
            ImageTagsDisplayMode.Space -> {
                tags.joinToString(separator = " ") { it }
            }
        }
    }

    /**
     * Sets and displays the image metadata.
     */
    private fun setMetadata() {
        val imageMetadata = image.metadata()
        val metadataValues = listOf(
            ImageTagsEntryModel("Author", imageMetadata.artist),
            ImageTagsEntryModel("Brand", imageMetadata.cameraBrand),
            ImageTagsEntryModel("Model", imageMetadata.cameraModel),
            ImageTagsEntryModel("Lens", imageMetadata.lensModel),
            ImageTagsEntryModel("ISO", imageMetadata.iso),
            ImageTagsEntryModel("Aperture", imageMetadata.aperture),
            ImageTagsEntryModel("Shutter Speed", imageMetadata.shutterSpeed),
        ).filterNot { it.getValue() == "Unknown" }
        val data: ObservableList<ImageTagsEntryModel> = FXCollections.observableArrayList(
            metadataValues
        )
        if (data.size == 0) {
            metadataVbox.isVisible = false
            return
        }
        metadataTableView.items = data

    }


    /**
     * Setter for setting the image.
     */
    private fun setImage(analyzedImage: AnalyzedImage) {
        val file = File(analyzedImage.absolutePath())
        if (analyzedImage.hasError()) {
            fileNameLabel.text = "Failed: ${file.name}"
            fileNameLabel.styleClass.add("errorLabel")
            imageView.image = Image(this.javaClass.getResourceAsStream("images/failed.png"))
            imageView.isCache = true
        } else {
            file.inputStream().use {
                imageView.image = Image(it, 244.0, 244.0, true, true)
                imageView.isCache = true
            }
            fileNameLabel.text = "File: ${file.name}"
        }
    }

    /**
     * Opens the image in the user's default image viewing application.
     * If the operation fails it will display an error alert.
     */
    fun onOpenImageClick() {
        if (image.hasError()) {
            return
        }
        if (SystemUtils.IS_OS_LINUX) {
            val status = Runtime.getRuntime().exec(arrayOf("xdg-open", image.absolutePath()))
            logger.fine(InputStreamReader(status.errorStream).readText())
        } else {
            if (Desktop.isDesktopSupported()) {
                val desktop = Desktop.getDesktop()
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(File(image.absolutePath()))
                }
            } else {
                logger.severe("Cannot open image ${image.absolutePath()}. Desktop action not supported!")
                ErrorAlert("Can't open file: ${image.absolutePath()}\nOperation is not supported!")
            }
        }
    }

    /**
     * Copies the image tags to the system clipboard.
     */
    fun onCopyTagsClick() {
        val clipboard: Clipboard = Clipboard.getSystemClipboard()
        val content = ClipboardContent()
        content.putString(predictedImageTags.text)
        clipboard.setContent(content)
    }
}