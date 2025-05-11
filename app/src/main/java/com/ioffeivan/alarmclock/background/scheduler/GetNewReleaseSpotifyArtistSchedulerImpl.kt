package com.ioffeivan.alarmclock.background.scheduler

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import com.ioffeivan.alarmclock.background.receiver.AlarmClockReceiver
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.utils.Action
import com.ioffeivan.alarmclock.core.utils.AlarmClockKeys
import com.ioffeivan.alarmclock.core.utils.SpotifyKeys
import com.ioffeivan.alarmclock.core.utils.getAlarmClockPendingIntent
import com.ioffeivan.alarmclock.core.utils.getTimeMillisUntilAlarmClock
import javax.inject.Inject

class GetNewReleaseSpotifyArtistSchedulerImpl @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager
) : GetNewReleaseSpotifyArtistScheduler {

    override fun schedule(alarmClock: AlarmClock) {
        val timeUntilAlarmClock = getTimeMillisUntilAlarmClock(alarmClock.time)

        if (timeUntilAlarmClock > 60000) {
            val intent = createScheduleIntent(alarmClock)

            // Получение нового контента Spotify происходит за минуту до срабатывания будильника
            val timeGetNewReleaseSpotifyArtist = System.currentTimeMillis() + timeUntilAlarmClock - 60000
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeGetNewReleaseSpotifyArtist,
                getAlarmClockPendingIntent(context, alarmClock.id, intent)
            )
        }
    }

    override fun cancel(alarmClock: AlarmClock) {
        val intent = createCancelIntent()
        alarmManager.cancel(
            getAlarmClockPendingIntent(context, alarmClock.id, intent)
        )
    }

    private fun createScheduleIntent(alarmClock: AlarmClock): Intent {
        // Здесь такая реализация получения id артиста, чтобы не хранить его в Sound,
        // так как id артиста нужен только для типа звука NEW_RELEASE_SPOTIFY_ARTIST
        // Uri Spotify artist - spotify:artist:<artist_id>
        val spotifyArtistId = alarmClock.sound.uri?.split(":")?.last()

        return Intent(context, AlarmClockReceiver::class.java).apply {
            action = Action.ACTION_GET_NEW_RELEASE_SPOTIFY_ARTIST
            putExtra(AlarmClockKeys.ID_KEY, alarmClock.id)
            putExtra(SpotifyKeys.SPOTIFY_ARTIST_ID_KEY, spotifyArtistId)
        }
    }

    private fun createCancelIntent(): Intent {
        return Intent(context, AlarmClockReceiver::class.java).apply {
            action = Action.ACTION_GET_NEW_RELEASE_SPOTIFY_ARTIST
        }
    }
}