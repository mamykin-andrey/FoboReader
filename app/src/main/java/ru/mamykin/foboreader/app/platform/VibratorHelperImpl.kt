package ru.mamykin.foboreader.app.platform

import android.annotation.SuppressLint
import android.content.Context
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import android.view.View
import ru.mamykin.foboreader.app.domain.GetVibrationEnabled
import ru.mamykin.foboreader.core.platform.OsHelper
import ru.mamykin.foboreader.core.platform.VibratorHelper
import javax.inject.Inject

internal class VibratorHelperImpl @Inject constructor(
    context: Context,
    private val getVibrationEnabled: GetVibrationEnabled
) : VibratorHelper {

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    @SuppressLint("MissingPermission")
    override fun vibrate(view: View) {
        if (!isClickVibrationEnabled()) return

        if (OsHelper.isM()) {
            view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        } else {
            vibrator.vibrate(20)
        }
    }

    private fun isClickVibrationEnabled(): Boolean {
        return getVibrationEnabled.execute()
    }
}