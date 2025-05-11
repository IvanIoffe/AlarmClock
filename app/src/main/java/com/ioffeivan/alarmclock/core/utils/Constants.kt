package com.ioffeivan.alarmclock.core.utils

object Action {
    const val ACTION_START_OR_CANCEL_ALARM_CLOCK =
        "com.ioffeivan.alarmclock.action.START_OR_CANCEL_ALARM_CLOCK"
    const val ACTION_STOP_ALARM_CLOCK = "com.ioffeivan.alarmclock.action.STOP_ALARM_CLOCK"
    const val ACTION_SNOOZE_ALARM_CLOCK = "com.ioffeivan.alarmclock.action.SNOOZE_ALARM_CLOCK"
    const val ACTION_FINISH_ALARM_CLOCK_ACTIVITY =
        "com.ioffeivan.alarmclock.action.FINISH_ALARM_CLOCK_ACTIVITY"
    const val ACTION_GET_NEW_RELEASE_SPOTIFY_ARTIST =
        "com.ioffeivan.alarmclock.action.GET_NEW_RELEASE_SPOTIFY_ARTIST"

    const val ACTION_QUICKBOOT_POWERON = "android.intent.action.QUICKBOOT_POWERON"
    const val ACTION_HTC_QUICKBOOT_POWERON = "com.htc.intent.action.QUICKBOOT_POWERON"
}

object AlarmClockKeys {
    const val ID_KEY = "alarmClockIdKey"
    const val HOUR_KEY = "alarmClockHourKey"
    const val MINUTE_KEY = "alarmClockMinuteKey"
    const val IS_ENABLED_KEY = "alarmClockIsEnabledKey"
    const val SOUND_NAME_KEY = "alarmClockSoundNameKey"
    const val SOUND_TYPE_KEY = "alarmClockSoundTypeKey"
    const val SOUND_URI_KEY = "alarmClockSoundUriKey"
    const val IS_VIBRATE_KEY = "alarmClockIsVibrateKey"
    const val NAME_KEY = "alarmClockNameKey"
}

object SpotifyKeys {
    const val SPOTIFY_ARTIST_ID_KEY = "artistSpotifyIdKey"
}