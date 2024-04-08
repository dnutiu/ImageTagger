package dev.nuculabs.imagetagger.ui

import dev.nuculabs.imagetagger.ai.IImageTagsPrediction

/**
 * BasicServiceLocator is implemented to avoid polluting the apps with singletons.
 * It's verbose but gluon-ignite was updated 2 years ago, and I've tried to get it running with
 * Spring, Guice and Dagger, and it got me more trouble than solutions.
 * This basically a compromise for simplicity.
 */
class BasicServiceLocator private constructor() {
    internal lateinit var imageTagsPrediction: IImageTagsPrediction

    // Singleton Pattern
    companion object {
        @Volatile
        private var instance: BasicServiceLocator? = null

        /**
         * Returns a new BasicServiceLocator singleton instance.
         */
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: BasicServiceLocator().also { instance = it }
            }
    }
}