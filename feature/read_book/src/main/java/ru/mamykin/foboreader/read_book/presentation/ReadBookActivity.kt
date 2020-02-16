package ru.mamykin.foboreader.read_book.presentation

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_read_book.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.extension.startActivity
import ru.mamykin.foboreader.core.ui.BaseActivity
import ru.mamykin.paginatedtextview.pagination.ReadState
import ru.mamykin.paginatedtextview.view.OnActionListener
import ru.mamykin.foboreader.read_book.R

class ReadBookActivity : BaseActivity(R.layout.activity_read_book) {

    companion object {
        private const val BOOK_PATH_EXTRA = "book_path_extra"

        fun start(context: Context, bookPath: String) {
            context.startActivity<ReadBookActivity>(BOOK_PATH_EXTRA to bookPath)
        }
    }

    private val readBookViewModel: ReadBookViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvText.setOnActionListener(object : OnActionListener {
            override fun onClick(paragraph: String) {
                readBookViewModel.onParagraphClicked(paragraph)
            }

            override fun onLongClick(word: String) {
                readBookViewModel.onWordClicked(word)
            }

            override fun onPageLoaded(state: ReadState) = with(state) {
                tvReadPercent.text = getString(R.string.read_percent_string, readPercent)
                tvRead.text = getString(R.string.read_pages_format, currentIndex, pagesCount)
            }
        })
        initViewModel()
    }

    private fun initViewModel() {
        readBookViewModel.stateLiveData.observe(this, Observer { state ->
            pbLoading.isVisible = state.isLoading
            pbLoading.isVisible = state.isTranslationLoading
            state.wordTranslation?.let { }
            state.paragraphTranslation?.let { }
            state.bookInfo?.let { tvName.text = it.bookTitle }
            if (state.isTranslationError) showSnackbar(R.string.error_translation)
            if (state.isBookLoadingError) showSnackbar(R.string.error_book_loading)
        })
    }
}