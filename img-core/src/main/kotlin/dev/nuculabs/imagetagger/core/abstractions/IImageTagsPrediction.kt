package dev.nuculabs.imagetagger.core.abstractions

import java.awt.image.BufferedImage
import java.io.InputStream

interface IImageTagsPrediction {
    /**
     * Predicts tags for a Bitmap.
     */
    fun predictTags(image: BufferedImage): List<String>

    /**
     * Predicts tags for a given image input stream.
     */
    fun predictTags(input: InputStream?): List<String>
}