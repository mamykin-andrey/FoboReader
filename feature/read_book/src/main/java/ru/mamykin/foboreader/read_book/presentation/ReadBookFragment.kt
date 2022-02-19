package ru.mamykin.foboreader.read_book.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.view.isVisible
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.extension.toHtml
import ru.mamykin.foboreader.read_book.platform.VibratorHelper
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.databinding.FragmentReadBookBinding
import ru.mamykin.foboreader.read_book.databinding.LayoutWordPopupBinding
import ru.mamykin.foboreader.read_book.di.DaggerReadBookComponent
import ru.mamykin.foboreader.read_book.domain.model.Translation
import ru.mamykin.foboreader.read_book.presentation.view.ClickableTextView
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
    internal lateinit var vibratorHelper: VibratorHelper

    private val binding by autoCleanedValue { FragmentReadBookBinding.bind(requireView()) }
    private var lastTextHashCode: Int = 0
    private var popupWindow: PopupWindow? = null
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
        binding.tvText.setOnActionListener(object : ClickableTextView.OnActionListener {
            override fun onClick(paragraph: String) {
                feature.sendIntent(ReadBookFeature.Intent.TranslateParagraph(paragraph))
            }

            override fun onLongClick(word: String) {
                feature.sendIntent(ReadBookFeature.Intent.TranslateWord(word))
            }
        })
        feature.stateData.observe(viewLifecycleOwner, ::showState)
        feature.effectData.observe(viewLifecycleOwner, ::takeEffect)
    }

    private fun showState(state: ReadBookFeature.State) = with(binding) {
        pbLoadingBook.isVisible = state.isTranslationLoading
        updateBookTextSize(state.textSize)
        showBookText(state.text)
        updateWordTranslation(state.wordTranslation)
        state.paragraphTranslation?.let(::showParagraphTranslation)
        tvName.text = state.title
        tvRead.text = getString(
            R.string.read_book_user_read_pages,
            state.currentPage,
            state.totalPages
        )
        tvReadPercent.text = state.readPercent.toString()
    }

    private fun updateBookTextSize(textSize: Float?) = with(binding) {
        textSize?.let {
            tvText.textSize = it
        }
    }

    private fun showBookText(text: String) {
        val hashCode = text.hashCode()
        text.takeIf { hashCode != lastTextHashCode }
            ?.toHtml()
            ?.let { binding.tvText.setup(it) }
            ?.also { lastTextHashCode = hashCode }
        binding.tvText.setOnClickListener(null)
    }

    private fun showParagraphTranslation(translation: CharSequence) {
        binding.tvText.text = translation
        binding.tvText.setOnClickListener {
            feature.sendIntent(ReadBookFeature.Intent.HideParagraphTranslation)
        }
        lastTextHashCode = 0
    }

    private fun updateWordTranslation(translation: Translation?) {
        if (translation != null) {
            showWordTranslation(translation)
        } else {
            popupWindow?.dismiss()
        }
    }

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    private fun showWordTranslation(translation: Translation) {
        val inflater = LayoutInflater.from(context)
        val popupBinding = LayoutWordPopupBinding.inflate(inflater, null, false)

        popupBinding.tvSource.text = translation.sourceText
        popupBinding.tvTranslated.text = translation.getMostPreciseTranslation()

        popupWindow = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow?.showAtLocation(view, Gravity.CENTER, 0, 0)

        popupBinding.root.setOnTouchListener { _, _ ->
            feature.sendIntent(ReadBookFeature.Intent.HideWordTranslation)
            true
        }
    }

    private fun takeEffect(effect: ReadBookFeature.Effect) {
        when (effect) {
            is ReadBookFeature.Effect.ShowSnackbar -> showSnackbar(effect.messageId)
            is ReadBookFeature.Effect.Vibrate -> vibratorHelper.vibrate(requireView())
        }
    }
}