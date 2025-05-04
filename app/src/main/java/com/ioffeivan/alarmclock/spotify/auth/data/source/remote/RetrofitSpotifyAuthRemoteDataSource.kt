package com.ioffeivan.alarmclock.spotify.auth.data.source.remote

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.core.data.apiRemoteRequestFlow
import com.ioffeivan.alarmclock.spotify.auth.data.source.remote.model.AuthResponseRemote
import com.ioffeivan.alarmclock.spotify.auth.data.source.remote.service.SpotifyAuthService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetrofitSpotifyAuthRemoteDataSource @Inject constructor(
    private val spotifyAuthService: SpotifyAuthService
) : SpotifyAuthRemoteDataSource {

    override fun auth(): Flow<ApiResponse<AuthResponseRemote>> {
        return apiRemoteRequestFlow {
            spotifyAuthService.auth()
        }
    }
}