@file:Suppress("unused")

package dev.nuculabs.imagetagger.ui.controls

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

/**
 * ImageMetadataPair represents a metadata and it's associated value.
 */
class ImageMetadataPair(metadata: String, value: String) {
    private val metadata: StringProperty = SimpleStringProperty(metadata)
    private val value: StringProperty = SimpleStringProperty(value)

    fun getMetadata(): String {
        return metadata.get()
    }

    fun setMetadata(metadata: String) {
        this.metadata.set(metadata)
    }

    fun metadataProperty(): StringProperty {
        return metadata
    }

    fun getValue(): String {
        return value.get()
    }

    fun setValue(value: String) {
        this.value.set(value)
    }

    fun valueProperty(): StringProperty {
        return value
    }
}