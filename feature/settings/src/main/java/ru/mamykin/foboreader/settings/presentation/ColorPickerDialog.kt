package ru.mamykin.foboreader.settings.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import org.koin.android.ext.android.inject
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.databinding.DialogColorPickerBinding
import ru.mamykin.foboreader.settings.databinding.ItemColorBinding
import ru.mamykin.foboreader.settings.domain.model.ColorItem
import ru.mamykin.foboreader.settings.presentation.list.ColorItemViewHolder

class ColorPickerDialog : DialogFragment() {

    private val colorsDataSource = dataSourceTypedOf<ColorItem>()
    private val appSettingsStorage: AppSettingsStorage by inject()

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_color_picker, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.select_color)
            .setView(view)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()

        initColors(DialogColorPickerBinding.bind(view))

        return dialog
    }

    private fun initColors(binding: DialogColorPickerBinding) = binding.apply {
        rvColors.setup {
            withDataSource(colorsDataSource)
            withItem<ColorItem, ColorItemViewHolder>(R.layout.item_color) {
                onBind({ ColorItemViewHolder(ItemColorBinding.bind(it)) }) { _, item -> bind(item) }
                onClick {
                    // TODO: replace with UseCase
                    appSettingsStorage.translationColorCodeField.set(item.colorCode)
                    dismiss()
                }
            }
        }
        loadData()
    }

    private fun loadData() {
        // TODO: replace with UseCase
        colorsDataSource.set(createColors(appSettingsStorage.translationColorCodeField.get() ?: "#000000"))
    }

    private fun createColors(selectedColorCode: String): List<ColorItem> {
        return listOf(
            Pair("#ff0000", "#ffffff"),
            Pair("#0099ff", "#ffffff"),
            Pair("#0066ff", "#ffffff"),
            Pair("#ff5050", "#ffffff"),
            Pair("#ffff00", "#000000"),
            Pair("#009900", "#ffffff"),
            Pair("#336600", "#ffffff"),
            Pair("#00cc66", "#ffffff")
        ).map { (code, checkCode) -> ColorItem(code, checkCode, code == selectedColorCode) }
    }
}