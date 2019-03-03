package ru.mamykin.foboreader.presentation.bookdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_book_detail.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.di.modules.BookDetailsModule
import ru.mamykin.foboreader.core.extension.asFormattedDate
import ru.mamykin.foboreader.core.ui.BaseActivity
import java.util.*

/**
 * Страница с информацией о книге
 */
class BookDetailsActivity : BaseActivity(), BookDetailsView {

    companion object {

        private const val BOOK_PATH_EXTRA = "book_path_extra"

        fun start(context: Context, bookPath: String) =
                context.startActivity(Intent(context, BookDetailsActivity::class.java)
                        .apply {
                            putExtra(BOOK_PATH_EXTRA, bookPath)
                        })
    }

    override val layout: Int = R.layout.activity_book_detail

    @InjectPresenter
    lateinit var presenter: BookDetailsPresenter

    @ProvidePresenter
    fun providePresenter(): BookDetailsPresenter = intent.getStringExtra(BOOK_PATH_EXTRA).let {
        getAppComponent()
                .getBookDetailsComponent(BookDetailsModule(it))
                .getBookDetailsPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.about_book), true)
        fabRead.setOnClickListener { presenter.onReadBookClicked() }
        presenter.router = BookDetailsRouter(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.router = null
    }

    override fun showTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setHomeEnabled(enabled: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(enabled)
    }

    override fun showBookName(name: String) {
        tvBookName.text = name
    }

    override fun showBookAuthor(author: String) {
        tvBookAuthor.text = author
    }

    override fun showBookPath(path: String) {
        tvBookPath.text = path
    }

    override fun showBookCurrentPage(currentPage: String) {
        tvCurrentPage.text = currentPage
    }

    override fun showBookGenre(genre: String) {
        tvBookGenre.text = genre
    }

    override fun showBookOriginalLang(lang: String) {
        tvBookLanguage.text = lang
    }

    override fun showBookCreatedDate(date: Date) {
        tvBookCreatedDate.text = date.asFormattedDate()
    }
}