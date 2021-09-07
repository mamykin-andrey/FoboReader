package ru.mamykin.foboreader.settings.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.databinding.DialogColorPickerBinding
import ru.mamykin.foboreader.settings.databinding.ItemColorBinding
import ru.mamykin.foboreader.settings.di.SettingsComponentHolder
import ru.mamykin.foboreader.settings.domain.model.ColorItem
import ru.mamykin.foboreader.settings.domain.usecase.GetTranslationColors
import ru.mamykin.foboreader.settings.domain.usecase.SetTranslationColor
import ru.mamykin.foboreader.settings.presentation.list.ColorItemViewHolder
import javax.inject.Inject

class ColorPickerDialogFragment : DialogFragment() {

    private val colorsDataSource = dataSourceTypedOf<ColorItem>()

    @Inject
    lateinit var getTranslationColors: GetTranslationColors

    @Inject
    lateinit var setTranslationColor: SetTranslationColor

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (requireActivity().application as SettingsComponentHolder).settingsComponent().inject(this)

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

    private fun initColorsList(binding: DialogColorPickerBinding) = binding.apply {
        rvColors.setup {
            withDataSource(colorsDataSource)
            withItem<ColorItem, ColorItemViewHolder>(R.layout.item_color) {
                onBind({ ColorItemViewHolder(ItemColorBinding.bind(it)) }) { _, item -> bind(item) }
                onClick {
                    setTranslationColor(item.colorCode)
                    dismiss()
                }
            }
        }
        colorsDataSource.set(getTranslationColors(Unit).getOrThrow())
    }
}