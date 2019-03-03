package ru.mamykin.foboreader.presentation.readbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_read_book.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.di.modules.ReadBookModule
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.ui.BaseActivity
import ru.mamykin.paginatedtextview.pagination.ReadState
import ru.mamykin.paginatedtextview.view.OnActionListener

/**
 * Страница чтения книги
 */
class ReadBookActivity : BaseActivity(), ReadBookView {

    companion object {

        private const val BOOK_PATH_EXTRA = "book_path_extra"

        fun start(context: Context, bookPath: String) = context.startActivity(
                Intent(context, ReadBookActivity::class.java).apply {
                    putExtra(BOOK_PATH_EXTRA, bookPath)
                })
    }

    override val layout: Int = R.layout.activity_read_book

    @InjectPresenter
    lateinit var presenter: ReadBookPresenter

    @ProvidePresenter
    internal fun providePresenter(): ReadBookPresenter {
        val module = ReadBookModule(intent.getStringExtra(BOOK_PATH_EXTRA))
        return getAppComponent().getReadBookComponent(module).getReadBookPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvText.setOnActionListener(object : OnActionListener {
            override fun onClick(paragraph: String) {
                presenter.onParagraphClicked(paragraph)
            }

            override fun onLongClick(word: String) {
                presenter.onWordClicked(word)
            }

            override fun onPageLoaded(state: ReadState) = with(state) {
                tvReadPercent.text = getString(R.string.read_percent_string, readPercent)
                tvRead.text = getString(R.string.read_pages_format, currentIndex, pagesCount)
            }
        })
    }

    override fun showLoading(show: Boolean) {
        pbLoading.isVisible = show
    }

    override fun showBookName(name: String) {
        tvName.text = name
    }

    override fun showParagraphTranslation(text: String) {

    }

    override fun showParagraphLoading(show: Boolean) {

    }

    override fun showWordLoading(show: Boolean) {

    }

    override fun showWordTranslation(word: String, translation: String) {

    }
}