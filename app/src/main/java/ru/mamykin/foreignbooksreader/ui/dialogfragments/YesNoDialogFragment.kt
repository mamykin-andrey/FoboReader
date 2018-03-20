package ru.mamykin.foreignbooksreader.ui.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment

import java.lang.ref.WeakReference

/**
 * Диалог с текстом и заголовком, в каждом диалоге отображаются поля "Ок" и "Отмена"
 */
class YesNoDialogFragment : AppCompatDialogFragment(), DialogInterface.OnClickListener {

    private var title: String? = null
    private var message: String? = null
    private var positiveListenerRef: WeakReference<PositiveClickListener>? = null
    private var negativeListenerRef: WeakReference<NegativeClickListener>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = savedInstanceState ?: arguments
        title = args.getString(TITLE_EXTRA)
        message = args.getString(MESSAGE_EXTRA)
        val builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(android.R.string.yes, this)
                .setNegativeButton(android.R.string.no, this)
                .setMessage(message)
        return builder.create()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putString(TITLE_EXTRA, title)
        outState.putString(MESSAGE_EXTRA, message)
    }

    fun setPositiveClickListener(listener: PositiveClickListener): YesNoDialogFragment {
        this.positiveListenerRef = WeakReference(listener)
        return this
    }

    fun setNegativeClickListener(listener: NegativeClickListener): YesNoDialogFragment {
        this.negativeListenerRef = WeakReference(listener)
        return this
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == Dialog.BUTTON_POSITIVE && positiveListenerRef != null && positiveListenerRef!!.get() != null) {
            positiveListenerRef!!.get()!!.onPositiveClick()
        } else if (which == Dialog.BUTTON_NEGATIVE && negativeListenerRef != null && negativeListenerRef!!.get() != null) {
            negativeListenerRef!!.get()!!.onNegativeClick()
        }
        dismiss()
    }

    interface PositiveClickListener {
        fun onPositiveClick()
    }

    interface NegativeClickListener {
        fun onNegativeClick()
    }

    companion object {
        private val TITLE_EXTRA = "title_extra"
        private val MESSAGE_EXTRA = "message_extra"

        fun newInstance(title: String, message: String): YesNoDialogFragment {
            val dialogFragment = YesNoDialogFragment()
            val args = Bundle()
            args.putString(TITLE_EXTRA, title)
            args.putString(MESSAGE_EXTRA, message)
            dialogFragment.arguments = args
            return dialogFragment
        }
    }
}