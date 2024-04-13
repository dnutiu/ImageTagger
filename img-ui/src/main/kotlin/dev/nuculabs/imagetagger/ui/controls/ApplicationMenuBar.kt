package dev.nuculabs.imagetagger.ui.controls

import dev.nuculabs.imagetagger.ui.BasicServiceLocator
import dev.nuculabs.imagetagger.ui.MainPageController
import dev.nuculabs.imagetagger.ui.pages.AboutPage
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import java.io.IOException

/**
 * Used as the application menu bar.
 */
class ApplicationMenuBar : MenuBar() {
    private val mainPageController: MainPageController by lazy {
        BasicServiceLocator.getInstance().mainPageController
    }

    init {
        // Load component
        val fxmlLoader = FXMLLoader(
            ImageTagsSessionHeader::class.java.getResource("application-menu-bar.fxml")
        )
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }

        useSystemMenuBarProperty().set(false)
    }

    /**
     * Calls the tag images functionality from the main controller.
     * The controller is retrieved in a lazy-like fashion.
     */
    @FXML
    private fun fileMenuTagImages() {
        mainPageController.onTagImagesButtonClick()
    }

    /**
     * Shows the about dialog.
     */
    @FXML
    private fun aboutMenuShowAbout() {
        AboutPage.show()
    }
}