package com.ioffeivan.alarmclock.background.scheduler

import com.ioffeivan.alarmclock.core.domain.model.AlarmClock

interface AlarmClockScheduler {
    fun schedule(alarmClock: AlarmClock)
    fun cancel(alarmClock: AlarmClock)
}