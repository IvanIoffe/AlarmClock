package com.ioffeivan.alarmclock.vibrator

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class VibratorControllerImpl(context: Context) : VibratorController {
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private val vibrationEffect = VibrationEffect.createWaveform(longArrayOf(500, 500), 0)

    override fun vibrate() {
        if (!vibrator.hasVibrator()) return

        vibrator.vibrate(vibrationEffect)
    }

    override fun cancel() {
        vibrator.cancel()
    }
}