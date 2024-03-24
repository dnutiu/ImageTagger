package dev.nuculabs.imagetagger.ui

import dev.nuculabs.imagetagger.ai.ImageTagsPrediction
import dev.nuculabs.imagetagger.ui.controls.ImageTagsEntryControl
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ProgressBar
import javafx.scene.control.Separator
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Logger
import javax.imageio.ImageIO


class MainPageController {
    private val logger: Logger = Logger.getLogger("MainPageController")

    /**
     * The thread pool worker pool.
     */
    private val workerPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    /**
     * Number of maximum prediction operations of an image tags that's that is allowed to run in parallel.
     */
    private val maxImagesPredictionInProgress = Runtime.getRuntime().availableProcessors()

    /**
     * Semaphore to limit the maximum amount of predictions submitted to the tread pool.
     */
    private val workerSemaphore: Semaphore = Semaphore(maxImagesPredictionInProgress)

    /**
     * A counter to keep track of the current image prediction.
     */
    private val processedImageFilesCount = AtomicInteger(0)

    /**
     * An integer to keep track of the total image files for a given prediction.
     */
    private var imageFilesTotal = 0

    /**
     * The ImageTagsPrediction service instance.
     */
    private val imageTagsPrediction = ImageTagsPrediction.getInstance()

    /**
     * A boolean that when set to true it will stop the current image tagging operation.
     * When a new operation is started the boolean is reset to false.
     */
    private var isCurrentTagsOperationCancelled: Boolean = false

    @FXML
    private lateinit var progressBar: ProgressBar

    @FXML
    private lateinit var verticalBox: VBox

    @FXML
    private lateinit var cancelButton: Button

    /**
     * Initializes the controller. Needs to be called after the dependencies have been injected.
     */
    fun initialize() {
        HBox.setHgrow(progressBar, Priority.ALWAYS)
        HBox.setHgrow(cancelButton, Priority.ALWAYS)
    }
    /**
     * Prompts the user to select files then predicts tags for the selected image files.
     */
    @FXML
    fun onTagImagesButtonClick() {
        synchronized(this) {
            val fileChooser = FileChooser().apply { title = "Choose images" }
            val filePaths = fileChooser.showOpenMultipleDialog(null) ?: return

            isCurrentTagsOperationCancelled = false
            progressBar.isVisible = true
            cancelButton.isVisible = true
            progressBar.progress = 0.0
            imageFilesTotal = filePaths.count()
            processedImageFilesCount.set(0)
            // Create a new thread to predict the image tags.
            Thread {
                logger.info("Analyzing $imageFilesTotal files")
                filePaths.forEach { filePath ->
                    if (isCurrentTagsOperationCancelled) {
                        logger.info("Cancelling current prediction operation.")
                        return@Thread
                    }
                    workerSemaphore.acquire()
                    workerPool.submit {
                        if (isCurrentTagsOperationCancelled) {
                            logger.info("Image prediction task is cancelled.")
                            workerSemaphore.release()
                            return@submit
                        }

                        predictImageTags(
                            filePath,
                            onError = {
                                workerSemaphore.release()
                            }
                        ) { imagePath, imageTags ->
                            // Add newly predicted tags to UI.
                            Platform.runLater {
                                // Add image and prediction to the view.
                                addNewImagePredictionEntry(imagePath, imageTags)
                                updateProgressBar()
                                workerSemaphore.release()
                            }
                        }
                    }
                }
            }.start()
        }
    }

    /**
     * Cancels the prediction operation.
     */
    @FXML
    fun onCancelTagImagesClick() {
        isCurrentTagsOperationCancelled = true
        progressBar.isVisible = false
        cancelButton.isVisible = false
    }

    /**
     * Predicts an image tags and executes an action with it.
     *
     * @param filePath - The image file's absolute path.
     */
    fun predictImageTags(
        filePath: File,
        onError: (Exception) -> Unit,
        onSuccess: (String, List<String>) -> Unit
    ) {
        try {
            // Get predictions for the image.
            val imageFile = ImageIO.read(File(filePath.absolutePath))
            val tags: List<String> = imageTagsPrediction.predictTags(imageFile)
            onSuccess(filePath.absolutePath, tags)
        } catch (e: Exception) {
            logger.warning("Error while predicting images $e")
            onError(e)
        }
    }

    /**
     * Updates the UI with a new ImagePredictionEntry.
     *
     * @param imagePath - The image path.
     * @param imageTags - The image's tags.
     */
    fun addNewImagePredictionEntry(
        imagePath: String,
        imageTags: List<String>,
    ) {
        verticalBox.children.add(ImageTagsEntryControl(imagePath, imageTags))
        verticalBox.children.add(Separator())
    }

    /**
     * Updates the progress bar of the UI.
     */
    fun updateProgressBar() {
        logger.info("Progress ${processedImageFilesCount.get()}/${imageFilesTotal} ${progressBar.progress}")
        progressBar.progress =
            ((processedImageFilesCount.incrementAndGet() * 100) / imageFilesTotal).toDouble() / 100.0
        if (processedImageFilesCount.get() == imageFilesTotal) {
            progressBar.isVisible = false
            cancelButton.isVisible = false
            logger.info("Finished processing images.")
        }
    }

    /**
     * Shuts down the MainPageController.
     */
    fun shutdown() {
        isCurrentTagsOperationCancelled = true
        workerPool.shutdownNow()
    }
}