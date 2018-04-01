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
                        negativeClickFunc: (() -> Unit)?): YesNoDialogFragment {

            val dialogFragment = YesNoDialogFragment()

            val args = Bundle()
            args.putString(TITLE_EXTRA, title)
            args.putString(MESSAGE_EXTRA, message)
            dialogFragment.arguments = args

            dialogFragment.positiveClickFunc = positiveClickFunc
            dialogFragment.negativeClickFunc = negativeClickFunc

            return dialogFragment
        }
    }

    private lateinit var positiveClickFunc: () -> Unit
    private var negativeClickFunc: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = savedInstanceState ?: arguments
        val title = args!!.getString(TITLE_EXTRA)
        val message = args.getString(MESSAGE_EXTRA)

        val builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(android.R.string.yes, this)
                .setNegativeButton(android.R.string.no, this)
                .setMessage(message)
        return builder.create()
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