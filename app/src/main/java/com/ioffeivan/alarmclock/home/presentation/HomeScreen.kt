package com.ioffeivan.alarmclock.home.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ioffeivan.alarmclock.R
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.presentation.TimeUntilAlarmClockMessage
import com.ioffeivan.alarmclock.core.utils.TimeHelper
import com.ioffeivan.alarmclock.core.utils.checkPostNotificationPermission
import com.ioffeivan.alarmclock.core.utils.showTimeUntilAlarmClockToast
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    onAlarmClockClick: (AlarmClock) -> Unit,
    onFloatingActionButtonClick: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by homeScreenViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onFloatingActionButtonClick() },
                shape = CircleShape,
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                when (val state = uiState) {
                    is HomeScreenUiState.Empty -> {
                        HomeScreenEmpty()
                    }

                    is HomeScreenUiState.Content -> {
                        HomeScreenContent(
                            alarmClocks = state.alarmClocks.items,
                            onAlarmClockClick = onAlarmClockClick,
                            onDeleteClick = { alarmClock ->
                                homeScreenViewModel.deleteAlarmClock(alarmClock)
                            },
                            turnOnAlarmClock = { alarmClock ->
                                homeScreenViewModel.updateAlarmClock(alarmClock)
                                showTimeUntilAlarmClockToast(
                                    context = context,
                                    alarmClockTime = alarmClock.time,
                                )
                            },
                            turnOffAlarmClock = { alarmClock ->
                                homeScreenViewModel.updateAlarmClock(alarmClock)
                            },
                        )
                    }

                    else -> {}
                }
            }
        },
    )
}

@Composable
private fun HomeScreenEmpty() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_alarm_clocks),
        )
    }
}

@Composable
private fun HomeScreenContent(
    alarmClocks: List<AlarmClock>,
    onAlarmClockClick: (AlarmClock) -> Unit,
    onDeleteClick: (AlarmClock) -> Unit,
    turnOnAlarmClock: (AlarmClock) -> Unit,
    turnOffAlarmClock: (AlarmClock) -> Unit,
) {
    val context = LocalContext.current
    val alarmClocksState = remember { mutableStateListOf<AlarmClock>() }.apply {
        clear()
        addAll(alarmClocks)
    }

    var pendingAlarmClock by remember { mutableStateOf(AlarmClock()) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        turnOnAlarmClock(pendingAlarmClock)
    }

    LazyColumn {
        items(items = alarmClocksState, key = { it.id ?: 0 }) { alarmClock ->
            AlarmClockItem(
                modifier = Modifier
                    .animateItem(),
                alarmClock = alarmClock,

                onAlarmClockClick = {
                    onAlarmClockClick(alarmClock)
                },

                onDeleteClick = {
                    onDeleteClick(alarmClock)
                    alarmClocksState.filterNot { it == alarmClock }
                },

                turnOnAlarmClock = {
                    checkPostNotificationPermission(
                        context = context,
                        onGranted = {
                            turnOnAlarmClock(alarmClock)
                        },
                        onDenied = {
                            pendingAlarmClock = alarmClock
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        },
                    )
                },

                turnOffAlarmClock = {
                    turnOffAlarmClock(alarmClock)
                }
            )

            if (alarmClocksState.indexOf(alarmClock) != alarmClocksState.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun AlarmClockItem(
    modifier: Modifier,
    alarmClock: AlarmClock,
    onAlarmClockClick: () -> Unit,
    onDeleteClick: () -> Unit,
    turnOnAlarmClock: () -> Unit,
    turnOffAlarmClock: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val checked = alarmClock.isEnabled

    Box(
        modifier = modifier
            .clickable { onAlarmClockClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, start = 16.dp, bottom = 2.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 2.dp, end = 8.dp),
                text = alarmClock.name ?: "",
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = TimeHelper.getFormattedTime(
                        LocalTime.of(alarmClock.time.hour, alarmClock.time.minute)
                    ),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            if (it)
                                turnOnAlarmClock()
                            else
                                turnOffAlarmClock()
                        }
                    )

                    IconButton(
                        onClick = { expanded = true }
                    ) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.delete)) },
                            onClick = {
                                expanded = false
                                onDeleteClick()
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                            }
                        )
                    }
                }
            }

            TimeUntilAlarmClockMessage(
                modifier = Modifier
                    .padding(start = 2.dp, end = 8.dp),
                isAlarmEnabled = checked,
                alarmClockTime = alarmClock.time,
            )
        }
    }
}