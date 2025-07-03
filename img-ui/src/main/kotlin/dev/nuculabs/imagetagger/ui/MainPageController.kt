package dev.nuculabs.imagetagger.ui

import dev.nuculabs.imagetagger.core.AnalyzedImage
import dev.nuculabs.imagetagger.ui.controls.ImageTagsDisplayMode
import dev.nuculabs.imagetagger.ui.controls.ImageTagsEntryControl
import dev.nuculabs.imagetagger.ui.controls.ImageTagsSessionHeader
import dev.nuculabs.imagetagger.ui.controls.ImageThumbnail
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ProgressBar
import javafx.scene.control.Separator
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Logger


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
     * Semaphore to limit the maximum number of predictions submitted to the tread pool.
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
    private val imageTagsPrediction = BasicServiceLocator.getInstance().imageTagsPrediction

    /**
     * A boolean that when set to, true it will stop the current image tagging operation.
     * When a new operation is started, the boolean is reset to false.
     */
    private var isCurrentTagsOperationCancelled: Boolean = false

    /**
     * Holds a list of predicted images controls that are rendered in the view.
     */
    private var predictedImages: MutableList<ImageTagsEntryControl> = ArrayList()

    /**
     * Controls how image tags are displayed on the screen.
     */
    private var tagsDisplayMode: ImageTagsDisplayMode = ImageTagsDisplayMode.Comma

    @FXML
    private lateinit var progressBar: ProgressBar

    @FXML
    private lateinit var bottomHBox: HBox

    @FXML
    private lateinit var cancelButton: Button

    @FXML
    private lateinit var tagImagesButton: Button

    @FXML
    private lateinit var tagsDisplayModeSelection: ChoiceBox<String>

    /**
     * Initializes the controller. Needs to be called after the dependencies have been injected.
     */
    fun initialize() {
        HBox.setHgrow(progressBar, Priority.ALWAYS)
        HBox.setHgrow(cancelButton, Priority.ALWAYS)

        initializeTagsDisplayMode()
    }

    /**
     * Initializes the tags display mode.
     */
    private fun initializeTagsDisplayMode() {
        // Tags display mode
        tagsDisplayModeSelection.items = FXCollections.observableArrayList(
            ImageTagsDisplayMode.entries.map { it.toString() }
        )
        tagsDisplayModeSelection.value = ImageTagsDisplayMode.default().toString()

        tagsDisplayModeSelection.selectionModel.selectedItemProperty().addListener { _, oldValue, newValue ->
            if (oldValue != newValue && newValue != null) {
                tagsDisplayMode = ImageTagsDisplayMode.fromString(newValue)
                predictedImages.forEach {
                    it.setTagsDisplayMode(tagsDisplayMode)
                }
            }
        }
    }

    /**
     * Prompts the user to select files then predicts tags for the selected image files.
     */
    @FXML
    fun onTagImagesButtonClick() {
        synchronized(this) {
            val fileChooser = FileChooser().apply { title = "Choose images" }
            val filePaths = fileChooser.showOpenMultipleDialog(null) ?: return
            if (tagImagesButton.isDisable) {
                return
            }
            tagImagesButton.isDisable = true
            isCurrentTagsOperationCancelled = false
            progressBar.isVisible = true
            cancelButton.isVisible = true
            progressBar.progress = 0.0
            imageFilesTotal = filePaths.count()
            processedImageFilesCount.set(0)
            // Create a new thread to predict the image tags.
            Thread {
                logger.info("Analyzing $imageFilesTotal files")
                if (filePaths.isNotEmpty()) {
//                    Platform.runLater {
//                        addNewImagePredictionHeader(imageFilesTotal, filePaths.first().parent)
//                    }
                }
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

                        val analyzedImage = AnalyzedImage(filePath, this.imageTagsPrediction)
                        workerSemaphore.release()
                        Platform.runLater {
                            // Add image and prediction to the view.
                            addNewImagePredictionEntry(analyzedImage)
                            updateProgressBar()
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
     * Updates the UI with a new ImagePredictionEntry.
     *
     * @param analyzedImage - The analyzed image instance.
     */
    fun addNewImagePredictionEntry(
        analyzedImage: AnalyzedImage,
    ) {
        // todo: don't add if error. log it
        val control = ImageThumbnail(analyzedImage)
        bottomHBox.children.add(control)
    }

    /**
     * Updates the progress bar of the UI.
     */
    fun updateProgressBar() {
        synchronized(this) {
            logger.info("Progress ${processedImageFilesCount.get()}/${imageFilesTotal} ${progressBar.progress}")
            val processedImages = processedImageFilesCount.incrementAndGet()
            progressBar.progress = ((processedImages * 100) / imageFilesTotal).toDouble() / 100.0
            if (processedImageFilesCount.get() == imageFilesTotal) {
                progressBar.isVisible = false
                cancelButton.isVisible = false
                tagImagesButton.isDisable = false
                logger.info("Finished processing images.")
            }
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