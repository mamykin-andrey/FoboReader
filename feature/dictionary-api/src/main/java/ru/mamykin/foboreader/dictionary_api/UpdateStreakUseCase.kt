package ru.mamykin.foboreader.dictionary_api

import kotlinx.coroutines.CoroutineScope

interface UpdateStreakUseCase {
    fun executeAsync(scope: CoroutineScope)
} 