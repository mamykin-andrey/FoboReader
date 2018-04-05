package ru.mamykin.foboreader.domain.readbook

import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.data.repository.translate.TranslateRepository
import ru.mamykin.foboreader.di.qualifiers.BookPath
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.entity.ViewParams
import rx.Completable
import rx.Single
import javax.inject.Inject

class ReadBookInteractor @Inject constructor(
        private val booksRepository: BooksRepository,
        private val translateRepository: TranslateRepository,
        private val textToSpeechService: TextToSpeechService,
        @BookPath private val bookPath: String
) {

    private lateinit var paginator: Paginator
    private lateinit var book: FictionBook

    fun getBookInfo(): Single<FictionBook> {
        return booksRepository.getBook(bookPath).doOnSuccess { this.book = it }
    }

    fun getTextTranslation(text: String): Single<Pair<String, String>> {
        val offlineTranslation = book.transMap[text]

        if (offlineTranslation != null) {
            return Single.just(Pair(text, offlineTranslation))
        }

        return translateRepository.getTextTranslation(text)
                .map { Pair(text, it.text.joinToString()) }
    }

    fun voiceWord(word: String) = textToSpeechService.voiceWord(word)

    fun initPaginator(viewParams: ViewParams): Completable {
        paginator = Paginator(
                book.fullText,
                viewParams.width,
                viewParams.height,
                viewParams.paint,
                viewParams.lineSpacingMultiplier,
                viewParams.lineSpacingExtra,
                viewParams.includeFontPadding
        )
        book.pagesCount = paginator.pagesCount
        return booksRepository.updateBook(book)
    }

    fun getCurrentPage(): Single<ReadBookState> = Single.just(getBookState())

    fun getNextPage(): Single<ReadBookState> {
        if (paginator.currentIndex < paginator.pagesCount - 1) {
            paginator.currentIndex++
        }
        return Single.just(getBookState())
    }

    fun getPrevPage(): Single<ReadBookState> {
        if (paginator.currentIndex > 0) {
            paginator.currentIndex--
        }
        return Single.just(getBookState())
    }

    private fun getBookState(): ReadBookState {
        return ReadBookState(
                paginator.currentIndex + 1,
                paginator.pagesCount,
                paginator.currentPage,
                paginator.readPercent
        )
    }
}