package com.ioffeivan.alarmclock.spotify.artist.presentaion

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
class SpotifyArtistsScreenViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<SpotifyArtistsScreenUiState>(SpotifyArtistsScreenUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun searchArtists(searchRequest: SearchRequest) {
        searchUseCase(searchRequest)
            .onEach {
                _uiState.value = when (it) {
                    is ApiResponse.Error -> {
                        SpotifyArtistsScreenUiState.Error
                    }

                    ApiResponse.Loading -> {
                        SpotifyArtistsScreenUiState.Loading
                    }

                    is ApiResponse.Success -> {
                        val artists = it.data.artists?.items
                        if (!artists.isNullOrEmpty()) {
                            SpotifyArtistsScreenUiState.Content(artists = artists)
                        } else {
                            SpotifyArtistsScreenUiState.EmptyContent
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}