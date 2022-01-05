package ru.mamykin.foboreader.settings.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.databinding.DialogColorPickerBinding
import ru.mamykin.foboreader.settings.di.DaggerSettingsComponent
import ru.mamykin.foboreader.settings.domain.usecase.GetTranslationColors
import ru.mamykin.foboreader.settings.domain.usecase.SetTranslationColor
import ru.mamykin.foboreader.settings.presentation.list.ColorListAdapter
import javax.inject.Inject

internal class SelectTranslationColorDialogFragment : DialogFragment() {

    companion object {

        const val TAG = "ColorPickerDialogFragment"

        fun newInstance(): DialogFragment = SelectTranslationColorDialogFragment()
    }

    private val adapter by lazy {
        ColorListAdapter {
            setTranslationColor.execute(it)
            dismiss()
        }
    }

    @Inject
    internal lateinit var getTranslationColors: GetTranslationColors

    @Inject
    internal lateinit var setTranslationColor: SetTranslationColor

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        DaggerSettingsComponent.factory().create(
            apiHolder().commonApi(),
            apiHolder().settingsApi(),
            apiHolder().navigationApi(),
        ).inject(this)

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_color_picker, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.select_color)
            .setView(view)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()

        initColorsList(DialogColorPickerBinding.bind(view))

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (parentFragment is DialogDismissedListener) {
            (parentFragment as DialogDismissedListener).onDismiss()
        }
    }

    private fun initColorsList(binding: DialogColorPickerBinding) {
        binding.rvColors.adapter = adapter
        adapter.submitList(getTranslationColors.execute())
    }
}