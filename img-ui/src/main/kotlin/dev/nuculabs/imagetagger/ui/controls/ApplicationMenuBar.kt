package dev.nuculabs.imagetagger.ui.controls

import dev.nuculabs.imagetagger.ui.BasicServiceLocator
import dev.nuculabs.imagetagger.ui.MainPageController
import dev.nuculabs.imagetagger.ui.pages.AboutPage
import javafx.fxml.FXMLLoader
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import java.io.IOException

/**
 * Used as the application menu bar.
 */
class ApplicationMenuBar : MenuBar() {
    private val fileMenu = Menu("File")
    private val aboutMenu = Menu("About")
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

        initialize()
    }

    private fun initialize() {
        useSystemMenuBarProperty().set(false)
        menus.addAll(fileMenu, aboutMenu)
        setupFileMenu()
        setupAboutMenu()
    }

    private fun setupAboutMenu() {
        val aboutMenuItem = MenuItem("About")
        aboutMenuItem.setOnAction {
            AboutPage.show()
        }
        aboutMenu.items.add(aboutMenuItem)
    }

    private fun setupFileMenu() {
        val tagImagesMenuItem = MenuItem("Tag Images")
        tagImagesMenuItem.setOnAction {
            mainPageController.onTagImagesButtonClick()
        }
        fileMenu.items.add(tagImagesMenuItem)
    }
}