package com.ioffeivan.alarmclock.spotify.album.domain.usecase

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.album.domain.model.Albums
import com.ioffeivan.alarmclock.spotify.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsByArtistIdUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
) {
    operator fun invoke(albumsArtistRequest: com.ioffeivan.alarmclock.spotify.album.domain.model.AlbumsArtistRequest): Flow<ApiResponse<Albums>> =
        albumRepository.getAlbumsByArtistId(albumsArtistRequest)
}