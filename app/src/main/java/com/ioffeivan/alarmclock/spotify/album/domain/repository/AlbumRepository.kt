package com.ioffeivan.alarmclock.spotify.album.domain.repository

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.album.domain.model.Albums
import com.ioffeivan.alarmclock.spotify.album.domain.model.TracksAlbumRequest
import com.ioffeivan.alarmclock.spotify.track.domain.model.Tracks
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun getAlbumsByArtistId(albumsArtistRequest: com.ioffeivan.alarmclock.spotify.album.domain.model.AlbumsArtistRequest): Flow<ApiResponse<Albums>>
    fun getTracksByAlbumId(tracksAlbumRequest: TracksAlbumRequest): Flow<ApiResponse<Tracks>>
}