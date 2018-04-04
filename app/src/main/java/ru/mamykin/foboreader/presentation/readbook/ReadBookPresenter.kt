package ru.mamykin.foboreader.presentation.readbook

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.readbook.ReadBookInteractor
import ru.mamykin.foboreader.domain.readbook.ReadBookState
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.extension.ViewParams
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

    fun onTranslateParagraphClicked(paragraph: String) {
        interactor.getTextTranslation(paragraph)
                .doOnSubscribe { viewState.showParagraphLoading(true) }
                .doAfterTerminate { viewState.showParagraphLoading(false) }
                .map { it.second }
                .subscribe(viewState::showParagraphTranslation)
                .unsubscribeOnDestory()
    }

    fun onHideParagraphClicked() {
        viewState.showSourceParagraph()
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
//        interactor.getLastReadedPage()
//                .subscribe(this::displayPageContent, Throwable::printStackTrace)
//                .unsubscribeOnDestory()
    }

    fun onSwipeRight() {
        interactor.getPrevPage()
                .subscribe(this::displayPageContent, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    fun onSwipeLeft() {
        interactor.getNextPage()
                .subscribe(this::displayPageContent, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    private fun loadBookInfo() {
        interactor.loadBookInfo()
                .doOnSubscribe { viewState.showLoading(true) }
                .doAfterTerminate { viewState.showLoading(false) }
                .subscribe(this::displayBookInfo, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    private fun displayBookInfo(book: FictionBook) {
        viewState.initBookView()
        viewState.showBookContent(true)
        viewState.showBookName(book.bookTitle!!)
    }

    private fun displayPageContent(state: ReadBookState) {
        viewState.showCurrentPage(state.currentPage)
        viewState.showPageText(state.currentPageText)
        viewState.showReadPages(state.pagesRead)
        viewState.showReadPercent(state.readPercent)
    }
}