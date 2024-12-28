package ru.mamykin.foboreader.core.presentation

import android.content.Context
import androidx.annotation.StringRes

sealed class StringOrResource {
    class String(
        val value: kotlin.String
    ) : StringOrResource()

    class Resource(
        @StringRes val stringRes: Int, vararg val formatArgs: Any,
    ) : StringOrResource()

    fun toString(context: Context) = when (this) {
        is String -> this.value
        is Resource -> context.getString(this.stringRes, *this.formatArgs)
    }
}