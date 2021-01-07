package ru.mamykin.foboreader.settings.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import org.koin.android.ext.android.inject
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.domain.usecase.GetAppLanguages
import ru.mamykin.foboreader.settings.domain.usecase.SetAppLanguage

class SelectAppLanguageDialog : DialogFragment() {

    private val getAppLanguages: GetAppLanguages by inject()
    private val setAppLanguage: SetAppLanguage by inject()

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val supportedLanguages = getAppLanguages(Unit)
            .getOrThrow()
        val supportedLanguagesNames = supportedLanguages.map { it.first }.toTypedArray()

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.select_app_language)
            .setItems(supportedLanguagesNames) { _, index ->
                supportedLanguages.getOrNull(index)
                    ?.second
                    ?.let(setAppLanguage::invoke)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
    }
}