package ru.mamykin.foboreader.dictionary_api

interface StreakManagerUseCase {
    fun updateStreakForCompletion()
    fun getCurrentStreak(): Int
    fun getBestStreak(): Int
} 