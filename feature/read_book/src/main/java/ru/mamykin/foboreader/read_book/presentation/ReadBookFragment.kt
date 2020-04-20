package ru.mamykin.foboreader.read_book.presentation

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.view.View
import androidx.lifecycle.Observer
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
    private lateinit var args: ReadBookFragmentArgs
    private var lastTextHashCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = ReadBookFragmentArgs.fromBundle(arguments!!)
    }

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
        viewModel.loadBookInfo(args.bookId)
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
            pbLoading.isVisible = state.isTranslationLoading
            showBookText(state.text)
            state.wordTranslation?.let { /* TODO */ }
            state.paragraphTranslation?.let { (p, t) -> showParagraphTranslation(p, t) }
            tvName.text = state.title
            tvRead.text = "${state.currentPage}/${state.totalPages}"
            tvReadPercent.text = "${state.readPercent}"
            state.error?.let { showSnackbar(it) }
        })
    }

    private fun showBookText(text: String) {
        text.hashCode()
                .takeIf { it != lastTextHashCode }
                ?.also { tvText.setup(Html.fromHtml(text)) }
                ?.also { lastTextHashCode = it }
    }

    private fun showParagraphTranslation(paragraph: String, translation: String) {
        tvText.text = SpannableString(paragraph + "\n\n" + translation)
                .apply { setColor(Color.RED, paragraph.length, length - 1) }
        lastTextHashCode = 0
    }
}