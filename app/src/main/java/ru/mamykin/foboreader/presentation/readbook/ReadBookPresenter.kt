package ru.mamykin.foboreader.presentation.readbook

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.readbook.ReadBookInteractor
import ru.mamykin.foboreader.domain.readbook.ReadBookState
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.entity.ViewParams
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
                .unsubscribeOnDestroy()
    }

    fun onWordClicked(word: String) {
        interactor.getTextTranslation(word)
                .applySchedulers()
                .doOnSubscribe { viewState.showWordLoading(true) }
                .doAfterTerminate { viewState.showWordLoading(false) }
                .subscribe({ viewState.showWordTranslation(it.first, it.second) }, { it.printStackTrace() })
                .unsubscribeOnDestroy()
    }

    fun onSpeakWordClicked(word: String) {
        interactor.voiceWord(word)
    }

    /**
     * Called when view are ready to drawing and it have measured
     */
    fun onViewInitCompleted(viewParams: ViewParams) {
        interactor.initPaginator(viewParams)
                .applySchedulers()
                .subscribe(this::showCurrentPage, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    fun onSwipeRight() {
        interactor.getPrevPage()
                .applySchedulers()
                .subscribe(this::showPageContent, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    fun onSwipeLeft() {
        interactor.getNextPage()
                .applySchedulers()
                .subscribe(this::showPageContent, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    private fun showCurrentPage() {
        interactor.getCurrentPage()
                .applySchedulers()
                .subscribe(this::showPageContent, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    private fun loadBookInfo() {
        interactor.getBookInfo()
                .applySchedulers()
                .doOnSubscribe { viewState.showLoading(true) }
                .doAfterTerminate { viewState.showLoading(false) }
                .subscribe(this::showBookInfo, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    private fun showBookInfo(book: FictionBook) = with(viewState) {
        initBookView()
        showBookName(book.bookTitle)
    }

    private fun showPageContent(state: ReadBookState) = with(viewState) {
        showReaded(state.currentPage, state.pagesCount)
        showReadPercent(state.readPercent)
        showPageText(state.currentPageText)
    }
}