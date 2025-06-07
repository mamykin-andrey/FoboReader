package ru.mamykin.foboreader.dictionary_api

interface GetWordsToLearnUseCase {
    suspend fun execute(): List<DictionaryWord>
}