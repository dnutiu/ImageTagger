package dev.nuculabs.imagetagger.ui.controls

/**
 * Determines how tags are displayed
 */
enum class ImageTagsDisplayMode {
    Comma,
    HashTags,
    Space;

    companion object {
        /**
         * Builds the enum value from a given string.
         *
         * @param value - The string
         * @throws IllegalArgumentException when an invalid value is provided.
         */
        fun fromString(value: String): ImageTagsDisplayMode {
            return when (value) {
                "Comma" -> Comma
                "Space" -> Space
                "HashTags" -> HashTags
                else -> throw IllegalArgumentException("Invalid argument $value")
            }
        }

        /**
         * Returns the default tags display mode.
         */
        fun default(): ImageTagsDisplayMode {
            return Comma
        }
    }
}