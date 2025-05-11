package com.ioffeivan.alarmclock.core.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ioffeivan.alarmclock.R
import com.ioffeivan.alarmclock.background.receiver.AlarmClockReceiver
import com.ioffeivan.alarmclock.core.utils.Action
import com.ioffeivan.alarmclock.core.utils.AlarmClockKeys
import com.ioffeivan.alarmclock.core.utils.TimeFormatter
import com.ioffeivan.alarmclock.ui.theme.AlarmClockTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@AndroidEntryPoint
class AlarmClockActionActivity : ComponentActivity() {

    private var finishActivityReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val alarmClockId = intent.getLongExtra(AlarmClockKeys.ID_KEY, -1)
        val alarmClockName = intent.getStringExtra(AlarmClockKeys.NAME_KEY)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        setContent {
            AlarmClockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    AlarmClockAction(
                        alarmClockName = alarmClockName ?: "",
                        onSnooze = {
                            sendBroadcast(
                                Intent(this, AlarmClockReceiver::class.java).apply {
                                    action = Action.ACTION_SNOOZE_ALARM_CLOCK
                                    putExtra(AlarmClockKeys.ID_KEY, alarmClockId)
                                }
                            )
                        },
                        onStop = {
                            sendBroadcast(
                                Intent(this, AlarmClockReceiver::class.java).apply {
                                    action = Action.ACTION_STOP_ALARM_CLOCK
                                    putExtra(AlarmClockKeys.ID_KEY, alarmClockId)
                                }
                            )
                        },
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()

        finishActivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Action.ACTION_FINISH_ALARM_CLOCK_ACTIVITY) {
                    finish()
                }
            }
        }

        registerReceiver(
            finishActivityReceiver,
            IntentFilter(Action.ACTION_FINISH_ALARM_CLOCK_ACTIVITY),
            RECEIVER_NOT_EXPORTED
        )
    }

    override fun onResume() {
        super.onResume()

        volumeControlStream = AudioManager.STREAM_ALARM
    }

    override fun onPause() {
        super.onPause()

        volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(finishActivityReceiver)
    }
}

@Composable
private fun AlarmClockAction(
    alarmClockName: String,
    onSnooze: () -> Unit,
    onStop: () -> Unit,
) {
    var time by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (isActive) {
            val currentTime = TimeFormatter.getFormattedCurrentTime()
            if (currentTime != time)
                time = currentTime
            delay(100)
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.7f),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = time,
                        fontSize = 84.sp,
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = alarmClockName,
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onSnooze() }
            ) {
                Text(
                    text = stringResource(id = R.string.snooze_alarm_clock),
                    textAlign = TextAlign.Center
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onStop() }
            ) {
                Text(
                    text = stringResource(id = R.string.turn_off_alarm_clock),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}