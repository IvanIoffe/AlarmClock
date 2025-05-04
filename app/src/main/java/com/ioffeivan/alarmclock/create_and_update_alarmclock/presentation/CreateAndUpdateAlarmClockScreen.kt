package com.ioffeivan.alarmclock.create_and_update_alarmclock.presentation

import android.Manifest
import android.media.AudioManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ioffeivan.alarmclock.app.MainActivity
import com.ioffeivan.alarmclock.R
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.presentation.ClearTextIconButton
import com.ioffeivan.alarmclock.core.presentation.TimeUntilAlarmClockMessage
import com.ioffeivan.alarmclock.core.utils.PluralDurationFormatter
import com.ioffeivan.alarmclock.core.utils.checkPostNotificationPermission
import com.ioffeivan.alarmclock.sound_selection.domain.model.Sound
import com.ioffeivan.alarmclock.sound_selection.presentation.SoundViewModel
import java.time.LocalTime

@Composable
fun CreateAndUpdateAlarmClockScreen(
    alarmClockId: String?,
    viewModel: CreateAndUpdateAlarmClockScreenViewModel = hiltViewModel(),
    soundViewModel: SoundViewModel,
    onNavigationIconTopAppBarClick: () -> Unit,
    onActionIconTopAppBarClick: (AlarmClock) -> Unit,
    onAlarmClockSoundClick: () -> Unit,
) {
    val context = LocalContext.current as MainActivity
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val soundState by soundViewModel.sound.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getAlarmClockById(alarmClockId)
        context.volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
    }

    when (val state = uiState) {
        is CreateAndUpdateAlarmClockScreenUiState.Content -> {
            CreateAndUpdateAlarmClockScreenContent(
                alarmClock = state.alarmClock,
                sound = soundState ?: state.alarmClock.sound,
                onNavigationIconTopAppBarClick = onNavigationIconTopAppBarClick,
                onActionIconTopAppBarClick = { alarmClock ->
                    viewModel.addOrUpdateAlarmClock(alarmClock)
                    onActionIconTopAppBarClick(alarmClock)
                },
                onAlarmClockSoundClick = { sound ->
                    if (soundState == null) soundViewModel.setSound(sound)
                    onAlarmClockSoundClick()
                },
            )
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateAndUpdateAlarmClockScreenContent(
    alarmClock: AlarmClock,
    sound: Sound,
    onNavigationIconTopAppBarClick: () -> Unit,
    onActionIconTopAppBarClick: (AlarmClock) -> Unit,
    onAlarmClockSoundClick: (Sound) -> Unit,
) {
    val context = LocalContext.current

    val timePickerState = rememberTimePickerState(
        initialHour = alarmClock.time.hour,
        initialMinute = alarmClock.time.minute,
    )
    val isVibrateState = rememberSaveable { mutableStateOf(alarmClock.isVibrate) }
    val nameState = rememberSaveable { mutableStateOf(alarmClock.name) }
    val timeSnoozeState = rememberSaveable { mutableIntStateOf(alarmClock.timeSnooze) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        onActionIconTopAppBarClick(
            alarmClock.copy(
                time = LocalTime.of(
                    timePickerState.hour,
                    timePickerState.minute
                ),
                isEnabled = true,
                sound = sound,
                isVibrate = isVibrateState.value,
                name = nameState.value,
                timeSnooze = timeSnoozeState.intValue,
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onNavigationIconTopAppBarClick() }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            checkPostNotificationPermission(
                                context = context,
                                onGranted = {
                                    onActionIconTopAppBarClick(
                                        alarmClock.copy(
                                            time = LocalTime.of(
                                                timePickerState.hour,
                                                timePickerState.minute
                                            ),
                                            isEnabled = true,
                                            sound = sound,
                                            isVibrate = isVibrateState.value,
                                            name = nameState.value,
                                            timeSnooze = timeSnoozeState.intValue,
                                        )
                                    )
                                },
                                onDenied = {
                                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                },
                            )
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                    }
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AlarmClockTimePicker(
                    timePickerState = timePickerState,
                )

                TimeUntilAlarmClockMessage(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    alarmClockTime = LocalTime.of(timePickerState.hour, timePickerState.minute),
                )

                AlarmClockName(
                    nameState = nameState,
                )

                AlarmClockSound(
                    soundName = sound.name ?: stringResource(R.string.alarm_clock_unnamed_sound),
                    onClick = {
                        onAlarmClockSoundClick(sound)
                    },
                )

                AlarmClockVibration(
                    isVibrateState = isVibrateState,
                )

                PostponingAlarmClock(
                    timeSnoozeState = timeSnoozeState,
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmClockTimePicker(
    timePickerState: TimePickerState,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@Composable
private fun AlarmClockName(nameState: MutableState<String?>) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.alarm_clock_name),
            fontSize = 16.sp,
        )

        Spacer(modifier = Modifier.height(4.dp))

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            value = nameState.value ?: "",
            onValueChange = { newText ->
                nameState.value = newText
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            cursorBrush = SolidColor(
                MaterialTheme.colorScheme.onBackground,
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                val isNullOrEmptyName = nameState.value.isNullOrEmpty()
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 1.dp, end = 36.dp),
                    ) {
                        innerTextField()
                    }
                    Text(
                        text = if (isNullOrEmptyName) stringResource(id = R.string.alarm_clock) else "",
                        color = Color.Gray,
                        fontSize = 14.sp,
                    )
                    ClearTextIconButton(
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                        isVisible = !isNullOrEmptyName,
                        onClick = {
                            if (!isFocused) focusRequester.requestFocus()
                            nameState.value = null
                        },
                    )
                }
            },
        )
    }
}

@Composable
private fun AlarmClockSound(
    soundName: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.alarm_clock_sound),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = soundName,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun AlarmClockVibration(
    isVibrateState: MutableState<Boolean>,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isVibrateState.value = !isVibrateState.value
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = R.string.alarm_clock_vibration),
                fontSize = 16.sp,
            )

            Switch(
                checked = isVibrateState.value,
                onCheckedChange = {
                    isVibrateState.value = it
                },
            )
        }
    }
}

@Composable
private fun PostponingAlarmClock(
    timeSnoozeState: MutableIntState,
) {
    val context = LocalContext.current
    val pluralDurationFormatter = remember { PluralDurationFormatter(context = context) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val timeSnoozeList = remember {
        List(60) {
            val minute = it + 1
            TimeSnooze(
                time = minute,
                displayName = pluralDurationFormatter.formatDuration(_minutes = minute),
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDialog = true
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.alarm_clock_time_snooze),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = pluralDurationFormatter.formatDuration(_minutes = timeSnoozeState.intValue),
                fontSize = 14.sp,
                color = Color.Gray,
            )

            if (showDialog) {
                ChoosingTimeSnoozeAlertDialog(
                    timeSnoozeState = timeSnoozeState,
                    list = timeSnoozeList,
                    onDismissRequest = {
                        showDialog = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ChoosingTimeSnoozeAlertDialog(
    timeSnoozeState: MutableIntState,
    list: List<TimeSnooze>,
    onDismissRequest: () -> Unit,
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = timeSnoozeState.intValue - 1
    )

    AlertDialog(
        modifier = Modifier
            .padding(vertical = 16.dp),
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        title = {
            Text(text = stringResource(R.string.alarm_clock_time_snooze))
        },
        text = {
            LazyColumn(state = listState) {
                items(items = list) { timeSnooze ->
                    val selected = timeSnoozeState.intValue == timeSnooze.time
                    TimeSnoozeItem(
                        modifier = Modifier
                            .selectable(
                                selected = selected,
                                onClick = {
                                    timeSnoozeState.intValue = timeSnooze.time
                                    onDismissRequest()
                                }
                            ),
                        formattedTime = timeSnooze.displayName,
                        selected = selected,
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
fun TimeSnoozeItem(
    modifier: Modifier,
    formattedTime: String,
    selected: Boolean,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = formattedTime,
            fontSize = 16.sp,
        )
    }
}