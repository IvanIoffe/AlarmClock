package com.ioffeivan.alarmclock.spotify.album.data.repository

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.core.data.mapper.mapToDomainModelFlow
import com.ioffeivan.alarmclock.spotify.album.data.mapper.toAlbums
import com.ioffeivan.alarmclock.spotify.album.data.source.remote.AlbumRemoteDataSource
import com.ioffeivan.alarmclock.spotify.album.domain.model.Albums
import com.ioffeivan.alarmclock.spotify.album.domain.model.TracksAlbumRequest
import com.ioffeivan.alarmclock.spotify.album.domain.repository.AlbumRepository
import com.ioffeivan.alarmclock.spotify.track.data.mapper.toTracks
import com.ioffeivan.alarmclock.spotify.track.domain.model.Tracks
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumRemoteDataSource: AlbumRemoteDataSource
) : AlbumRepository {

    override fun getAlbumsByArtistId(albumsArtistRequest: com.ioffeivan.alarmclock.spotify.album.domain.model.AlbumsArtistRequest): Flow<ApiResponse<Albums>> {
        return albumRemoteDataSource.getAlbumsByArtistId(albumsArtistRequest)
            .mapToDomainModelFlow {
                it.toAlbums()
            }
    }

    override fun getTracksByAlbumId(tracksAlbumRequest: TracksAlbumRequest): Flow<ApiResponse<Tracks>> {
        return albumRemoteDataSource.getTracksByAlbumId(tracksAlbumRequest)
            .mapToDomainModelFlow {
                it.toTracks()
            }
    }
}