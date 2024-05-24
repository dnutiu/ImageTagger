package dev.nuculabs.imagetagger.core

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifIFD0Directory
import com.drew.metadata.exif.ExifSubIFDDirectory
import java.io.File

/**
 * Interface that holds common image metadata objects used by this application.
 */
interface IImageMetadata {
    val artist: String
    val aperture: String
    val shutterSpeed: String
    val iso: String
    val lensModel: String
    val cameraModel: String
    val cameraBrand: String
}

/**
 * An image metadata provider that uses
 */
class ImageMetadata internal constructor(file: File) : IImageMetadata {
    private var metadata: Metadata? = null
    private lateinit var _cameraBrand: String
    private lateinit var _cameraModel: String
    private lateinit var _lensModel: String
    private lateinit var _artist: String
    private lateinit var _aperture: String
    private lateinit var _iso: String
    private lateinit var _shutterSpeed: String

    init {
        metadata = ImageMetadataReader.readMetadata(file)
        if (metadata != null) {
            val exifDirectory = metadata?.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
            val exifSubDirectory = metadata?.getFirstDirectoryOfType(ExifSubIFDDirectory::class.java)

            _cameraBrand = exifDirectory?.getString(ExifIFD0Directory.TAG_MAKE) ?: "Unknown"
            _cameraModel = exifDirectory?.getString(ExifIFD0Directory.TAG_MODEL) ?: "Unknown"
            _lensModel = exifSubDirectory?.getString(ExifIFD0Directory.TAG_LENS_MODEL) ?: "Unknown"
            _artist = exifDirectory?.getString(ExifIFD0Directory.TAG_ARTIST) ?: "Unknown"
            _aperture = exifSubDirectory?.getString(ExifIFD0Directory.TAG_FNUMBER) ?: "Unknown"
            _iso = exifSubDirectory?.getString(ExifIFD0Directory.TAG_ISO_EQUIVALENT) ?: "Unknown"
            _shutterSpeed = exifSubDirectory?.getString(ExifIFD0Directory.TAG_EXPOSURE_TIME) ?: "Unknown"
        }

    }

    override val cameraBrand: String
        get() {
            if (metadata == null) {
                return "Unknown"
            }
            return _cameraBrand
        }

    override val cameraModel: String
        get() {
            if (metadata == null) {
                return "Unknown"
            }
            return _cameraModel
        }

    override val lensModel: String
        get() {
            if (metadata == null) {
                return "Unknown"
            }
            return _lensModel
        }

    override val artist: String
        get() {
            if (metadata == null) {
                return "Unknown"
            }
            return _artist
        }

    override val aperture: String
        get() {
            if (metadata == null) {
                return "Unknown"
            }
            return _aperture
        }

    override val iso: String
        get() {
            if (metadata == null) {
                return "Unknown"
            }
            return _iso
        }

    override val shutterSpeed: String
        get() {
            if (metadata == null) {
                return "Unknown"
            }
            return _shutterSpeed
        }

    override fun toString(): String {
        return "ImageMetadata(cameraBrand='$cameraBrand', cameraModel='$cameraModel', lensModel='$lensModel', artist='$artist', aperture='$aperture', iso='$iso', shutterSpeed='$shutterSpeed')"
    }
}
