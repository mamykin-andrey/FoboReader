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
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.extension.setColor
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.extension.toHtml
import ru.mamykin.foboreader.core.extension.trimSpecialCharacters
import ru.mamykin.foboreader.core.platform.VibratorHelper
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.viewBinding
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.databinding.FragmentReadBookBinding
import ru.mamykin.foboreader.read_book.databinding.LayoutWordPopupBinding
import ru.mamykin.foboreader.read_book.domain.entity.TranslationEntity
import ru.mamykin.foboreader.read_book.presentation.view.ClickableTextView

class ReadBookFragment : BaseFragment<ReadBookViewModel, ViewState, Effect>(R.layout.fragment_read_book) {

    override val viewModel: ReadBookViewModel by viewModel()
//        parametersOf(ReadBookFragmentArgs.fromBundle(requireArguments()).bookId)

    private val vibratorHelper: VibratorHelper by inject()
    private val appSettingsStorage: AppSettingsStorage by inject()
    private val binding by viewBinding { FragmentReadBookBinding.bind(requireView()) }
    private var lastTextHashCode: Int = 0
    private var popupWindow: PopupWindow? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvText.textSize = appSettingsStorage.readTextSizeField.get().toFloat()
        binding.tvText.setOnActionListener(object : ClickableTextView.OnActionListener {
            override fun onClick(paragraph: String) {
                vibratorHelper.clickVibrate()
                viewModel.sendEvent(Event.TranslateParagraph(paragraph.trim()))
            }

            override fun onLongClick(word: String) {
                vibratorHelper.clickVibrate()
                viewModel.sendEvent(Event.TranslateWord(word.trimSpecialCharacters()))
            }
        })
    }

    override fun showState(state: ViewState) {
        progressView.isVisible = state.isTranslationLoading
        showBookText(state.text)
        state.wordTranslation?.let(::showWordTranslation) ?: popupWindow?.dismiss()
        state.paragraphTranslation?.let(::showParagraphTranslation)
        binding.tvName.text = state.title
        binding.tvRead.text = getString(
            R.string.read_book_user_read_pages,
            state.currentPage,
            state.totalPages
        )
        binding.tvReadPercent.text = state.readPercent.toString()
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
                appSettingsStorage.translationColorCodeField.get().let { Color.parseColor(it) },
                paragraph.length,
                length - 1
            )
        }
        binding.tvText.setOnClickListener { viewModel.sendEvent(Event.HideParagraphTranslation) }
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
            viewModel.sendEvent(Event.HideWordTranslation)
            true
        }
    }

    override fun takeEffect(effect: Effect) {
        when (effect) {
            is Effect.ShowSnackbar -> showSnackbar(effect.messageId)
        }
    }
}