package ru.mamykin.foreignbooksreader.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.TextView

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter

import butterknife.BindView
import butterknife.OnClick
import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.common.UiUtils
import ru.mamykin.foreignbooksreader.presenters.BookDetailsPresenter
import ru.mamykin.foreignbooksreader.views.BookDetailsView

/**
 * Страница с информацией о книге
 */
class BookDetailsActivity : BaseActivity(), BookDetailsView {

    @BindView(R.id.tvName)
    protected var tvBookName: TextView? = null
    @BindView(R.id.fabRead)
    protected var fabRead: FloatingActionButton? = null
    @BindView(R.id.tvAuthor)
    protected var tvBookAuthor: TextView? = null
    @BindView(R.id.tvBookPath)
    protected var tvBookPath: TextView? = null
    @BindView(R.id.tvBookCurrentPage)
    protected var tvBookCurrentPage: TextView? = null
    @BindView(R.id.tvBookGenre)
    protected var tvBookGenre: TextView? = null
    @BindView(R.id.tvBookLanguage)
    protected var tvBookLanguage: TextView? = null
    @BindView(R.id.tvBookCreatedDate)
    protected var tvBookCreatedDate: TextView? = null

    @InjectPresenter
    internal var presenter: BookDetailsPresenter? = null

    @ProvidePresenter
    internal fun provideBookDetailsPresenter(): BookDetailsPresenter {
        return BookDetailsPresenter(intent.extras!!.getInt(BOOK_ID_EXTRA))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.about_book), true)
    }

    override fun getLayout(): Int {
        return R.layout.activity_book_detail
    }

    override fun showTitle(title: String) {
        UiUtils.setTitle(this, title)
    }

    override fun setHomeEnabled(enabled: Boolean) {
        UiUtils.setHomeEnabled(this, enabled)
    }

    override fun showBookName(name: String) {
        tvBookName!!.text = name
    }

    override fun showBookAuthor(author: String) {
        tvBookAuthor!!.text = author
    }

    override fun showBookPath(path: String) {
        tvBookPath!!.text = path
    }

    override fun showBookCurrentPage(currentPage: String) {
        tvBookCurrentPage!!.text = currentPage
    }

    override fun showBookGenre(genre: String) {
        tvBookGenre!!.text = genre
    }

    override fun showBookOriginalLang(lang: String) {
        tvBookLanguage!!.text = lang
    }

    override fun showBookCreatedDate(date: String) {
        tvBookCreatedDate!!.text = date
    }

    override fun openBook(bookId: Int) {
        startActivity(ReadBookActivity.getStartIntent(this, bookId))
    }

    @OnClick(R.id.fabRead)
    fun onReadClicked() {
        presenter!!.onReadClicked()
    }

    companion object {
        private val BOOK_ID_EXTRA = "book_id_extra"

        fun getStartIntent(context: Context, bookId: Int): Intent {
            val bookDetailsIntent = Intent(context, BookDetailsActivity::class.java)
            bookDetailsIntent.putExtra(BookDetailsActivity.BOOK_ID_EXTRA, bookId)
            return bookDetailsIntent
        }
    }
}