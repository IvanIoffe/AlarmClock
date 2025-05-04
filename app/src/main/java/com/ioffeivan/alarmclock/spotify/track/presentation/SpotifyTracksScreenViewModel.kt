package com.ioffeivan.alarmclock.spotify.track.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import com.ioffeivan.alarmclock.spotify.search.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SpotifyTracksScreenViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<SpotifyTracksScreenUiState>(SpotifyTracksScreenUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun searchTracks(searchRequest: SearchRequest) {
        searchUseCase(searchRequest)
            .onEach {
                _uiState.value = when (it) {
                    is ApiResponse.Error -> {
                        SpotifyTracksScreenUiState.Error
                    }

                    ApiResponse.Loading -> {
                        SpotifyTracksScreenUiState.Loading
                    }

                    is ApiResponse.Success -> {
                        SpotifyTracksScreenUiState.Content(
                            it.data.tracks?.items ?: emptyList()
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}