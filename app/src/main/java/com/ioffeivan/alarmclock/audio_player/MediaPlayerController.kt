package com.ioffeivan.alarmclock.audio_player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.core.net.toUri
import com.ioffeivan.alarmclock.core.utils.Constants
import com.ioffeivan.alarmclock.core.utils.defaultSound

class MediaPlayerController(private val context: Context) : PlayerController {
    private var mediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
    }

    override fun play(uri: String?) {
        reset()
        if (uri == null) return

        configureMediaPlayer(uri)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    override fun resume() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun dispose() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    fun reset() {
        mediaPlayer.reset()
    }

    private fun configureMediaPlayer(uri: String) {
        try {
            mediaPlayer.setDataSource(context, uri.toUri())
        } catch (e: Exception) {
            mediaPlayer.setDataSource(context, defaultSound)
        } finally {
            mediaPlayer.apply {
                isLooping = true
            }
        }
    }
}