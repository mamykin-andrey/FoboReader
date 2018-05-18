package ru.mamykin.foboreader.entity

import android.text.TextPaint

data class ViewParams(
        val width: Int,
        val height: Int,
        val paint: TextPaint,
        val lineSpacingMultiplier: Float,
        val lineSpacingExtra: Float,
        val includeFontPadding: Boolean
)