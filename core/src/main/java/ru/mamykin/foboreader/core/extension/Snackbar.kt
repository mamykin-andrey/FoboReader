package ru.mamykin.foboreader.core.extension

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import ru.mamykin.foboreader.core.presentation.SnackbarData

suspend fun SnackbarHostState.showSnackbarWithData(
    data: SnackbarData,
    context: Context,
    duration: SnackbarDuration = if (data.action != null) SnackbarDuration.Indefinite else SnackbarDuration.Short,
    withDismissAction: Boolean = false,
): Boolean {
    val (message, action) = data
    val result = this.showSnackbar(
        message = message.toString(context),
        actionLabel = action?.first,
        duration = duration,
        withDismissAction = withDismissAction,
    )
    return if (result == SnackbarResult.ActionPerformed) {
        requireNotNull(action?.second).invoke()
        true
    } else false
}