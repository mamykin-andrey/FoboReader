package ru.mamykin.foboreader.dictionary_api

interface GetStreakInfoUseCase {
    suspend fun execute(): StreakInfo
}