package ru.mamykin.foboreader.read_book.reader

import android.view.HapticFeedbackConstants
import android.view.View
import javax.inject.Inject

internal class VibrationManagerImpl @Inject constructor(
    private val getVibrationEnabled: GetVibrationEnabled
) : VibrationManager {

    override fun vibrate(view: View) {
        if (isClickVibrationEnabled()) {
            view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
    }

    private fun isClickVibrationEnabled(): Boolean {
        return getVibrationEnabled.execute()
    }
}