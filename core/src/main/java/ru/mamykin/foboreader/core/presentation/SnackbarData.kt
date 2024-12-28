package ru.mamykin.foboreader.core.presentation

typealias SnackbarAction = Pair<String, suspend () -> Unit>

data class SnackbarData(
    val message: StringOrResource,
    val action: SnackbarAction? = null,
)