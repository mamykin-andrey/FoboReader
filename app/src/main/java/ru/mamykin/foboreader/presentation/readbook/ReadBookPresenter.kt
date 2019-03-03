package ru.mamykin.foboreader.presentation.readbook

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.platform.ResourcesManager
import ru.mamykin.foboreader.core.platform.Schedulers
import ru.mamykin.foboreader.core.ui.BasePresenter
import ru.mamykin.foboreader.domain.readbook.ReadBookInteractor
import javax.inject.Inject

@InjectViewState
class ReadBookPresenter @Inject constructor(
        private val interactor: ReadBookInteractor,
        override val resourcesManager: ResourcesManager,
        override val schedulers: Schedulers
) : BasePresenter<ReadBookView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadBookInfo()
    }

    fun onParagraphClicked(paragraph: String) {
        interactor.getParagraphTranslation(paragraph)
                .applySchedulers()
                .showProgress(viewState::showParagraphLoading)
                .subscribe(
                        { viewState.showParagraphTranslation(it) },
                        { onError(R.string.error_translation) }
                )
                .unsubscribeOnDestroy()
    }

    fun onWordClicked(word: String) {
        interactor.getWordTranslation(word)
                .applySchedulers()
                .showProgress(viewState::showWordLoading)
                .subscribe(
                        { viewState.showWordTranslation(word, it) },
                        { onError(R.string.error_translation) }
                )
                .unsubscribeOnDestroy()
    }

    fun onSpeakWordClicked(word: String) {
        interactor.voiceWord(word)
    }

    private fun loadBookInfo() {
        interactor.getBookInfo()
                .applySchedulers()
                .showProgress()
                .subscribe(
                        { viewState.showBookName(it.bookTitle) },
                        { onError(R.string.error_book_loading) }
                )
                .unsubscribeOnDestroy()
    }
}