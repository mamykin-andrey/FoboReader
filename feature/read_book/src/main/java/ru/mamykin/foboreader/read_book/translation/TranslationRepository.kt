package ru.mamykin.foboreader.read_book.translation

internal interface TranslationRepository {
    suspend fun getTranslation(text: String): Result<TextTranslation>
}