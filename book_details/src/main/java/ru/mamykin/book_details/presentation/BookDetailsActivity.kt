package ru.mamykin.book_details.presentation

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_book_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.book_details.R
import ru.mamykin.core.data.model.FictionBook
import ru.mamykin.core.extension.asFormattedDate
import ru.mamykin.core.extension.showSnackbar
import ru.mamykin.core.extension.startActivity
import ru.mamykin.core.ui.BaseActivity

class BookDetailsActivity : BaseActivity(R.layout.activity_book_detail) {

    companion object {

        private const val BOOK_PATH_EXTRA = "book_path_extra"

        fun start(context: Context, bookPath: String) {
            context.startActivity<BookDetailsActivity>(BOOK_PATH_EXTRA to bookPath)
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
        bookDetailsViewModel.loadData(intent.getStringExtra(BOOK_PATH_EXTRA)!!)
        bookDetailsViewModel.stateLiveData.observe(this, Observer { state ->
            if (state.error) showSnackbar(R.string.book_details_load_info_error)
            state.book?.let(::showBookInfo)
        })
    }

    private fun showBookInfo(book: FictionBook) {
        tvBookName.text = book.bookTitle
        tvBookAuthor.text = book.bookAuthor!!
        tvBookPath.text = book.filePath
        tvCurrentPage.text = book.currentPage.toString()
        tvBookGenre.text = book.bookGenre!!
        tvBookLanguage.text = book.bookSrcLang!!
        tvBookCreatedDate.text = book.docDate!!.asFormattedDate()
    }
}