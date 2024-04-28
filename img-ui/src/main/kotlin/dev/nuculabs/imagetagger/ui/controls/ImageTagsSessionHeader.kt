package dev.nuculabs.imagetagger.ui.controls

import dev.nuculabs.imagetagger.ui.alerts.ErrorAlert
import dev.nuculabs.imagetagger.ui.utils.date.DateTimeProvider
import dev.nuculabs.imagetagger.ui.utils.date.IDateTimeProvider
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import org.apache.commons.lang3.SystemUtils
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.util.logging.Logger

class ImageTagsSessionHeader : HBox() {
    private val logger: Logger = Logger.getLogger("ImageTagsSessionHeader")

    /**
     * Is a provider for providing the date-time data for setting the title's header.
     */
    var dateTimeProvider: IDateTimeProvider = DateTimeProvider()

    /**
     * Is the path that will be opened when the open directory button is clicked.
     */
    private var directoryPath: String? = null

    @FXML
    private lateinit var title: Label

    @FXML
    private lateinit var openDirectoryButton: Button

    init {
        val fxmlLoader = FXMLLoader(
            ImageTagsSessionHeader::class.java.getResource("image-tags-session-header.fxml")
        )
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }

        updateHeader(0)
        openDirectoryButton.setOnAction {
            openDirectory()
        }
    }

    /**
     * Updates the header with the number of images and the directory path.
     */
    fun updateHeader(numberOfImages: Int, directoryPath: String? = null) {
        // set directory path and button visibility
        this.directoryPath = directoryPath
        if (this.directoryPath == null) {
            this.openDirectoryButton.isVisible = false
        } else {
            this.openDirectoryButton.isVisible = true
        }

        // update header title
        val shortDate = dateTimeProvider.getTodayShortDate()
        this.title.text = "$shortDate ($numberOfImages Images)"
    }

    /**
     * Opens the directory in the user's Desktop.
     */
    fun openDirectory() {
        directoryPath?.let {
            if (SystemUtils.IS_OS_LINUX) {
                Runtime.getRuntime().exec("xdg-open $directoryPath")
            } else {
                if (Desktop.isDesktopSupported()) {
                    val desktop = Desktop.getDesktop()
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        desktop.open(File(it))
                    } else {
                        logger.severe("Cannot open image directory $it. Desktop action not supported!")
                        ErrorAlert("Can't open file: $it\nOperation is not supported!")
                    }
                } else {
                    logger.severe("Cannot open image directory $it. Desktop action not supported!")
                    ErrorAlert("Can't open file: $it\nOperation is not supported!")
                }
            }
        }
    }
}