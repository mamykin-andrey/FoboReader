package ru.mamykin.foboreader.ui.readbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_read_book.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.di.modules.ReadBookModule
import ru.mamykin.foboreader.extension.addGlobalLayoutListener
import ru.mamykin.foboreader.extension.isVisible
import ru.mamykin.foboreader.presentation.readbook.ReadBookPresenter
import ru.mamykin.foboreader.presentation.readbook.ReadBookView
import ru.mamykin.foboreader.ui.global.BaseActivity
import ru.mamykin.foboreader.ui.global.control.swipabletextview.OnActionListener
import ru.mamykin.foboreader.ui.global.control.swipabletextview.OnSwipeListener
import javax.inject.Inject

/**
 * Страница чтения книги
 */
class ReadBookActivity : BaseActivity(), ReadBookView, OnActionListener, OnSwipeListener {

    companion object {

        private const val BOOK_PATH_EXTRA = "book_path_extra"

        fun getStartIntent(context: Context, bookPath: String): Intent {
            val readIntent = Intent(context, ReadBookActivity::class.java)
            readIntent.putExtra(BOOK_PATH_EXTRA, bookPath)
            return readIntent
        }
    }

    override val layout: Int = R.layout.activity_read_book

    @Inject
    @InjectPresenter
    lateinit var presenter: ReadBookPresenter

    @ProvidePresenter
    internal fun providePresenter(): ReadBookPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvText.setOnActionListener(this)
        tvText.setOnSwipeListener(this)
    }

    override fun injectDependencies() {
        super.injectDependencies()
        val module = ReadBookModule(intent.getStringExtra(BOOK_PATH_EXTRA))
        getAppComponent().getReadBookComponent(module).inject(this)
    }

    override fun showLoading(show: Boolean) {
        pbLoading.isVisible = show
    }

    override fun showBookName(name: String) {
        tvName.text = name
    }

    override fun onClick(paragraph: String) {
        presenter.onParagraphClicked(paragraph)
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

    override fun showParagraphTranslation(text: String) {
        //tvText.setTranslation(text)
    }

    override fun initBookView() {
        tvText.addGlobalLayoutListener(presenter::onViewInitCompleted)
    }

    override fun showParagraphLoading(show: Boolean) {
    }

    override fun showWordLoading(show: Boolean) {
    }

    override fun showReadPercent(percent: Float) {
        tvReadPercent.text = getString(R.string.read_percent_string, percent)
    }

    override fun showWordTranslation(word: String, translation: String) {
        // TODO: show word popup
    }

    override fun showPageText(text: CharSequence) {
        tvText.text = text
    }

    override fun showReaded(currentPage: Int, pagesCount: Int) {
        tvRead.text = getString(R.string.read_pages_format, currentPage, pagesCount)
    }
}