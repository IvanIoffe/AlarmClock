package com.ioffeivan.alarmclock.spotify.album.data.source.remote

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.album.data.source.remote.model.AlbumsRemote
import com.ioffeivan.alarmclock.spotify.album.domain.model.TracksAlbumRequest
import com.ioffeivan.alarmclock.spotify.track.data.source.remote.model.TracksRemote
import kotlinx.coroutines.flow.Flow

interface AlbumRemoteDataSource {
    fun getAlbumsByArtistId(albumsArtistRequest: com.ioffeivan.alarmclock.spotify.album.domain.model.AlbumsArtistRequest): Flow<ApiResponse<AlbumsRemote>>
    fun getTracksByAlbumId(tracksAlbumRequest: TracksAlbumRequest): Flow<ApiResponse<TracksRemote>>
}