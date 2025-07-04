package dev.nuculabs.imagetagger.catalog

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image


/**
 * SessionCatalog is a class for managing the current session of the application.
 */
class SessionCatalog {

    /**
     * Represents the currently selected image.
     */
    var selectedImage: ObjectProperty<Image> = SimpleObjectProperty();
}