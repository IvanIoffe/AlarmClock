package com.ioffeivan.alarmclock.background.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ioffeivan.alarmclock.audio_player.MediaPlayerController
import com.ioffeivan.alarmclock.audio_player.PlayerController
import com.ioffeivan.alarmclock.audio_player.SpotifyPlayerController
import com.ioffeivan.alarmclock.background.service.notification.AlarmClockNotificationHelper
import com.ioffeivan.alarmclock.core.utils.AlarmClockKeys
import com.ioffeivan.alarmclock.core.utils.LoadingError
import com.ioffeivan.alarmclock.core.utils.defaultSound
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType
import com.ioffeivan.alarmclock.spotify.connection.SpotifyConnector
import com.ioffeivan.alarmclock.spotify.connection.domain.ConnectionResult
import com.ioffeivan.alarmclock.spotify.connection.domain.usecase.SpotifyConnectUseCase
import com.ioffeivan.alarmclock.vibrator.VibratorController
import com.ioffeivan.alarmclock.vibrator.VibratorControllerImpl
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp
import com.spotify.android.appremote.api.error.NotLoggedInException
import com.spotify.android.appremote.api.error.UserNotAuthorizedException
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class PlayAlarmClockSoundService : Service() {

    @Inject
    lateinit var alarmClockNotificationHelper: AlarmClockNotificationHelper

    @Inject
    lateinit var spotifyConnectUseCase: Lazy<SpotifyConnectUseCase>

    private val coroutineScope by lazy {
        CoroutineScope(Dispatchers.Main)
    }

    private var playerController: PlayerController? = null
    private var vibratorController: VibratorController? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val alarmClockId = intent.getLongExtra(AlarmClockKeys.ID_KEY, -1)
        val alarmClockSoundType =
            intent.getStringExtra(AlarmClockKeys.SOUND_TYPE_KEY) ?: SoundType.SYSTEM.name
        val alarmClockSoundUri = intent.getStringExtra(AlarmClockKeys.SOUND_URI_KEY)
        val alarmClockIsVibrate = intent.getBooleanExtra(AlarmClockKeys.IS_VIBRATE_KEY, false)
        val alarmClockName = intent.getStringExtra(AlarmClockKeys.NAME_KEY)

        playSound(alarmClockSoundType, alarmClockSoundUri)

        if (alarmClockIsVibrate) {
            vibratorController = VibratorControllerImpl(applicationContext)
            vibratorController?.vibrate()
        }

        startForeground(ID, alarmClockNotificationHelper.createNotification(alarmClockId, alarmClockName))

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        playerController?.dispose()
        vibratorController?.cancel()
        coroutineScope.cancel()

        super.onDestroy()
    }

    private fun playSound(soundType: String, soundUri: String?) {
        when (SoundType.valueOf(soundType)) {
            SoundType.SYSTEM, SoundType.DEVICE -> {
                if (soundUri != null) {
                    playerController = MediaPlayerController(applicationContext)
                    playerController?.play(soundUri)
                }
            }

            SoundType.SPOTIFY_TRACK, SoundType.NEW_RELEASE_SPOTIFY_ARTIST -> {
                spotifyConnectUseCase.get().invoke()
                    .onEach {
                        when (it) {
                            is ConnectionResult.Connected -> {
                                playerController =
                                    SpotifyPlayerController(SpotifyConnector.spotifyAppRemote)
                                playerController?.play(soundUri)
                            }

                            is ConnectionResult.Failure -> {
                                when (it.throwable) {
                                    is CouldNotFindSpotifyApp, is NotLoggedInException,
                                    is UserNotAuthorizedException, is LoadingError -> {
                                        playerController = MediaPlayerController(applicationContext)
                                        playerController?.play(defaultSound.toString())
                                    }
                                }
                            }
                        }
                    }
                    .launchIn(coroutineScope)
            }
        }
    }

    private companion object {
        private const val ID = 1
    }
}