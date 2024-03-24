package dev.nuculabs.imagetagger.ai

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.util.logging.Logger
import javax.imageio.ImageIO

/**
 * ImageTagsPrediction is a specialized class that predicts an Image's tags
 */
class ImageTagsPrediction private constructor() {
    private val logger: Logger = Logger.getLogger("InfoLogging")
    private var ortEnv: OrtEnvironment = OrtEnvironment.getEnvironment()
    private var ortSession: OrtSession
    private var modelClasses: MutableList<String> = mutableListOf()

    init {
        try {
            logger.info("Loaded ML model.")
            ImageTagsPrediction::class.java.getResourceAsStream("/dev/nuculabs/imagetagger/ai/prediction.onnx").let { modelFile ->
                ortSession = ortEnv.createSession(
                    modelFile!!.readBytes(),
                    OrtSession.SessionOptions()
                )
            }
            ImageTagsPrediction::class.java.getResourceAsStream("/dev/nuculabs/imagetagger/ai/prediction_categories.txt")
                .let { classesFile ->
                    modelClasses.addAll(0, classesFile!!.bufferedReader().readLines())
                }
            logger.info("Loaded ${modelClasses.size} model classes.")
        } catch (e: NullPointerException) {
            logger.severe(
                "Failed to load model file or categories file. If you're building the project from " +
                        "source, please follow the instructions from the README.md: " +
                        "https://github.com/dnutiu/ImageTagger." +
                        "Exception ${e.message}"
            )
            throw e
        }
    }

    /**
     * Processes an image into an ONNX Tensor.
     */
    private fun processImage(bufferedImage: BufferedImage): Array<Array<Array<FloatArray>>> {
        try {
            val tensorData = Array(1) {
                Array(3) {
                    Array(224) {
                        FloatArray(224)
                    }
                }
            }
            val mean = floatArrayOf(0.485f, 0.456f, 0.406f)
            val standardDeviation = floatArrayOf(0.229f, 0.224f, 0.225f)

            // crop image to 224x224
            var width: Int = bufferedImage.width
            var height: Int = bufferedImage.height
            var startX = 0
            var startY = 0
            if (width > height) {
                startX = (width - height) / 2
                width = height
            } else {
                startY = (height - width) / 2
                height = width
            }

            val image = bufferedImage.getSubimage(startX, startY, width, height)
            val resizedImage = image.getScaledInstance(224, 224, 4)
            val scaledImage = BufferedImage(224, 224, BufferedImage.TYPE_4BYTE_ABGR)
            scaledImage.graphics.drawImage(resizedImage, 0, 0, null)


            // Process image
            for (y in 0 until scaledImage.height) {
                for (x in 0 until scaledImage.width) {
                    val pixel: Int = scaledImage.getRGB(x, y)

                    // Get RGB values
                    tensorData[0][0][y][x] =
                        ((pixel shr 16 and 0xFF) / 255f - mean[0]) / standardDeviation[0]
                    tensorData[0][1][y][x] =
                        ((pixel shr 16 and 0xFF) / 255f - mean[1]) / standardDeviation[1]
                    tensorData[0][2][y][x] =
                        ((pixel shr 16 and 0xFF) / 255f - mean[2]) / standardDeviation[2]
                }
            }
            return tensorData
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    /**
     * Uses the ML model to predict tags for a given bitmap.
     */
    @Suppress("UNCHECKED_CAST")
    private fun predictTagsInternal(bufferedImage: BufferedImage): List<String> {
        // 1. Get input and output names
        val inputName: String = ortSession.inputNames.iterator().next()
        val outputName: String = ortSession.outputNames.iterator().next()

        // 2. Create input tensor
        val inputTensor = OnnxTensor.createTensor(ortEnv, processImage(bufferedImage))

        // 3. Run the model.
        val inputs = mapOf(inputName to inputTensor)
        val results = ortSession.run(inputs)

        // 4. Get output tensor
        val outputTensor = results.get(outputName)
        if (outputTensor.isPresent) {
            // 5. Get prediction results
            val floatBuffer = outputTensor.get().value as Array<FloatArray>
            val predictions = ArrayList<String>()

            // filter buffer by threshold
            for (i in floatBuffer[0].indices) {
                if (floatBuffer[0][i] > -0.5) {
                    predictions.add(modelClasses[i])
                }
            }

            return predictions
        } else {
            return ArrayList()
        }
    }

    /**
     * Predicts tags for a Bitmap.
     */
    fun predictTags(image: BufferedImage): List<String> {
        return predictTagsInternal(image)
    }

    /**
     * Predicts tags for a given image input stream.
     */
    fun predictTags(input: InputStream?): List<String> {
        if (input == null) {
            return ArrayList()
        }

        return predictTagsInternal(ImageIO.read(input))
    }

    /**
     * Close the session and environment.
     */
    fun close() {
        ortSession.close()
        ortEnv.close()
        modelClasses.clear()
    }

    // Singleton Pattern
    companion object {
        @Volatile
        private var instance: ImageTagsPrediction? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ImageTagsPrediction().also { instance = it }
            }
    }
}