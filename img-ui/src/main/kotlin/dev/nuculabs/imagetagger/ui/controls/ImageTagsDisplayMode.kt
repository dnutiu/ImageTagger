package dev.nuculabs.imagetagger.ui.controls

/**
 * Determines how tags are displayed
 */
enum class ImageTagsDisplayMode {
    CommaSeparated {
        override fun toString(): String {
            return "Comma Separated"
        }
    },
    HashTags;

    companion object {
        fun fromString(value: String): ImageTagsDisplayMode {
            return when (value) {
                CommaSeparated.toString() -> CommaSeparated
                "HashTags" -> HashTags
                else -> throw IllegalArgumentException("Invalid argument $value")
            }
        }
    }
}