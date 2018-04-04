package ru.mamykin.foboreader.domain.readbook

import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.data.repository.translate.TranslateRepository
import ru.mamykin.foboreader.di.qualifiers.BookId
import ru.mamykin.foboreader.di.qualifiers.BookPath
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.extension.ViewParams
import rx.Single
import javax.inject.Inject

class ReadBookInteractor @Inject constructor(
        private val booksRepository: BooksRepository,
        private val translateRepository: TranslateRepository,
        private val textToSpeechService: TextToSpeechService,
        @BookPath private val bookPath: String?,
        @BookId private val bookId: Int?
) {

    private lateinit var paginator: Paginator
    private lateinit var book: FictionBook

    fun loadBookInfo(): Single<FictionBook> {
        if (bookId == null && bookPath == null) {
            return Single.error(IllegalStateException("No book ID, or book path was set!"))
        }
        val bookObs = if (bookId != null) {
            booksRepository.getBook(bookId)
        } else {
            booksRepository.getBook(bookPath!!)
        }
        return bookObs.doOnSuccess { this.book = it }
    }

    fun getTextTranslation(text: String): Single<Pair<String, String>> {
        val offlineTranslation = book.transMap!![text]

        if (offlineTranslation != null) {
            return Single.just(Pair(text, offlineTranslation))
        }

        return translateRepository.getTextTranslation(text)
                .map { Pair(text, it.text!!.joinToString()) }
    }

    fun voiceWord(word: String) {
        textToSpeechService.voiceWord(word)
    }

    fun onViewInitCompleted(viewParams: ViewParams) {
        paginator = Paginator(
                book.fullText,
                viewParams.width,
                viewParams.height,
                viewParams.paint,
                viewParams.lineSpacingMultiplier,
                viewParams.lineSpacingExtra,
                viewParams.includeFontPadding
        )
    }

    fun getNextPage(): Single<ReadBookState> {
        val currentIndex = paginator.currentIndex
        val pagesCount = paginator.pagesCount
        if (currentIndex < pagesCount) {
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
                paginator.currentIndex,
                paginator.currentPage!!.toString(),
                paginator.currentIndex,
                calculatePageReadPercentage(book)
        )
    }

    private fun calculatePageReadPercentage(book: FictionBook): Float {
        return (book.currentPage / book.pagesCount).toFloat()
    }
}