package com.ioffeivan.alarmclock.core.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.ioffeivan.alarmclock.R
import com.ioffeivan.alarmclock.core.utils.PluralDurationFormatter
import com.ioffeivan.alarmclock.core.utils.getTimeMillisUntilAlarmClock
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.LocalTime

@Composable
fun TimeUntilAlarmClockMessage(
    modifier: Modifier,
    isAlarmEnabled: Boolean = true,
    alarmClockTime: LocalTime,
) {
    val context = LocalContext.current
    val pluralDurationFormatter = remember { PluralDurationFormatter(context) }
    var timeUntilAlarmClockMessage by remember { mutableStateOf("") }

    LaunchedEffect(isAlarmEnabled, alarmClockTime) {
        while (isActive && isAlarmEnabled) {
            val timeMillisUntilAlarmClock = getTimeMillisUntilAlarmClock(alarmClockTime)
            val formattedTime = pluralDurationFormatter.formatDuration(timeMillisUntilAlarmClock)

            if (formattedTime != timeUntilAlarmClockMessage)
                timeUntilAlarmClockMessage =
                    context.getString(R.string.will_work_after, formattedTime)
            delay(100)
        }
    }

    Text(
        modifier = modifier,
        text = if (isAlarmEnabled) timeUntilAlarmClockMessage else "",
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}