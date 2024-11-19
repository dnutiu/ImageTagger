package dev.nuculabs.imagetagger.ui.utils.date

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

interface IDateTimeProvider {
    /**
     * Returns today's short date. For example: 09 April 2024
     */
    fun getTodayShortDate(): String

    fun getTodayTime(): String
}

/**
 * Utilities for providing date and time related values.
 */
class DateTimeProvider : IDateTimeProvider {

    /**
     * Returns today's short date. For example: 09 April 2024
     */
    override fun getTodayShortDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH)
        val date = Date()
        return dateFormat.format(date)
    }

    /**
     * Returns today's short time. Example: 15:30
     */
    override fun getTodayTime(): String {
        val dateFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val date = Date()
        return dateFormat.format(date)
    }
}