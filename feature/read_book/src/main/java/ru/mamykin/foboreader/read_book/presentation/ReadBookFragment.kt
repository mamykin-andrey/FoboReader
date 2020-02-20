package ru.mamykin.foboreader.read_book.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_read_book.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.paginatedtextview.pagination.ReadState
import ru.mamykin.paginatedtextview.view.OnActionListener

class ReadBookFragment : BaseFragment(R.layout.fragment_read_book) {

    private val viewModel: ReadBookViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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