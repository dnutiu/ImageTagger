package dev.nuculabs.imagetagger.catalog

import dev.nuculabs.imagetagger.core.AnalyzedImage
import dev.nuculabs.imagetagger.ui.controls.ImageMetadataPair
import dev.nuculabs.imagetagger.ui.controls.ImageTagsDisplayMode
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.image.Image


/**
 * SessionCatalog is a class for managing the current session of the application.
 */
class SessionCatalog {

    /**
     * Represents the currently selected image.
     */
    var selectedImage: ObjectProperty<Image> = SimpleObjectProperty();
    var selectedAnalyzedImage: ObjectProperty<AnalyzedImage?> = SimpleObjectProperty();
    var imageMetadataEntries: ObjectProperty<ObservableList<ImageMetadataPair>> = SimpleObjectProperty()
    var imagePredictedTags: ObjectProperty<String> = SimpleObjectProperty()
    var tagsDisplayMode: ObjectProperty<String> = SimpleObjectProperty()

    init {
        selectedAnalyzedImage.addListener(ChangeListener { _observable: ObservableValue<out AnalyzedImage?>, _oldValue: AnalyzedImage?, newValue: AnalyzedImage? ->
            if (newValue != null) {
                setMetadata(newValue)
                setPredictedTags()
            }
        })
        tagsDisplayMode.addListener(ChangeListener { _observable: ObservableValue<out String>, _oldValue: String?, newValue: String? ->
            if (newValue != null) {
                setPredictedTags()
            }
        })
    }

    private fun setMetadata(image: AnalyzedImage) {
        val imageMetadata = image.metadata()
        val metadataValues = listOf(
            ImageMetadataPair("Author", imageMetadata.artist),
            ImageMetadataPair("Brand", imageMetadata.cameraBrand),
            ImageMetadataPair("Model", imageMetadata.cameraModel),
            ImageMetadataPair("Lens", imageMetadata.lensModel),
            ImageMetadataPair("ISO", imageMetadata.iso),
            ImageMetadataPair("Aperture", imageMetadata.aperture),
            ImageMetadataPair("Shutter Speed", imageMetadata.shutterSpeed),
        ).filterNot { it.getValue() == "Unknown" }
        val data: ObservableList<ImageMetadataPair> = FXCollections.observableArrayList(
            metadataValues
        )
        imageMetadataEntries.set(data)
    }

    private fun setPredictedTags() {
        if (selectedAnalyzedImage.get() == null) {
            return
        }
        val tags = selectedAnalyzedImage.get()!!.tags()
        val tagsMode = ImageTagsDisplayMode.fromString(tagsDisplayMode.get())
        imagePredictedTags.set(
            when (tagsMode) {
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
        )
    }
}