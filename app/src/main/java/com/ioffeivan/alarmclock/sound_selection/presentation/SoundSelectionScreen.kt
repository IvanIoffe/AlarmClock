package com.ioffeivan.alarmclock.sound_selection.presentation

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ioffeivan.alarmclock.R
import com.ioffeivan.alarmclock.app.MainActivity
import com.ioffeivan.alarmclock.audio_player.MediaPlayerController
import com.ioffeivan.alarmclock.core.utils.defaultSound
import com.ioffeivan.alarmclock.create_and_update_alarmclock.presentation.utils.queryFileName
import com.ioffeivan.alarmclock.create_and_update_alarmclock.presentation.utils.takePersistableUriPermission
import com.ioffeivan.alarmclock.sound_selection.domain.model.Sound
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundSelectionScreen(
    soundViewModel: SoundViewModel,
    onNavigationIconTopAppBarClick: () -> Unit,
    onSpotifyContentTypeClick: (String) -> Unit,
) {
    val context = LocalContext.current as MainActivity
    val mediaPlayerController = remember { MediaPlayerController(context) }

    val soundState by soundViewModel.sound.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        context.volumeControlStream = AudioManager.STREAM_ALARM

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    mediaPlayerController.resume()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    mediaPlayerController.pause()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            mediaPlayerController.dispose()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val systemSounds = listOf(
        Sound(
            name = stringResource(id = R.string.mute),
            type = SoundType.SYSTEM,
            uri = null,
        ),
        Sound(
            name = stringResource(id = R.string.default_sound),
            type = SoundType.SYSTEM,
            uri = defaultSound.toString(),
        ),
    )
    val deviceSound = soundState.takeIf { it?.type == SoundType.DEVICE }
        ?: Sound(
            name = null,
            type = SoundType.DEVICE,
            uri = null,
        )

    val spotifyTrack = soundState.takeIf { it?.type == SoundType.SPOTIFY_TRACK }
        ?: Sound(
            name = null,
            type = SoundType.SPOTIFY_TRACK,
            uri = null,
        )

    val newReleaseSpotifyArtist = soundState.takeIf { it?.type == SoundType.NEW_RELEASE_SPOTIFY_ARTIST }
        ?: Sound(
            name = null,
            type = SoundType.NEW_RELEASE_SPOTIFY_ARTIST,
            uri = null,
        )

    val allSounds = listOf(
        SoundTypeItem(
            icon = painterResource(id = R.drawable.ic_phone_android),
            type = SoundType.SYSTEM,
            name = stringResource(id = R.string.system_sounds),
        ),
        systemSounds[0], systemSounds[1],
        SoundTypeItem(
            icon = painterResource(id = R.drawable.ic_audio_file),
            type = SoundType.DEVICE,
            name = stringResource(id = R.string.device_sounds),
        ),
        deviceSound,
        SoundTypeItem(
            icon = painterResource(id = R.drawable.ic_public),
            type = SoundType.SPOTIFY_TRACK,
            name = stringResource(id = R.string.spotify_track),
        ),
        spotifyTrack,
        SoundTypeItem(
            icon = painterResource(id = R.drawable.ic_new),
            type = SoundType.NEW_RELEASE_SPOTIFY_ARTIST,
            name = stringResource(id = R.string.new_release_spotify_artist),
        ),
        newReleaseSpotifyArtist,
    )

    val contentResolver = context.contentResolver
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data ?: run {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_sound_selection),
                        Toast.LENGTH_LONG
                    ).show()
                    return@rememberLauncherForActivityResult
                }

                val fileName = context.contentResolver.queryFileName(uri)

                val newSound = deviceSound.copy(name = fileName, uri = uri.toString())
                soundViewModel.setSound(newSound)

                takePersistableUriPermission(contentResolver, uri)
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.select_sound)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigationIconTopAppBarClick()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        },

        content = { paddingValues ->
            Sounds(
                soundState = soundState!!,
                paddingValues = paddingValues,
                allSounds = allSounds,
                onSoundClick = { sound, isPlaying ->
                    soundViewModel.setSound(sound)
                    if (sound.type in arrayOf(SoundType.SYSTEM, SoundType.DEVICE)) {
                        if (isPlaying) {
                            mediaPlayerController.play(sound.uri)
                        } else {
                            mediaPlayerController.reset()
                        }
                    }
                },
                onSoundTypeClick = { soundType ->
                    when (soundType) {
                        SoundType.DEVICE -> {
                            mediaPlayerController.reset()

                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "audio/*"
                            }
                            launcher.launch(intent)
                        }

                        SoundType.SPOTIFY_TRACK -> {
                            onSpotifyContentTypeClick(SoundType.SPOTIFY_TRACK.name)
                        }

                        SoundType.NEW_RELEASE_SPOTIFY_ARTIST -> {
                            onSpotifyContentTypeClick(SoundType.NEW_RELEASE_SPOTIFY_ARTIST.name)
                        }

                        else -> {}
                    }
                }
            )
        }
    )
}

@Composable
private fun Sounds(
    soundState: Sound,
    paddingValues: PaddingValues,
    allSounds: List<Any>,
    onSoundClick: (Sound, Boolean) -> Unit,
    onSoundTypeClick: (SoundType) -> Unit,
) {
    var isPlayingSound by remember { mutableStateOf<Sound?>(null) }

    LazyColumn(contentPadding = paddingValues) {
        items(items = allSounds) { item ->
            if (item is SoundTypeItem) {
                SoundTypeItem(
                    soundTypeItem = item,
                    onClick = {
                        onSoundTypeClick(item.type)
                    }
                )
            } else if (item is Sound) {
                if (item.name != null) {
                    val selected = item == soundState
                    val isPlaying = item == isPlayingSound
                    SoundItem(
                        modifier = Modifier
                            .selectable(
                                selected = selected,
                                onClick = {
                                    onSoundClick(item, !isPlaying)
                                    isPlayingSound = if (!isPlaying) item else null
                                },
                            ),
                        nameSound = item.name,
                        isSelected = selected,
                    )
                }
            }
        }
    }
}

@Composable
private fun SoundTypeItem(
    soundTypeItem: SoundTypeItem,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painter = soundTypeItem.icon, contentDescription = null)

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = soundTypeItem.name,
                fontSize = 20.sp,
            )
        }
    }
}

@Composable
private fun SoundItem(
    modifier: Modifier,
    nameSound: String,
    isSelected: Boolean,
) {
    val backgroundColor = if (isSelected) Color.LightGray.copy(alpha = 0.25f) else Color.Unspecified

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(start = 56.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(0.9f),
                text = nameSound,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            RadioButton(
                modifier = Modifier
                    .weight(0.1f),
                selected = isSelected,
                onClick = null
            )
        }
    }
}