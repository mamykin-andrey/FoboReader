package ru.mamykin.foboreader.ui.bookdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_book_detail.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.common.Utils
import ru.mamykin.foboreader.di.modules.BookDetailsModule
import ru.mamykin.foboreader.presentation.bookdetails.BookDetailsPresenter
import ru.mamykin.foboreader.presentation.bookdetails.BookDetailsView
import ru.mamykin.foboreader.ui.global.BaseActivity
import java.util.*
import javax.inject.Inject

/**
 * Страница с информацией о книге
 */
class BookDetailsActivity : BaseActivity(), BookDetailsView {

    override val layout: Int = R.layout.activity_book_detail

    companion object {

        private const val BOOK_ID_EXTRA = "book_id_extra"

        fun getStartIntent(context: Context, bookId: Int): Intent {
            val bookDetailsIntent = Intent(context, BookDetailsActivity::class.java)
            bookDetailsIntent.putExtra(BOOK_ID_EXTRA, bookId)
            return bookDetailsIntent
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: BookDetailsPresenter

    @ProvidePresenter
    fun providePresenter(): BookDetailsPresenter {
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.about_book), true)
        fabRead.setOnClickListener { presenter.onReadBookClicked() }
    }

    override fun injectDependencies() {
        val bookId = intent.getIntExtra(BOOK_ID_EXTRA, -1)
        val bookDetailsRouter = BookDetailsRouter(this)
        val bookDetailsModule = BookDetailsModule(bookDetailsRouter, bookId)
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
        tvBookCurrentPage.text = currentPage
    }

    override fun showBookGenre(genre: String) {
        tvBookGenre.text = genre
    }

    override fun showBookOriginalLang(lang: String) {
        tvBookLanguage.text = lang
    }

    override fun showBookCreatedDate(date: Date) {
        tvBookCreatedDate.text = Utils.getFormattedDate(date)
    }
}