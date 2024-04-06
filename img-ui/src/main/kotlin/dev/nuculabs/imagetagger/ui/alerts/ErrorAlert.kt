package dev.nuculabs.imagetagger.ui.alerts

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.layout.Region

class ErrorAlert(var message: String, var show: Boolean = true) : Alert(AlertType.ERROR) {
    init {
        contentText = message
        dialogPane.buttonTypes.add(ButtonType.CLOSE)
        dialogPane.minHeight = Region.USE_PREF_SIZE
        if (show) {
            show()
        }
    }
}