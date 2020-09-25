package ru.mamykin.foboreader.read_book.presentation

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.view.View
import kotlinx.android.synthetic.main.fragment_read_book.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.extension.setColor
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.widget.paginatedtextview.pagination.ReadState
import ru.mamykin.widget.paginatedtextview.view.OnActionListener

class ReadBookFragment : BaseFragment<ReadBookViewModel, ViewState, Effect>(
    R.layout.fragment_read_book
) {
    companion object {

        private const val EXTRA_BOOK_ID = "BOOK_ID"

        fun bundle(bookId: Long) = Bundle().apply {
            putLong(EXTRA_BOOK_ID, bookId)
        }
    }

    override val viewModel: ReadBookViewModel by viewModel {
        parametersOf(arguments?.getLong(EXTRA_BOOK_ID) ?: throw IllegalStateException("No book ID!"))
    }
    private var lastTextHashCode: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvText.setOnActionListener(object : OnActionListener {
            override fun onClick(paragraph: String) {
                viewModel.sendEvent(Event.TranslateParagraph(paragraph.trim()))
            }

            override fun onLongClick(word: String) {
                viewModel.sendEvent(Event.TranslateWord(word.trim()))
            }

            override fun onPageLoaded(state: ReadState) = with(state) {
                viewModel.sendEvent(Event.PageOpened(state.currentIndex))
                tvReadPercent.text = getString(
                    R.string.read_book_user_read_percent,
                    readPercent
                )
                tvRead.text = getString(
                    R.string.read_book_user_read_pages,
                    currentIndex,
                    pagesCount
                )
            }
        })
    }

    override fun showState(state: ViewState) {
        pbLoading.isVisible = state.isTranslationLoading
        showBookText(state.text)
        state.wordTranslation?.let(::showWordTranslation)
        state.paragraphTranslation?.let(::showParagraphTranslation)
        tvName.text = state.title
        tvRead.text = getString(
            R.string.read_book_user_read_pages,
            state.currentPage,
            state.totalPages
        )
        tvReadPercent.text = state.readPercent.toString()
    }

    private fun showBookText(text: String) {
        text.hashCode()
            .takeIf { it != lastTextHashCode }
            ?.let {
                tvText.setOnClickListener(null)
                tvText.setup(Html.fromHtml(text))
                lastTextHashCode = it
            }
    }

    private fun showParagraphTranslation(info: Pair<String, String>) {
        val (paragraph, translation) = info
        tvText.text = SpannableString(paragraph + "\n\n" + translation).apply {
            setColor(Color.RED, paragraph.length, length - 1)
        }
        tvText.setOnClickListener { viewModel.sendEvent(Event.HideParagraphTranslation) }
        lastTextHashCode = 0
    }

    private fun showWordTranslation(info: Pair<String, String>) {
        // TODO
//        val (word, translation) = info
    }

    override fun takeEffect(effect: Effect) {
        when (effect) {
            is Effect.ShowSnackbar -> showSnackbar(effect.messageId)
        }
    }
}