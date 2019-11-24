package ru.mamykin.core.platform

import android.text.TextPaint

data class ViewParams(
        val width: Int,
        val height: Int,
        val paint: TextPaint,
        val spacingMult: Float,
        val spacingAdd: Float,
        val includePad: Boolean
)