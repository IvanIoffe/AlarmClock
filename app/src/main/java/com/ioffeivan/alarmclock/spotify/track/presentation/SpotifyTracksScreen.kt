package com.ioffeivan.alarmclock.spotify.track.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.ioffeivan.alarmclock.R
import com.ioffeivan.alarmclock.audio_player.SpotifyPlayerController
import com.ioffeivan.alarmclock.sound_selection.domain.model.Sound
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType
import com.ioffeivan.alarmclock.sound_selection.presentation.SoundViewModel
import com.ioffeivan.alarmclock.spotify.common.presentation.EmptyContentScreen
import com.ioffeivan.alarmclock.spotify.common.presentation.ErrorScreen
import com.ioffeivan.alarmclock.spotify.common.presentation.LoadingScreen
import com.ioffeivan.alarmclock.spotify.common.presentation.PlayingSoundLottieAnimation
import com.ioffeivan.alarmclock.spotify.common.presentation.SpotifySearchBar
import com.ioffeivan.alarmclock.spotify.common.presentation.TokenViewModel
import com.ioffeivan.alarmclock.spotify.connection.SpotifyConnector
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchType
import com.ioffeivan.alarmclock.spotify.track.domain.model.Track
import com.ioffeivan.alarmclock.spotify.track.domain.model.getTrackNameWithArtistsName

@Composable
fun SpotifyTracksScreen(
    spotifyTracksScreenViewModel: SpotifyTracksScreenViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel,
    soundViewModel: SoundViewModel,
    onLeadingIconClick: () -> Unit,
) {
    val uiState by spotifyTracksScreenViewModel.uiState.collectAsStateWithLifecycle()
    val accessToken by tokenViewModel.accessToken.collectAsStateWithLifecycle()

    val spotifyPlayerController =
        remember { SpotifyPlayerController(SpotifyConnector.spotifyAppRemote) }

    DisposableEffect(Unit) {
        onDispose {
            spotifyPlayerController.dispose()
        }
    }

    SearchTracksScreen(
        uiState = uiState,
        onSearch = { query ->
            spotifyTracksScreenViewModel.searchTracks(
                SearchRequest(
                    token = accessToken ?: "",
                    query = query,
                    type = SearchType.TRACK,
                )
            )
        },
        onLeadingIconClick = {
            onLeadingIconClick()
        },
        onTrackClick = { track, isPlaying ->
            soundViewModel.setSound(
                Sound(
                    name = track.getTrackNameWithArtistsName(),
                    type = SoundType.SPOTIFY_TRACK,
                    uri = track.uri,
                )
            )
            if (!isPlaying) {
                spotifyPlayerController.play(track.uri)
            } else {
                spotifyPlayerController.pause()
            }
        },
    )
}

@Composable
private fun SearchTracksScreen(
    uiState: SpotifyTracksScreenUiState,
    onSearch: (String) -> Unit,
    onLeadingIconClick: () -> Unit,
    onTrackClick: (Track, Boolean) -> Unit,
) {
    val isActive = remember { mutableStateOf(false) }
    val query = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SpotifySearchBar(
                isActive = isActive,
                query = query,
                placeholderText = stringResource(R.string.enter_track_name),
                onSearch = onSearch,
                onLeadingIconClick = onLeadingIconClick,
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                when (uiState) {
                    is SpotifyTracksScreenUiState.Content -> {
                        Tracks(
                            tracks = uiState.tracks,
                            onTrackClick = onTrackClick,
                        )
                    }

                    is SpotifyTracksScreenUiState.Error -> {
                        ErrorScreen(
                            errorMessage = stringResource(id = R.string.error_loading_message),
                            actionText = stringResource(id = R.string.error_loading_action_text),
                            onButtonClick = {
                                onSearch(query.value)
                            },
                        )
                    }

                    SpotifyTracksScreenUiState.Loading -> {
                        LoadingScreen()
                    }

                    SpotifyTracksScreenUiState.EmptyContent -> {
                        EmptyContentScreen()
                    }

                    else -> {}
                }
            }
        }
    )
}

@Composable
private fun Tracks(
    tracks: List<Track>,
    onTrackClick: (Track, Boolean) -> Unit,
) {
    var selectedTrack by remember { mutableStateOf<Track?>(null) }
    var playingTrack by remember { mutableStateOf<Track?>(null) }

    LazyColumn {
        items(items = tracks) { track ->
            val isSelected = track == selectedTrack
            val isPlaying = track == playingTrack
            Track(
                modifier = Modifier
                    .clickable {
                        onTrackClick(track, isPlaying)
                        playingTrack = if (!isPlaying) track else null
                        selectedTrack = track
                    },
                track = track,
                isSelected = isSelected,
                isPlaying = isPlaying,
            )
        }
    }
}

@Composable
private fun Track(
    modifier: Modifier,
    track: Track,
    isSelected: Boolean,
    isPlaying: Boolean,
) {
    val containerBackgroundColor =
        if (isSelected) Color.LightGray.copy(alpha = 0.25f) else Color.Unspecified

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(containerBackgroundColor)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp)),
                model = track.imageUrl,
                loading = {
                    Box(modifier = Modifier.background(Color.DarkGray))
                },
                error = {
                    Box(modifier = Modifier.background(Color.DarkGray))
                },
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.1f))
                )
                PlayingSoundLottieAnimation(
                    modifier = Modifier
                        .size(36.dp),
                    isPlaying = isPlaying,
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = track.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = track.displayArtists,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}