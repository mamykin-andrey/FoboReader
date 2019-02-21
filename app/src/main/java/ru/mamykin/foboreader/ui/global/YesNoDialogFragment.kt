package ru.mamykin.foboreader.ui.global

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment

/**
 * Диалог с текстом и заголовком, в каждом диалоге отображаются поля "Ок" и "Отмена"
 */
class YesNoDialogFragment : AppCompatDialogFragment(), DialogInterface.OnClickListener {

    companion object {

        private const val TITLE_EXTRA = "title_extra"
        private const val MESSAGE_EXTRA = "message_extra"

        fun newInstance(title: String,
                        message: String,
                        positiveClickFunc: () -> Unit,
                        negativeClickFunc: (() -> Unit)?): YesNoDialogFragment = YesNoDialogFragment().apply {
            this.arguments = Bundle().apply {
                putString(TITLE_EXTRA, title)
                putString(MESSAGE_EXTRA, message)
            }
            this.positiveClickFunc = positiveClickFunc
            this.negativeClickFunc = negativeClickFunc
        }
    }

    private lateinit var positiveClickFunc: () -> Unit
    private var negativeClickFunc: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = savedInstanceState ?: arguments
        return AlertDialog.Builder(context)
                .setTitle(args!!.getString(TITLE_EXTRA))
                .setPositiveButton(android.R.string.yes, this)
                .setNegativeButton(android.R.string.no, this)
                .setMessage(args.getString(MESSAGE_EXTRA))
                .create()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == Dialog.BUTTON_POSITIVE) {
            positiveClickFunc.invoke()
        } else if (which == Dialog.BUTTON_NEGATIVE) {
            negativeClickFunc?.invoke()
        }
        this.dismiss()
    }
}