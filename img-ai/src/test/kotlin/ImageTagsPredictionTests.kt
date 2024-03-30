package dev.nuculabs.imagetagger.ai

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.File
import javax.imageio.ImageIO

class ImageTagsPredictionTests {

    @Test
    fun testPredictTagsForBufferedImage_TimisoaraBega() {
        val timisoaraBega = ImageTagsPredictionTests::class.java.getResource("timisoara-bega.jpg")
        val tags = imageTagsPrediction.predictTags(ImageIO.read(timisoaraBega))
        assertEquals(
            listOf(
                "lake",
                "nature",
                "no people",
                "outdoors",
                "reflection",
                "river",
                "sky",
                "tranquil",
                "tree",
                "water"
            ), tags
        )
    }

    @Test
    fun testPredictTagsForBufferedImage_TimisoaraThrees() {
        val timisoaraBega = ImageTagsPredictionTests::class.java.getResource("timisoara-threes.jpg")
        val tags = imageTagsPrediction.predictTags(ImageIO.read(timisoaraBega))
        assertEquals(
            listOf("day", "forest", "growth", "nature", "no people", "outdoors", "plant", "tree"), tags
        )
    }

    @Test
    fun testPredictTagsForBufferedImage_TimisoaraWaterTower() {
        val timisoaraBega = ImageTagsPredictionTests::class.java.getResource("timisoara-water-tower.jpg")
        val tags = imageTagsPrediction.predictTags(ImageIO.read(timisoaraBega))
        assertEquals(
            listOf(
                "architecture",
                "building exterior",
                "built structure",
                "day",
                "history",
                "no people",
                "outdoors",
                "travel destinations"
            ), tags
        )
    }

    @Test
    fun testPredictTagsForInputStream_TimisoaraBega() {
        val image = ImageTagsPredictionTests::class.java.getResource("timisoara-bega.jpg")
        val tags = imageTagsPrediction.predictTags(File(image!!.toURI()).inputStream())
        assertEquals(
            listOf(
                "lake",
                "nature",
                "no people",
                "outdoors",
                "reflection",
                "river",
                "sky",
                "tranquil",
                "tree",
                "water"
            ), tags
        )
    }

    @Test
    fun testPredictTagsForInputStream__TimisoaraThrees() {
        val image = ImageTagsPredictionTests::class.java.getResource("timisoara-threes.jpg")
        val tags = imageTagsPrediction.predictTags(File(image!!.toURI()).inputStream())
        assertEquals(
            listOf("day", "forest", "growth", "nature", "no people", "outdoors", "plant", "tree"), tags
        )
    }

    @Test
    fun testPredictTagsForInputStream_TimisoaraWaterTower() {
        val image = ImageTagsPredictionTests::class.java.getResource("timisoara-water-tower.jpg")
        val tags = imageTagsPrediction.predictTags(File(image!!.toURI()).inputStream())
        assertEquals(
            listOf(
                "architecture",
                "building exterior",
                "built structure",
                "day",
                "history",
                "no people",
                "outdoors",
                "travel destinations"
            ), tags
        )
    }

    companion object {
        private lateinit var imageTagsPrediction: ImageTagsPrediction
        @JvmStatic
        @BeforeAll
        fun setUp() {
            imageTagsPrediction = ImageTagsPrediction()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            imageTagsPrediction.close()
        }
    }

}