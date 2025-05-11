package com.ioffeivan.alarmclock.background.worker.utils

import androidx.work.Data
import com.ioffeivan.alarmclock.core.utils.AlarmClockKeys

class AlarmClockInputDataUtils(private val inputData: Data) {

    fun getId(): Long? {
        val id = inputData.getLong(AlarmClockKeys.ID_KEY, -1L)
        return if (id != -1L) id else null
    }

    fun getHour(defaultValue: Int): Int {
        return inputData.getInt(AlarmClockKeys.HOUR_KEY, defaultValue)
    }

    fun getMinute(defaultValue: Int): Int {
        return inputData.getInt(AlarmClockKeys.MINUTE_KEY, defaultValue)
    }

    fun isEnabled(defaultValue: Boolean): Boolean {
        return inputData.getBoolean(AlarmClockKeys.IS_ENABLED_KEY, defaultValue)
    }

    fun getSoundName(defaultValue: String?): String? {
        return inputData.getString(AlarmClockKeys.SOUND_NAME_KEY) ?: defaultValue
    }

    fun getSoundUri(defaultValue: String?): String? {
        return inputData.getString(AlarmClockKeys.SOUND_URI_KEY) ?: defaultValue
    }

    fun getSoundType(defaultValue: String): String {
        return inputData.getString(AlarmClockKeys.SOUND_TYPE_KEY) ?: defaultValue
    }
}