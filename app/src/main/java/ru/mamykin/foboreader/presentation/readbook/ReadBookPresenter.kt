package ru.mamykin.foboreader.presentation.readbook

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.readbook.ReadBookInteractor
import ru.mamykin.foboreader.domain.readbook.ReadBookState
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.extension.ViewParams
import ru.mamykin.foboreader.extension.applySchedulers
import ru.mamykin.foboreader.presentation.global.BasePresenter
import javax.inject.Inject

@InjectViewState
class ReadBookPresenter @Inject constructor(
        private val interactor: ReadBookInteractor
) : BasePresenter<ReadBookView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadBookInfo()
    }

    fun onParagraphClicked(paragraph: String) {
        interactor.getTextTranslation(paragraph)
                .applySchedulers()
                .doOnSubscribe { viewState.showParagraphLoading(true) }
                .doAfterTerminate { viewState.showParagraphLoading(false) }
                .map { it.second }
                .subscribe(viewState::showParagraphTranslation, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    fun onWordClicked(word: String) {
        interactor.getTextTranslation(word)
                .doOnSubscribe { viewState.showWordLoading(true) }
                .doAfterTerminate { viewState.showWordLoading(false) }
                .subscribe(viewState::showWordTranslation)
                .unsubscribeOnDestory()
    }

    fun onSpeakWordClicked(word: String) {
        interactor.voiceWord(word)
    }

    /**
     * Вызывается когда View готов к отрисовке, и известны параметры его размеров
     */
    fun onViewInitCompleted(viewParams: ViewParams) {
        interactor.onViewInitCompleted(viewParams)
        val bookState = interactor.getBookState()
        displayPageContent(bookState)
    }

    fun onSwipeRight() {
        val bookState = interactor.getPrevPage()
        displayPageContent(bookState)
    }

    fun onSwipeLeft() {
        val bookState = interactor.getNextPage()
        displayPageContent(bookState)
    }

    private fun loadBookInfo() {
        interactor.getBookInfo()
                .applySchedulers()
                .doOnSubscribe { viewState.showLoading(true) }
                .doAfterTerminate { viewState.showLoading(false) }
                .subscribe(this::displayBookInfo, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    private fun displayBookInfo(book: FictionBook) {
        viewState.initBookView()
        viewState.showBookName(book.bookTitle)
    }

    private fun displayPageContent(state: ReadBookState) {
        viewState.showReaded(state.currentPage, state.pagesCount)
        viewState.showReadPercent(state.readPercent)
        viewState.showPageText(state.currentPageText)
    }
}