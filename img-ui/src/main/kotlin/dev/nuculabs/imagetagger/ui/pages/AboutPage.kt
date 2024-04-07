package dev.nuculabs.imagetagger.ui.pages

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.awt.Desktop
import java.net.URL


class AboutPage {

    @FXML
    fun openBlog() {
        Desktop.getDesktop().browse(URL("https://blog.nuculabs.dev").toURI());
    }

    @FXML
    fun openGithub() {
        Desktop.getDesktop().browse(URL("https://github.com/dnutiu/ImageTagger").toURI());
    }

    companion object {
        fun show() {
            val fxmlLoader = FXMLLoader(AboutPage::class.java.getResource("about-page.fxml"))
            val root = fxmlLoader.load<Any>() as Parent
            val newStage = Stage()
            newStage.title = "About ImageTagger"
            newStage.scene = Scene(root)
            newStage.isResizable = false
            newStage.show()
        }
    }
}