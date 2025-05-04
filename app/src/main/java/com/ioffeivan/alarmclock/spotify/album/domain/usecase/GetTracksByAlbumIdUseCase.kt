package com.ioffeivan.alarmclock.spotify.album.domain.usecase

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.album.domain.model.TracksAlbumRequest
import com.ioffeivan.alarmclock.spotify.album.domain.repository.AlbumRepository
import com.ioffeivan.alarmclock.spotify.track.domain.model.Tracks
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTracksByAlbumIdUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
) {
    operator fun invoke(tracksAlbumRequest: TracksAlbumRequest): Flow<ApiResponse<Tracks>> =
        albumRepository.getTracksByAlbumId(tracksAlbumRequest)
}