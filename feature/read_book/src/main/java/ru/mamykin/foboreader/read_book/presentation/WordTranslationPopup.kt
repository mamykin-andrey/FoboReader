package ru.mamykin.foboreader.read_book.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import ru.mamykin.foboreader.read_book.databinding.LayoutWordPopupBinding
import ru.mamykin.foboreader.read_book.domain.model.TextTranslation

internal class WordTranslationPopup(
    private val context: Context,
) {
    private var popupWindow: PopupWindow? = null

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    fun show(
        view: View,
        translation: TextTranslation,
        onDismiss: () -> Unit,
    ) {
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
            onDismiss()
            true
        }
    }

    fun dismiss() {
        popupWindow?.dismiss()
        popupWindow = null
    }
}