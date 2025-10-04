package dev.nuculabs.imagetagger.core

import dev.nuculabs.imagetagger.core.abstractions.IImageTagsPrediction
import java.awt.image.BufferedImage
import java.io.File
import java.util.logging.Logger
import javax.imageio.ImageIO


/**
 * AnalyzedImage represents an Analyzed Image inside the ImageTagger application.
 */
class AnalyzedImage(private val file: File, imageTagsPrediction: IImageTagsPrediction) {

    private var imageFile: File = file
    private var bufferedImage: BufferedImage? = null
    private var predictedTags: List<String> = emptyList()
    private val logger: Logger = Logger.getLogger("AnalyzedImage")
    private lateinit var imageMetadata: IImageMetadata
    private var error: String = ""
    private var hasError: Boolean = false

    /**
     * Initializes the analyzed image and predicts its tags.
     */
    init {
        try {
            imageFile = File(file.absolutePath)
            bufferedImage = ImageIO.read(imageFile)
            predictedTags = imageTagsPrediction.predictTags(bufferedImage!!)
            imageMetadata = ImageMetadata(imageFile)
        } catch (e: NullPointerException) {
            val message = "Error while predicting image: invalid image type or type not supported."
            logger.warning(message)
            hasError = true
            error = message
        } catch (e: Exception) {
            val message = "Error loading image $e"
            logger.warning(message)
            hasError = true
            error = e.message.toString()
        }
    }

    /**
     * Returns an image's metadata
     */
    fun metadata(): IImageMetadata {
        return imageMetadata
    }

    /**
     * Returns a boolean indicating if the image analysis has errors.
     * The flag is `True` if it has errors, `False` otherwise.
     */
    fun hasError(): Boolean {
        return hasError
    }

    /**
     * Returns the prediction error
     */
    fun errorMessage(): String {
        return error
    }

    /**
     * Returns the absolute file path of the image.
     */
    fun absolutePath(): String {
        return file.absolutePath
    }

    /**
     * Returns the predicted tags.
     */
    fun tags(): List<String> {
        return predictedTags
    }

    /**
     * Returns the file name of the image.
     */
    fun fileName(): String {
        return file.name
    }
}