package com.ioffeivan.alarmclock.spotify.auth.data.source.remote.service

import com.ioffeivan.alarmclock.BuildConfig
import com.ioffeivan.alarmclock.spotify.auth.data.source.remote.model.AuthResponseRemote
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SpotifyAuthService {

    @FormUrlEncoded
    @POST("api/token")
    suspend fun auth(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("redirect_uri") redirectUri: String = BuildConfig.SPOTIFY_REDIRECT_URI,
        @Field("client_id") clientId: String = BuildConfig.SPOTIFY_CLIENT_ID,
        @Field("client_secret") clientSecret: String = BuildConfig.SPOTIFY_CLIENT_SECRET,
    ): Response<AuthResponseRemote>
}