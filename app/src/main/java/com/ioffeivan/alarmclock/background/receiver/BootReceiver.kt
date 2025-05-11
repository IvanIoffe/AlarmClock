package com.ioffeivan.alarmclock.background.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ioffeivan.alarmclock.background.worker.RestoreAlarmClocksWorker
import com.ioffeivan.alarmclock.core.utils.Action
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var workManager: WorkManager

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Action.ACTION_QUICKBOOT_POWERON,
            Action.ACTION_HTC_QUICKBOOT_POWERON -> {
                workManager.enqueue(
                    OneTimeWorkRequestBuilder<RestoreAlarmClocksWorker>().build()
                )
            }
        }
    }
}