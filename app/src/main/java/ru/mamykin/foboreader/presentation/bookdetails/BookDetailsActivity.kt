package ru.mamykin.foboreader.presentation.bookdetails

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_book_detail.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.asFormattedDate
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.extension.startActivity
import ru.mamykin.foboreader.core.ui.BaseActivity
import ru.mamykin.foboreader.domain.entity.FictionBook

class BookDetailsActivity : BaseActivity(R.layout.activity_book_detail) {

    companion object {

        private const val BOOK_PATH_EXTRA = "book_path_extra"

        fun start(context: Context, bookPath: String) {
            context.startActivity<BookDetailsActivity>(BOOK_PATH_EXTRA to bookPath)
        }
    }

    private val viewModel: BookDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.about_book), true)
        fabRead.setOnClickListener {
            viewModel.onEvent(BookDetailsViewModel.Event.OnReadBookClicked)
        }
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.router = null
    }

    private fun initViewModel() {
        viewModel.loadData(intent.getStringExtra(BOOK_PATH_EXTRA)!!)
        viewModel.router = BookDetailsRouter(this)
        viewModel.stateLiveData.observe(this, Observer { state ->
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