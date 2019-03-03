package ru.mamykin.foboreader.domain.readbook

import ru.mamykin.foboreader.core.di.qualifiers.BookPath
import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.data.repository.translate.TranslateRepository
import ru.mamykin.foboreader.domain.entity.FictionBook
import rx.Single
import javax.inject.Inject

class ReadBookInteractor @Inject constructor(
        private val booksRepository: BooksRepository,
        private val translateRepository: TranslateRepository,
        private val textToSpeechService: TextToSpeechService,
        @BookPath private val bookPath: String
) {
    private lateinit var book: FictionBook

    fun getBookInfo(): Single<FictionBook> =
            booksRepository.getBook(bookPath)
                    .doOnSuccess { this.book = it }

    fun getParagraphTranslation(paragraph: String): Single<String> =
            book.transMap[paragraph]?.let { Single.just(it) }
                    ?: translateRepository.getTextTranslation(paragraph)

    fun getWordTranslation(word: String): Single<String> =
            translateRepository.getTextTranslation(word)

    fun voiceWord(word: String) = textToSpeechService.voiceWord(word)
}