package ru.mamykin.foboreader.read_book.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.extension.toHtml
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.databinding.FragmentReadBookBinding
import ru.mamykin.foboreader.read_book.di.DaggerReadBookComponent
import ru.mamykin.foboreader.read_book.domain.model.TextTranslation
import ru.mamykin.foboreader.read_book.platform.VibrationManager
import javax.inject.Inject

class ReadBookFragment : BaseFragment(R.layout.fragment_read_book) {

    companion object {

        private const val EXTRA_BOOK_ID = "extra_book_id"

        fun newInstance(bookId: Long) = ReadBookFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_BOOK_ID, bookId)
            }
        }
    }

    override val featureName: String = "read_book"

    @Inject
    internal lateinit var feature: ReadBookFeature

    @Inject
    internal lateinit var vibrationManager: VibrationManager

    private val binding by autoCleanedValue { FragmentReadBookBinding.bind(requireView()) }
    private var lastTextHashCode: Int = 0
    private val wordTranslationPopup by lazy { WordTranslationPopup(requireContext()) }
    private val bookId: Long by lazy { requireArguments().getLong(EXTRA_BOOK_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerReadBookComponent.factory().create(
                bookId,
                apiHolder().networkApi(),
                apiHolder().navigationApi(),
                commonApi(),
                apiHolder().settingsApi()
            )
        }.inject(this)
    }

    override fun onCleared() {
        feature.onCleared()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBookTextViews()
        feature.stateFlow.collectWithRepeatOnStarted(::showState)
        feature.effectFlow.collectWithRepeatOnStarted(::takeEffect)
    }

    private fun initBookTextViews() {
        binding.tvText.setOnParagraphClickListener {
            feature.sendIntent(ReadBookFeature.Intent.TranslateParagraph(it))
        }
        binding.tvText.setOnWordLongClickListener {
            feature.sendIntent(ReadBookFeature.Intent.TranslateWord(it))
        }
    }

    private fun showState(state: ReadBookFeature.State) = with(binding) {
        pbLoadingBook.isVisible = state.isTranslationLoading
        updateBookTextSize(state.textSize)
        showBookText(state.text)
        updateWordTranslation(state.wordTranslation)
        state.paragraphTranslation?.let(::showParagraphTranslation)
        tvName.text = state.title
        updatePagesRead(state.currentPage, state.totalPages)
        tvReadPercent.text = state.readPercent.toString()
    }

    private fun updateBookTextSize(textSize: Float?) = with(binding) {
        textSize?.let {
            tvText.textSize = it
        }
    }

    private fun updatePagesRead(currentPage: Int, totalPages: Int) = with(binding) {
        tvRead.text = getString(
            R.string.read_book_user_read_pages,
            currentPage,
            totalPages
        )
    }

    private fun showBookText(text: String) {
        val textHashCode = text.hashCode()
        if (textHashCode != lastTextHashCode) {
            binding.tvText.setup(text.toHtml())
            lastTextHashCode = textHashCode
            binding.tvText.setOnClickListener(null)
        }
    }

    private fun showParagraphTranslation(translation: CharSequence) {
        binding.tvText.text = translation
        binding.tvText.setOnClickListener {
            feature.sendIntent(ReadBookFeature.Intent.HideParagraphTranslation)
        }
        lastTextHashCode = 0
    }

    private fun updateWordTranslation(translation: TextTranslation?) {
        if (translation != null) {
            wordTranslationPopup.show(requireView(), translation) {
                feature.sendIntent(ReadBookFeature.Intent.HideWordTranslation)
            }
        } else {
            wordTranslationPopup.dismiss()
        }
    }

    private fun takeEffect(effect: ReadBookFeature.Effect) {
        when (effect) {
            is ReadBookFeature.Effect.ShowSnackbar -> showSnackbar(effect.messageId)
            is ReadBookFeature.Effect.Vibrate -> vibrationManager.vibrate(requireView())
        }
    }
}