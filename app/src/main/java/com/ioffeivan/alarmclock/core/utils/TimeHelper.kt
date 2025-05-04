package com.ioffeivan.alarmclock.core.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TimeHelper {

    companion object {
        private const val FORMAT = "HH:mm"
        private val dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT, Locale.ENGLISH)

        fun getFormattedCurrentTime(): String {
            return dateTimeFormatter.format(LocalTime.now())
        }

        fun getFormattedTime(time: LocalTime): String {
            return dateTimeFormatter.format(time)
        }
    }
}