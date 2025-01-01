package ru.mamykin.foboreader.core.presentation

import android.content.Context
import androidx.annotation.StringRes

sealed class StringOrResource {

    data class String(
        val value: kotlin.String
    ) : StringOrResource()

    class Resource(
        @StringRes val stringRes: Int, vararg val formatArgs: Any,
    ) : StringOrResource() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Resource

            if (stringRes != other.stringRes) return false
            if (!formatArgs.contentEquals(other.formatArgs)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = stringRes
            result = 31 * result + formatArgs.contentHashCode()
            return result
        }

        override fun toString(): kotlin.String {
            return "Resource(stringRes=$stringRes, formatArgs=${formatArgs.contentToString()})"
        }
    }

    fun toString(context: Context) = when (this) {
        is String -> this.value
        is Resource -> context.getString(this.stringRes, *this.formatArgs)
    }
}