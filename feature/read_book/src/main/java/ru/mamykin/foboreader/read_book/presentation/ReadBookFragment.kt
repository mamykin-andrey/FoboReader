package ru.mamykin.foboreader.read_book.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.extension.*
import ru.mamykin.foboreader.core.platform.VibratorHelper
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.databinding.FragmentReadBookBinding
import ru.mamykin.foboreader.read_book.databinding.LayoutWordPopupBinding
import ru.mamykin.foboreader.read_book.di.DaggerReadBookComponent
import ru.mamykin.foboreader.read_book.domain.entity.TranslationEntity
import ru.mamykin.foboreader.read_book.presentation.view.ClickableTextView
import javax.inject.Inject

class ReadBookFragment : Fragment(R.layout.fragment_read_book) {

    companion object {

        private const val EXTRA_BOOK_ID = "extra_book_id"

        fun newInstance(bookId: Long) = ReadBookFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_BOOK_ID, bookId)
            }
        }
    }

    @Inject
    internal lateinit var viewModel: ReadBookFeature

    @Inject
    internal lateinit var vibratorHelper: VibratorHelper

    @Inject
    internal lateinit var appSettingsStorage: AppSettingsStorage

    private val binding by autoCleanedValue { FragmentReadBookBinding.bind(requireView()) }
    private var lastTextHashCode: Int = 0
    private var popupWindow: PopupWindow? = null
    private val bookId: Long by lazy { requireArguments().getLong(EXTRA_BOOK_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerReadBookComponent.factory().create(
            bookId,
            apiHolder().networkApi(),
            apiHolder().navigationApi(),
            apiHolder().commonApi(),
            apiHolder().settingsApi()
        ).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvText.textSize = appSettingsStorage.readTextSize.toFloat()
        binding.tvText.setOnActionListener(object : ClickableTextView.OnActionListener {
            override fun onClick(paragraph: String) {
                vibratorHelper.clickVibrate()
                viewModel.sendEvent(ReadBookFeature.Event.TranslateParagraphClicked(paragraph.trim()))
            }

            override fun onLongClick(word: String) {
                vibratorHelper.clickVibrate()
                viewModel.sendEvent(ReadBookFeature.Event.TranslateWordClicked(word.trimSpecialCharacters()))
            }
        })
        viewModel.stateData.observe(viewLifecycleOwner, ::showState)
        viewModel.effectData.observe(viewLifecycleOwner, ::takeEffect)
    }

    private fun showState(state: ReadBookFeature.State) = with(binding) {
        pbLoadingBook.isVisible = state.isTranslationLoading
        showBookText(state.text)
        state.wordTranslation?.let(::showWordTranslation) ?: popupWindow?.dismiss()
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
        val hashCode = text.hashCode()
        text.takeIf { hashCode != lastTextHashCode }
            ?.toHtml()
            ?.let { binding.tvText.setup(it) }
            ?.also { lastTextHashCode = hashCode }
        // binding.tvText.setOnClickListener(null)
    }

    private fun showParagraphTranslation(info: Pair<String, String>) {
        val (paragraph, translation) = info
        binding.tvText.text = SpannableString(paragraph + "\n\n" + translation).apply {
            setColor(
                Color.parseColor(appSettingsStorage.translationColor),
                paragraph.length,
                length - 1
            )
        }
        binding.tvText.setOnClickListener { viewModel.sendEvent(ReadBookFeature.Event.HideParagraphTranslationClicked) }
        lastTextHashCode = 0
    }

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    private fun showWordTranslation(translation: TranslationEntity) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.layout_word_popup, null)
        val popupBinding = LayoutWordPopupBinding.bind(popupView)

        popupBinding.tvSource.text = translation.source
        popupBinding.tvTranslated.text = translation.getMostLikelyTranslation()

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT

        popupWindow = PopupWindow(popupView, width, height, true)
        popupWindow?.showAtLocation(view, Gravity.CENTER, 0, 0)

        popupView.setOnTouchListener { v, _ ->
            viewModel.sendEvent(ReadBookFeature.Event.HideWordTranslationClicked)
            true
        }
    }

    private fun takeEffect(effect: ReadBookFeature.Effect) {
        when (effect) {
            is ReadBookFeature.Effect.ShowSnackbar -> showSnackbar(effect.messageId)
        }
    }
}