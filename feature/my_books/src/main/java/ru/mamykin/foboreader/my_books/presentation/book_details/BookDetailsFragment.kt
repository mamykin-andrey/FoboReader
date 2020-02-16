package ru.mamykin.foboreader.my_books.presentation.book_details

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_book_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.domain.model.BookInfo

class BookDetailsFragment : BaseFragment(R.layout.activity_book_detail) {

    private val viewModel: BookDetailsViewModel by viewModel()
    private val bookId: Long by lazy {
        BookDetailsFragmentArgs.fromBundle(arguments!!).bookId
    }

    override fun loadData() {
        viewModel.loadBookInfo(bookId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initViews() {
        fabRead.setOnClickListener {
            viewModel.onEvent(BookDetailsViewModel.Event.OnReadBookClicked)
        }
    }

    private fun initToolbar() {
        toolbar!!.title = getString(R.string.book_info_title)
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(this, Observer { state ->
            if (state.error) showSnackbar(R.string.book_details_load_info_error)
            state.bookInfo?.let(::showBookInfo)
        })
    }

    private fun showBookInfo(book: BookInfo) {
        tvBookName.text = book.title
        tvBookAuthor.text = book.author
        tvBookPath.text = book.filePath
        tvCurrentPage.text = book.currentPage.toString()
        tvBookGenre.text = book.genre
    }
}