package com.ioffeivan.alarmclock.spotify.artist.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.ioffeivan.alarmclock.R
import com.ioffeivan.alarmclock.sound_selection.domain.model.Sound
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType
import com.ioffeivan.alarmclock.sound_selection.presentation.SoundViewModel
import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artist
import com.ioffeivan.alarmclock.spotify.common.presentation.EmptyContentScreen
import com.ioffeivan.alarmclock.spotify.common.presentation.ErrorScreen
import com.ioffeivan.alarmclock.spotify.common.presentation.LoadingScreen
import com.ioffeivan.alarmclock.spotify.common.presentation.SpotifySearchBar
import com.ioffeivan.alarmclock.spotify.common.presentation.TokenViewModel
import com.ioffeivan.alarmclock.spotify.connection.SpotifyConnector
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchType

@Composable
fun SpotifyArtistsScreen(
    spotifyArtistsScreenViewModel: SpotifyArtistsScreenViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel,
    soundViewModel: SoundViewModel,
    onLeadingIconClick: () -> Unit,
) {
    val uiState by spotifyArtistsScreenViewModel.uiState.collectAsStateWithLifecycle()
    val accessToken by tokenViewModel.accessToken.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            SpotifyConnector.disconnect()
        }
    }

    SearchArtistsScreen(
        uiState = uiState,
        onSearch = { query ->
            spotifyArtistsScreenViewModel.searchArtists(
                SearchRequest(
                    token = accessToken ?: "",
                    query = query,
                    type = SearchType.ARTIST,
                ),
            )
        },
        onLeadingIconClick = {
            onLeadingIconClick()
        },
        onArtistClick = { artist ->
            soundViewModel.setSound(
                Sound(
                    name = artist.name,
                    type = SoundType.NEW_RELEASE_SPOTIFY_ARTIST,
                    uri = artist.uri,
                )
            )
        }
    )
}

@Composable
private fun SearchArtistsScreen(
    uiState: SpotifyArtistsScreenUiState,
    onSearch: (String) -> Unit,
    onLeadingIconClick: () -> Unit,
    onArtistClick: (Artist) -> Unit,
) {
    val isActive = remember { mutableStateOf(false) }
    val query = remember { mutableStateOf("") }

    var showDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SpotifySearchBar(
                isActive = isActive,
                query = query,
                placeholderText = stringResource(R.string.enter_artist),
                onSearch = onSearch,
                onLeadingIconClick = onLeadingIconClick,
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
            ) {
                Hint(
                    onHintClick = { showDialog = true }
                )

                if (showDialog) {
                    Dialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        onConfirm = {
                            showDialog = false
                        },
                        dialogTitle = stringResource(R.string.how_it_work),
                        dialogText = stringResource(R.string.dialog_text),
                    )
                }

                when (uiState) {
                    is SpotifyArtistsScreenUiState.Content -> {
                        SpotifyArtistsContent(
                            uiState = uiState,
                            onArtistClick = onArtistClick,
                        )
                    }

                    is SpotifyArtistsScreenUiState.Error -> {
                        ErrorScreen(
                            errorMessage = stringResource(id = R.string.error_loading_message),
                            actionText = stringResource(id = R.string.error_loading_action_text),
                            onButtonClick = {
                                onSearch(query.value)
                            },
                        )
                    }

                    SpotifyArtistsScreenUiState.Loading -> {
                        LoadingScreen()
                    }

                    SpotifyArtistsScreenUiState.EmptyContent -> {
                        EmptyContentScreen()
                    }

                    else -> {}
                }
            }
        }
    )
}

@Composable
private fun Hint(
    onHintClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
    ) {
        Text(
            text = stringResource(R.string.hint_text),
            fontSize = 14.sp,
            lineHeight = 18.sp,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier
                .clickable {
                    onHintClick()
                },
            text = stringResource(R.string.how_it_work),
            fontSize = 12.sp,
            lineHeight = 1.sp,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Composable
private fun Dialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.clearly))
            }
        },
    )
}

@Composable
private fun SpotifyArtistsContent(
    uiState: SpotifyArtistsScreenUiState.Content,
    onArtistClick: (Artist) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Artists(
            artists = uiState.artists,
            onArtistClick = onArtistClick
        )
    }
}

@Composable
private fun Artists(
    artists: List<Artist>,
    onArtistClick: (Artist) -> Unit,
) {
    var selectedArtist by remember { mutableStateOf<Artist?>(null) }

    LazyColumn {
        items(items = artists) { artist ->
            val isSelected = artist == selectedArtist
            Artist(
                modifier = Modifier
                    .clickable {
                        onArtistClick(artist)
                        selectedArtist = artist
                    },
                artist = artist,
                isSelected = isSelected,
            )
        }
    }
}

@Composable
private fun Artist(
    modifier: Modifier,
    artist: Artist,
    isSelected: Boolean,
) {
    val backgroundColor = if (isSelected) Color.LightGray.copy(alpha = 0.25f) else Color.Unspecified

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(0.75.dp, Color.LightGray, CircleShape),
            model = artist.imageUrl,
            loading = {
                Box(modifier = Modifier.background(Color.DarkGray))
            },
            error = {
                Box(modifier = Modifier.background(Color.DarkGray))
            },
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = artist.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}