package ru.mamykin.foboreader.ui.readbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_read_book.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.common.UiUtils
import ru.mamykin.foboreader.extension.addGlobalLayoutListener
import ru.mamykin.foboreader.extension.isVisible
import ru.mamykin.foboreader.presentation.readbook.ReadBookPresenter
import ru.mamykin.foboreader.presentation.readbook.ReadBookView
import ru.mamykin.foboreader.ui.global.BaseActivity
import ru.mamykin.foboreader.ui.global.control.SwipeableTextView

/**
 * Страница чтения книги
 */
class ReadBookActivity : BaseActivity(), ReadBookView, SwipeableTextView.SwipeableListener {

    companion object {

        private const val BOOK_PATH_EXTRA = "book_path_extra"
        private const val BOOK_ID_EXTRA = "book_id_extra"

        fun getStartIntent(context: Context, bookId: Int): Intent {
            val readIntent = Intent(context, ReadBookActivity::class.java)
            readIntent.putExtra(BOOK_ID_EXTRA, bookId)
            return readIntent
        }

        fun getStartIntent(context: Context, bookPath: String): Intent {
            val readIntent = Intent(context, ReadBookActivity::class.java)
            readIntent.putExtra(BOOK_PATH_EXTRA, bookPath)
            return readIntent
        }
    }

    override val layout: Int = R.layout.activity_read_book

    @InjectPresenter
    lateinit var presenter: ReadBookPresenter

    @ProvidePresenter
    internal fun providePresenter(): ReadBookPresenter {
        if (intent.extras.containsKey(BOOK_PATH_EXTRA)) {
            return ReadBookPresenter(intent.extras.getString(BOOK_PATH_EXTRA))
        } else {
            return ReadBookPresenter(intent.extras.getInt(BOOK_ID_EXTRA))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvText.setSwipeableListener(this)
    }

    override fun showToast(text: String) {
        UiUtils.showToast(this, text)
    }

    override fun showLoading(show: Boolean) {
        pbLoading.isVisible = show
    }

    override fun displayBookName(name: String) {
        tvName.text = name
    }

    override fun onClick(paragraph: String) {
        presenter.onTranslateParagraphClicked(paragraph)
    }

    override fun onLongClick(word: String) {
        presenter.onWordClicked(word)
    }

    override fun onSwipeLeft() {
        presenter.onSwipeLeft()
    }

    override fun onSwipeRight() {
        presenter.onSwipeRight()
    }

    override fun displayReadPages(text: String) {
        tvRead.text = text
    }

    override fun displayReadPercent(text: String) {
        tvReadPercent.text = text
    }

    override fun displaySourceParagraph(text: CharSequence) {
        tvText.text = text
        tvText.updateWordLinks()
    }

    override fun displayParagraphTranslation(text: String) {
        tvText.setTranslation(text)
    }

    override fun displayWordTranslation(original: String, translation: String) {
        UiUtils.showWordPopup(this, original, translation, presenter::onSpeakWordClicked)
    }

    override fun initBookView(title: String, text: String) {
        tvText.addGlobalLayoutListener(presenter::onViewInitCompleted)
    }

    override fun showBookContent(show: Boolean) {
        llBookContent.isVisible = show
    }
}