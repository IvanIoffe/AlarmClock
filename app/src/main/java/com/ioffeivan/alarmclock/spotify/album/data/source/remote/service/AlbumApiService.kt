package com.ioffeivan.alarmclock.spotify.album.data.source.remote.service

import com.ioffeivan.alarmclock.spotify.album.data.source.remote.model.AlbumsRemote
import com.ioffeivan.alarmclock.spotify.track.data.source.remote.model.TracksRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumApiService {

    @GET("v1/artists/{id}/albums")
    suspend fun getAlbumsByArtistId(
        @Header("Authorization") token: String,
        @Path("id") artistId: String,
        @Query("limit") limit: Int = 50,
    ): Response<AlbumsRemote>

    @GET("v1/albums/{id}/tracks")
    suspend fun getTracksByAlbumId(
        @Header("Authorization") token: String,
        @Path("id") albumId: String,
    ): Response<TracksRemote>
}