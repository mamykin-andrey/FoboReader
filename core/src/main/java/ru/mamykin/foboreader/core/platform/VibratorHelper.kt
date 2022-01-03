package ru.mamykin.foboreader.core.platform

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import ru.mamykin.foboreader.core.domain.usecase.GetVibrationEnabled
import javax.inject.Inject

class VibratorHelper @Inject constructor(
    context: Context,
    private val getVibrationEnabled: GetVibrationEnabled
) {
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    @SuppressLint("MissingPermission")
    fun clickVibrate() {
        if (!isClickVibrationEnabled()) return

        if (OsHelper.isQ()) {
            vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(20)
        }
    }

    private fun isClickVibrationEnabled(): Boolean {
        return getVibrationEnabled.execute().getOrThrow()
    }
}