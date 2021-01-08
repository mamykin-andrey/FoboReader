package ru.mamykin.foboreader.core.platform

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import ru.mamykin.foboreader.core.domain.usecase.GetVibrationEnabled

class VibratorHelper(
    context: Context,
    private val getVibrationEnabled: GetVibrationEnabled
) {
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val clickVibrationEnabled: Boolean
        get() = getVibrationEnabled(Unit).getOrThrow()

    @SuppressLint("MissingPermission")
    fun clickVibrate() {
        if (!clickVibrationEnabled) return

        if (OsHelper.isQ()) {
            vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(20)
        }
    }
}