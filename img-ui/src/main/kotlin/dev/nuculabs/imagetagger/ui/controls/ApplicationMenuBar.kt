package dev.nuculabs.imagetagger.ui.controls

import dev.nuculabs.imagetagger.ui.MainPageController
import dev.nuculabs.imagetagger.ui.alerts.AboutAlert
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem

/**
 * Used as the application menu bar.
 */
class ApplicationMenuBar(private val mainPageController: MainPageController) : MenuBar() {
    private val fileMenu = Menu("File")
    private val aboutMenu = Menu("About")

    init {
        useSystemMenuBarProperty().set(true)
        menus.addAll(fileMenu, aboutMenu)
        setupFileMenu()
        setupAboutMenu()
    }

    private fun setupAboutMenu() {
        val aboutMenuItem = MenuItem("About")
        aboutMenuItem.setOnAction {
            AboutAlert()
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