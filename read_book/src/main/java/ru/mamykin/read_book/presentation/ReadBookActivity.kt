package ru.mamykin.read_book.presentation

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_read_book.*
import ru.mamykin.core.extension.isVisible
import ru.mamykin.core.extension.showSnackbar
import ru.mamykin.core.extension.startActivity
import ru.mamykin.core.ui.BaseActivity
import ru.mamykin.paginatedtextview.pagination.ReadState
import ru.mamykin.paginatedtextview.view.OnActionListener
import ru.mamykin.read_book.R
import ru.mamykin.read_book.di.DaggerReadBookComponent

class ReadBookActivity : BaseActivity(R.layout.activity_read_book) {

    companion object {
        private const val BOOK_PATH_EXTRA = "book_path_extra"

        fun start(context: Context, bookPath: String) {
            context.startActivity<ReadBookActivity>(BOOK_PATH_EXTRA to bookPath)
        }
    }

    private val viewModel: ReadBookViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
        tvText.setOnActionListener(object : OnActionListener {
            override fun onClick(paragraph: String) {
                viewModel.onParagraphClicked(paragraph)
            }

            override fun onLongClick(word: String) {
                viewModel.onWordClicked(word)
            }

            override fun onPageLoaded(state: ReadState) = with(state) {
                tvReadPercent.text = getString(R.string.read_percent_string, readPercent)
                tvRead.text = getString(R.string.read_pages_format, currentIndex, pagesCount)
            }
        })
        initViewModel()
    }

    private fun initDi() {
        DaggerReadBookComponent.builder()
                .appComponent(getAppComponent())
                .build()
                .inject(this)
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(this, Observer { state ->
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