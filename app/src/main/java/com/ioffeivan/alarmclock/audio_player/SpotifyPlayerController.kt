package com.ioffeivan.alarmclock.audio_player

import com.ioffeivan.alarmclock.spotify.connection.SpotifyConnector
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Repeat

class SpotifyPlayerController(
    private val spotifyAppRemote: SpotifyAppRemote?,
) : PlayerController {
    private val player = spotifyAppRemote?.playerApi

    override fun play(uri: String?) {
        player?.play(uri, PlayerApi.StreamType.ALARM)
        player?.setRepeat(Repeat.ONE)
    }

    override fun resume() {
        player?.resume()
    }

    override fun pause() {
        player?.pause()
    }

    override fun dispose() {
        spotifyAppRemote?.playerApi?.pause()
        SpotifyConnector.disconnect()
    }
}