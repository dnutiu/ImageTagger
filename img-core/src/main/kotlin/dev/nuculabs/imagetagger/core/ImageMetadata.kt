package dev.nuculabs.imagetagger.core

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifIFD0Directory
import java.io.File

// TODO: Lens Model, Camera Brand, Camera Model, ISO, Aperture, Shutter Speed

/**
 * Interface that holds common image metadata objects used by this application.
 */
interface IImageMetadata {
    val artist: String
}

/**
 * An image metadata provider that uses
 */
class ImageMetadata internal constructor(file: File) : IImageMetadata {
    private var metadata: Metadata? = null

    init {
        metadata = ImageMetadataReader.readMetadata(file)
    }

    override val artist: String
        get() {
            if (metadata == null) {
                return "Unknown"
            }
            val exifDirectory = metadata?.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
            return exifDirectory?.getString(ExifIFD0Directory.TAG_ARTIST) ?: "Unknown"
        }
}
