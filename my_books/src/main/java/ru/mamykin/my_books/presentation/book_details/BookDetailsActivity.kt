package ru.mamykin.my_books.presentation.book_details

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_book_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.core.extension.getLongExtra
import ru.mamykin.core.extension.showSnackbar
import ru.mamykin.core.extension.startActivity
import ru.mamykin.core.ui.BaseActivity
import ru.mamykin.my_books.R
import ru.mamykin.my_books.domain.model.BookInfo

class BookDetailsActivity : BaseActivity(R.layout.activity_book_detail) {

    companion object {

        private const val EXTRA_BOOK_ID = "book_id"

        fun start(context: Context, bookId: Long) {
            context.startActivity<BookDetailsActivity>(EXTRA_BOOK_ID to bookId)
        }
    }

    private val bookDetailsViewModel: BookDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.about_book), true)
        fabRead.setOnClickListener {
            bookDetailsViewModel.onEvent(BookDetailsViewModel.Event.OnReadBookClicked)
        }
        initViewModel()
    }

    private fun initViewModel() {
        bookDetailsViewModel.stateLiveData.observe(this, Observer { state ->
            if (state.error) showSnackbar(R.string.book_details_load_info_error)
            state.bookInfo?.let(::showBookInfo)
        })

        val bookId = intent.getLongExtra(EXTRA_BOOK_ID) ?: return
        bookDetailsViewModel.loadData(bookId)
    }

    private fun showBookInfo(book: BookInfo) {
        tvBookName.text = book.title
        tvBookAuthor.text = book.author
        tvBookPath.text = book.filePath
        tvCurrentPage.text = book.currentPage.toString()
        tvBookGenre.text = book.genre
//        tvBookLanguage.text = book.bookSrcLang!!
//        tvBookCreatedDate.text = book.docDate!!.asFormattedDate()
    }
}