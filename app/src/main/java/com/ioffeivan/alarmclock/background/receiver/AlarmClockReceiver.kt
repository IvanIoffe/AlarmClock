package com.ioffeivan.alarmclock.background.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ioffeivan.alarmclock.background.service.PlayAlarmClockSoundService
import com.ioffeivan.alarmclock.background.worker.GetNewReleaseSpotifyArtistWorker
import com.ioffeivan.alarmclock.background.worker.ScheduleAlarmClockWorker
import com.ioffeivan.alarmclock.background.worker.SnoozeAlarmClockWorker
import com.ioffeivan.alarmclock.background.worker.UpdateAlarmClockInDatabaseWorker
import com.ioffeivan.alarmclock.background.worker.utils.WorkRequestTags
import com.ioffeivan.alarmclock.core.utils.Action
import com.ioffeivan.alarmclock.core.utils.AlarmClockKeys
import com.ioffeivan.alarmclock.core.utils.SpotifyKeys
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmClockReceiver : BroadcastReceiver() {

    @Inject
    lateinit var workManager: WorkManager

    override fun onReceive(context: Context, intent: Intent?) {
        val alarmClockId =
            intent?.getLongExtra(AlarmClockKeys.ID_KEY, -1) ?: return

        when (intent.action) {
            Action.ACTION_START_OR_CANCEL_ALARM_CLOCK -> {
                val alarmClockHour = intent.getStringExtra(AlarmClockKeys.HOUR_KEY)
                val alarmClockMinute = intent.getStringExtra(AlarmClockKeys.MINUTE_KEY)
                val alarmClockSoundType = intent.getStringExtra(AlarmClockKeys.SOUND_TYPE_KEY)
                val alarmClockSoundUri = intent.getStringExtra(AlarmClockKeys.SOUND_URI_KEY)
                val alarmClockIsVibrate = intent.getBooleanExtra(AlarmClockKeys.IS_VIBRATE_KEY, false)
                val alarmClockName = intent.getStringExtra(AlarmClockKeys.NAME_KEY)

                stopPlayAlarmClockSoundService(context)
                sendBroadcastForFinishActivity(context)
                startForegroundService(
                    context,
                    alarmClockId,
                    alarmClockHour,
                    alarmClockMinute,
                    alarmClockSoundType,
                    alarmClockSoundUri,
                    alarmClockIsVibrate,
                    alarmClockName,
                )
                workManager.enqueue(updateAlarmClockRequest(alarmClockId))
                workManager.cancelAllWorkByTag(WorkRequestTags.GET_NEW_RELEASE_SPOTIFY_ARTIST_REQUEST_TAG)
            }

            Action.ACTION_SNOOZE_ALARM_CLOCK -> {
                workManager
                    .beginWith(snoozeAlarmClockRequest(alarmClockId))
                    .then(updateAlarmClockRequest(alarmClockId))
                    .enqueue()

                stopPlayAlarmClockSoundService(context)
                sendBroadcastForFinishActivity(context)
            }

            Action.ACTION_STOP_ALARM_CLOCK -> {
                stopPlayAlarmClockSoundService(context)
                sendBroadcastForFinishActivity(context)
            }

            Action.ACTION_GET_NEW_RELEASE_SPOTIFY_ARTIST -> {
                val spotifyArtistId =
                    intent.getStringExtra(SpotifyKeys.SPOTIFY_ARTIST_ID_KEY) ?: return

                workManager
                    .beginWith(getNewReleaseSpotifyArtistRequest(spotifyArtistId))
                    .then(scheduleAlarmClockRequest(alarmClockId))
                    .then(updateAlarmClockRequest(alarmClockId))
                    .enqueue()
            }
        }
    }

    private fun stopPlayAlarmClockSoundService(context: Context) {
        context.stopService(Intent(context, PlayAlarmClockSoundService::class.java))
    }

    private fun sendBroadcastForFinishActivity(context: Context) {
        context.sendBroadcast(Intent(Action.ACTION_FINISH_ALARM_CLOCK_ACTIVITY))
    }

    private fun startForegroundService(
        context: Context,
        alarmClockId: Long,
        alarmClockHour: String?,
        alarmClockMinute: String?,
        alarmClockSoundType: String?,
        alarmClockSoundUri: String?,
        alarmClockIsVibrate: Boolean?,
        alarmClockName: String?,
    ) {
        val playAlarmClockSoundServiceIntent =
            Intent(context, PlayAlarmClockSoundService::class.java).apply {
                putExtra(AlarmClockKeys.ID_KEY, alarmClockId)
                putExtra(AlarmClockKeys.HOUR_KEY, alarmClockHour)
                putExtra(AlarmClockKeys.MINUTE_KEY, alarmClockMinute)
                putExtra(AlarmClockKeys.SOUND_TYPE_KEY, alarmClockSoundType)
                putExtra(AlarmClockKeys.SOUND_URI_KEY, alarmClockSoundUri)
                putExtra(AlarmClockKeys.IS_VIBRATE_KEY, alarmClockIsVibrate)
                putExtra(AlarmClockKeys.NAME_KEY, alarmClockName)
            }

        context.startForegroundService(playAlarmClockSoundServiceIntent)
    }

    private fun updateAlarmClockRequest(alarmClockId: Long): OneTimeWorkRequest {
        val inputData = Data.Builder()
            .putLong(AlarmClockKeys.ID_KEY, alarmClockId)
            .build()

        return OneTimeWorkRequestBuilder<UpdateAlarmClockInDatabaseWorker>()
            .setInputData(inputData)
            .build()
    }

    private fun scheduleAlarmClockRequest(alarmClockId: Long): OneTimeWorkRequest {
        val inputData = Data.Builder()
            .putLong(AlarmClockKeys.ID_KEY, alarmClockId)
            .build()

        return OneTimeWorkRequestBuilder<ScheduleAlarmClockWorker>()
            .setInputData(inputData)
            .build()
    }

    private fun snoozeAlarmClockRequest(alarmClockId: Long): OneTimeWorkRequest {
        val inputData = Data.Builder()
            .putLong(AlarmClockKeys.ID_KEY, alarmClockId)
            .build()

        return OneTimeWorkRequestBuilder<SnoozeAlarmClockWorker>()
            .setInputData(inputData)
            .build()
    }

    private fun getNewReleaseSpotifyArtistRequest(spotifyArtistId: String): OneTimeWorkRequest {
        val inputData = Data.Builder()
            .putString(SpotifyKeys.SPOTIFY_ARTIST_ID_KEY, spotifyArtistId)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequestBuilder<GetNewReleaseSpotifyArtistWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .addTag(WorkRequestTags.GET_NEW_RELEASE_SPOTIFY_ARTIST_REQUEST_TAG)
            .build()
    }
}