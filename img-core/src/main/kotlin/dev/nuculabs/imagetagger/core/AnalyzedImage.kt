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

    private var bufferedImage: BufferedImage? = null
    private var predictedTags: List<String> = emptyList()
    private val logger: Logger = Logger.getLogger("AnalyzedImage")
    private var error: String = ""

    /**
     * Initializes the analyzed image and predicts its tags.
     */
    init {
        try {
            bufferedImage = ImageIO.read(File(file.absolutePath))
            predictedTags = imageTagsPrediction.predictTags(bufferedImage!!)
        } catch (e: Exception) {
            logger.warning("Error while predicting images $e")
            error = e.message.toString()
        }
    }

    /**
     * Returns the prediction error
     */
    fun errorMessage(): String {
        return error;
    }

    /**
     * Returns the absolute file path of the image.
     */
    fun absolutePath(): String {
        return file.absolutePath;
    }

    /**
     * Returns the predicted tags.
     */
    fun tags(): List<String> {
        return predictedTags
    }
}