package dev.nuculabs.imagetagger.core

import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifIFD0Directory

// TODO: Lens Model, Camera Brand, Camera Model, ISO, Aperture, Shutter Speed

class ImageMetadata internal constructor(private val metadata: Metadata?) {
    val artist: String
        get() {
            if (metadata == null) {
                return "Unknown"
            }
            val exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
            return exifDirectory?.getString(ExifIFD0Directory.TAG_ARTIST) ?: "Unknown"
        }
}
