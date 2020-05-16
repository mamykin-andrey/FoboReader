package ru.mamykin.foboreader.read_book.presentation

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.view.View
import kotlinx.android.synthetic.main.fragment_read_book.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.extension.setColor
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.widget.paginatedtextview.pagination.ReadState
import ru.mamykin.widget.paginatedtextview.view.OnActionListener

class ReadBookFragment : BaseFragment(R.layout.fragment_read_book) {

    private val viewModel: ReadBookViewModel by viewModel()
    private val safeArguments by lazy { ReadBookFragmentArgs.fromBundle(arguments!!) }
    private var lastTextHashCode: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvText.setOnActionListener(object : OnActionListener {
            override fun onClick(paragraph: String) {
                viewModel.onParagraphClicked(paragraph.trim())
            }

            override fun onLongClick(word: String) {
                viewModel.onWordClicked(word.trim())
            }

            override fun onPageLoaded(state: ReadState) = with(state) {
                tvReadPercent.text = getString(R.string.read_percent_string, readPercent)
                tvRead.text = getString(R.string.read_pages_format, currentIndex, pagesCount)
            }
        })
        initViewModel()
    }

    override fun loadData() {
        viewModel.loadBookInfo(safeArguments.bookId)
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe { showState(it) }
    }

    private fun showState(state: ReadBookViewModel.ViewState) = with(state) {
        pbLoading.isVisible = isTranslationLoading
        showBookText(text)
        wordTranslation?.let(::showWordTranslation)
        paragraphTranslation?.let(::showParagraphTranslation)
        tvName.text = title
        tvRead.text = getString(R.string.format_book_read_amount, currentPage, totalPages)
        tvReadPercent.text = readPercent.toString()
        error?.let { showSnackbar(it) }
    }

    private fun showBookText(text: String) {
        text.hashCode()
                .takeIf { it != lastTextHashCode }
                ?.also { tvText.setup(Html.fromHtml(text)) }
                ?.also { lastTextHashCode = it }
    }

    private fun showParagraphTranslation(info: Pair<String, String>) {
        val (paragraph, translation) = info
        tvText.text = SpannableString(paragraph + "\n\n" + translation).apply {
            setColor(Color.RED, paragraph.length, length - 1)
        }
        lastTextHashCode = 0
    }

    private fun showWordTranslation(info: Pair<String, String>) {
        val (word, translation) = info
    }
}