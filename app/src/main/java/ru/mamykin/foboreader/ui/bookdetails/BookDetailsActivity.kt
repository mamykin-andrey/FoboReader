package ru.mamykin.foboreader.ui.bookdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_book_detail.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.di.modules.BookDetailsModule
import ru.mamykin.foboreader.extension.asFormattedDate
import ru.mamykin.foboreader.presentation.bookdetails.BookDetailsPresenter
import ru.mamykin.foboreader.presentation.bookdetails.BookDetailsView
import ru.mamykin.foboreader.ui.global.BaseActivity
import java.util.*
import javax.inject.Inject

/**
 * Страница с информацией о книге
 */
class BookDetailsActivity : BaseActivity(), BookDetailsView {

    companion object {

        private const val BOOK_PATH_EXTRA = "book_path_extra"

        fun getStartIntent(context: Context, bookPath: String): Intent {
            val bookDetailsIntent = Intent(context, BookDetailsActivity::class.java)
            bookDetailsIntent.putExtra(BOOK_PATH_EXTRA, bookPath)
            return bookDetailsIntent
        }
    }

    override val layout: Int = R.layout.activity_book_detail

    // TODO: get presenter from component
    @Inject
    @InjectPresenter
    lateinit var presenter: BookDetailsPresenter

    @ProvidePresenter
    fun providePresenter(): BookDetailsPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.about_book), true)
        fabRead.setOnClickListener { presenter.onReadBookClicked() }
    }

    override fun injectDependencies() {
        super.injectDependencies()
        val bookPath = intent.getStringExtra(BOOK_PATH_EXTRA)
        val bookDetailsRouter = BookDetailsRouter(this)
        val bookDetailsModule = BookDetailsModule(bookDetailsRouter, bookPath)
        getAppComponent().getBookDetailsComponent(bookDetailsModule).inject(this)
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