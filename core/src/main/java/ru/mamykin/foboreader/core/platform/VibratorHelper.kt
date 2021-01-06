package ru.mamykin.foboreader.core.platform

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class VibratorHelper(
    private val context: Context
) {
    fun shortVibrate() {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v!!.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v!!.vibrate(20)
        }
    }
}