package com.ioffeivan.alarmclock.spotify.album.data.source.remote

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.core.data.apiRemoteRequestFlow
import com.ioffeivan.alarmclock.spotify.album.data.source.remote.model.AlbumsRemote
import com.ioffeivan.alarmclock.spotify.album.data.source.remote.service.AlbumApiService
import com.ioffeivan.alarmclock.spotify.album.domain.model.TracksAlbumRequest
import com.ioffeivan.alarmclock.spotify.track.data.source.remote.model.TracksRemote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetrofitAlbumRemoteDataSource @Inject constructor(
    private val albumApiService: AlbumApiService
) : AlbumRemoteDataSource {

    override fun getAlbumsByArtistId(albumsArtistRequest: com.ioffeivan.alarmclock.spotify.album.domain.model.AlbumsArtistRequest): Flow<ApiResponse<AlbumsRemote>> {
        return apiRemoteRequestFlow {
            albumApiService.getAlbumsByArtistId(
                token = "Bearer ${albumsArtistRequest.token}",
                artistId = albumsArtistRequest.artistId,
            )
        }
    }

    override fun getTracksByAlbumId(tracksAlbumRequest: TracksAlbumRequest): Flow<ApiResponse<TracksRemote>> {
        return apiRemoteRequestFlow {
            albumApiService.getTracksByAlbumId(
                token = "Bearer ${tracksAlbumRequest.token}",
                albumId = tracksAlbumRequest.albumId,
            )
        }
    }
}