package com.ioffeivan.alarmclock.background.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.core.utils.Constants
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType
import com.ioffeivan.alarmclock.spotify.album.domain.model.Album
import com.ioffeivan.alarmclock.spotify.album.domain.model.TracksAlbumRequest
import com.ioffeivan.alarmclock.spotify.album.domain.usecase.GetAlbumsByArtistIdUseCase
import com.ioffeivan.alarmclock.spotify.album.domain.usecase.GetTracksByAlbumIdUseCase
import com.ioffeivan.alarmclock.spotify.auth.domain.model.AuthResponse
import com.ioffeivan.alarmclock.spotify.auth.domain.usecase.SpotifyAuthUseCase
import com.ioffeivan.alarmclock.spotify.track.domain.model.Track
import com.ioffeivan.alarmclock.spotify.track.domain.model.getTrackNameWithArtistsName
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

@HiltWorker
class GetNewReleaseSpotifyArtistWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val spotifyAuthUseCase: SpotifyAuthUseCase,
    private val getAlbumsByArtistIdUseCase: GetAlbumsByArtistIdUseCase,
    private val getTracksByAlbumIdUseCase: GetTracksByAlbumIdUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val artistId =
                inputData.getString(Constants.AlarmClockKeys.SPOTIFY_ARTIST_ID_KEY) ?: return Result.failure()

            val authResponse: AuthResponse = spotifyAuth() ?: return Result.retry()
            val outputData: Data = getOutputData(authResponse, artistId) ?: return Result.retry()

            Result.success(outputData)
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun spotifyAuth(): AuthResponse? {
        return spotifyAuthUseCase()
            .filterNot { it == ApiResponse.Loading }
            .map { response ->
                when (response) {
                    is ApiResponse.Success -> response.data
                    else -> null
                }
            }
            .firstOrNull()
    }

    private suspend fun getOutputData(
        authResponse: AuthResponse,
        artistId: String,
    ): Data? {
        val track = getTrackFromLastAlbumSpotifyArtist(authResponse, artistId) ?: return null

        return Data.Builder()
            .putString(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_NAME_KEY, track.getTrackNameWithArtistsName())
            .putString(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_TYPE_KEY, SoundType.SPOTIFY_TRACK.name)
            .putString(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_URI_KEY, track.uri)
            .build()
    }

    private suspend fun getTrackFromLastAlbumSpotifyArtist(
        authResponse: AuthResponse,
        artistId: String,
    ): Track? {
        val lastAlbum = getLastAlbumSpotifyArtist(authResponse, artistId) ?: return null

        return getTracksByAlbumIdUseCase(
            TracksAlbumRequest(
                token = authResponse.accessToken,
                albumId = lastAlbum.id,
            )
        ).filterNot { it == ApiResponse.Loading }
            .map { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        response.data.items.first { track ->
                            artistId in track.artists.map { it.id }
                        }
                    }

                    else -> null
                }
            }
            .firstOrNull()
    }

    private suspend fun getLastAlbumSpotifyArtist(
        authResponse: AuthResponse,
        artistId: String,
    ): Album? {
        return getAlbumsByArtistIdUseCase(
            com.ioffeivan.alarmclock.spotify.album.domain.model.AlbumsArtistRequest(
                token = authResponse.accessToken,
                artistId = artistId,
            )
        ).filterNot { it == ApiResponse.Loading }
            .map { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        response.data.items.maxByOrNull { album -> album.releaseDate }
                    }

                    else -> null
                }
            }
            .firstOrNull()
    }
}