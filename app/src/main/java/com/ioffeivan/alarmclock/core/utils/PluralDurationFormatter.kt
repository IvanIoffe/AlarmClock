package com.ioffeivan.alarmclock.core.utils

import android.content.Context
import com.ioffeivan.alarmclock.R
import java.time.Duration
import java.util.Locale

class PluralDurationFormatter(
    private val context: Context
) {
    private val resources = context.resources

    fun formatDuration(millis: Long): String {
        val duration = Duration.ofMillis(millis).plusMinutes(1)
        val days = duration.toDays().toInt()
        val hours = (duration.toHours() % 24).toInt()
        val minutes = (duration.toMinutes() % 60).toInt()

        return when {
            days > 0 && hours != 0 && minutes != 0 -> {
                String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.duration_format_days_hours_minutes),
                    resources.getQuantityString(R.plurals.plurals_day, days, days),
                    resources.getQuantityString(R.plurals.plurals_hour, hours, hours),
                    resources.getQuantityString(R.plurals.plurals_minute, minutes, minutes),
                )
            }

            days > 0 && hours != 0 -> {
                String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.duration_format_days_hours),
                    resources.getQuantityString(R.plurals.plurals_day, days, days),
                    resources.getQuantityString(R.plurals.plurals_hour, hours, hours),
                )
            }

            days > 0 && minutes != 0 -> {
                String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.duration_format_days_minutes),
                    resources.getQuantityString(R.plurals.plurals_day, days, days),
                    resources.getQuantityString(R.plurals.plurals_minute, minutes, minutes)
                )
            }

            days > 0 -> {
                String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.duration_format_days_only),
                    resources.getQuantityString(R.plurals.plurals_day, days, days),
                )
            }

            hours > 0 && minutes != 0 -> {
                String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.duration_format_hours_minutes),
                    resources.getQuantityString(R.plurals.plurals_hour, hours, hours),
                    resources.getQuantityString(R.plurals.plurals_minute, minutes, minutes),
                )
            }

            hours > 0 -> {
                String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.duration_format_hours_only),
                    resources.getQuantityString(R.plurals.plurals_hour, hours, hours),
                )
            }

            minutes > 1 -> {
                String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.duration_format_minutes_only),
                    resources.getQuantityString(R.plurals.plurals_minute, minutes, minutes),
                )
            }

            else -> {
                String.format(
                    Locale.getDefault(),
                    "%s",
                    context.getString(R.string.duration_format_less_than_minute),
                )
            }
        }
    }

    fun formatDuration(_minutes: Int): String {
        val duration = Duration.ofMinutes(_minutes.toLong())
        val hours = (duration.toHours() % 24).toInt()
        val minutes = _minutes % 60

        return when {
            hours > 0 -> {
                String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.duration_format_hours_only),
                    resources.getQuantityString(R.plurals.plurals_hour, hours, hours),
                )
            }

            else -> {
                String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.duration_format_minutes_only),
                    resources.getQuantityString(R.plurals.plurals_minute, minutes, minutes)
                )
            }
        }
    }
}