package ru.mamykin.core.extension

import android.content.Intent

/**
 * Получение неотрицательного extra типа [Long] по [name]
 */
fun Intent.getLongExtra(name: String): Long? {
    return getLongExtra(name, -1).takeIf { it != -1L }
}