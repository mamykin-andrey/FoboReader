package ru.mamykin.foboreader.read_book.presentation

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mamykin.foboreader.core.extension.setColor
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.extension.toHtml
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.databinding.FragmentReadBookBinding
import ru.mamykin.widget.paginatedtextview.pagination.ReadState
import ru.mamykin.widget.paginatedtextview.view.OnActionListener

class ReadBookFragment : BaseFragment<ReadBookViewModel, ViewState, Effect>(R.layout.fragment_read_book) {

    override val viewModel: ReadBookViewModel by viewModel {
        parametersOf(ReadBookFragmentArgs.fromBundle(requireArguments()).bookId)
    }

    private val binding by viewBinding { FragmentReadBookBinding.bind(requireView()) }
    private var lastTextHashCode: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvText.setOnActionListener(object : OnActionListener {
            override fun onClick(paragraph: String) {
                viewModel.sendEvent(Event.TranslateParagraph(paragraph.trim()))
            }

            override fun onLongClick(word: String) {
                viewModel.sendEvent(Event.TranslateWord(word.trim()))
            }

            override fun onPageLoaded(state: ReadState) = with(state) {
                viewModel.sendEvent(Event.PageLoaded(state.currentIndex, state.pagesCount))
                binding.tvReadPercent.text = getString(
                    R.string.read_book_user_read_percent,
                    readPercent
                )
                binding.tvRead.text = getString(
                    R.string.read_book_user_read_pages,
                    currentIndex,
                    pagesCount
                )
            }
        })
    }

    override fun showState(state: ViewState) {
        progressView.isVisible = state.isTranslationLoading
        showBookText(state.text, state.currentPage)
        state.wordTranslation?.let(::showWordTranslation)
        state.paragraphTranslation?.let(::showParagraphTranslation)
        binding.tvName.text = state.title
        binding.tvRead.text = getString(
            R.string.read_book_user_read_pages,
            state.currentPage,
            state.totalPages
        )
        binding.tvReadPercent.text = state.readPercent.toString()
    }

    private fun showBookText(text: String, page: Int) {
        text.hashCode()
            .takeIf { it != lastTextHashCode }
            ?.let {
//                binding.tvText.setOnClickListener(null)
                binding.tvText.setup(text.toHtml(), page)
                lastTextHashCode = it
            }
    }

    private fun showParagraphTranslation(info: Pair<String, String>) {
        val (paragraph, translation) = info
        binding.tvText.text = SpannableString(paragraph + "\n\n" + translation).apply {
            setColor(Color.RED, paragraph.length, length - 1)
        }
        binding.tvText.setOnClickListener { viewModel.sendEvent(Event.HideParagraphTranslation) }
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