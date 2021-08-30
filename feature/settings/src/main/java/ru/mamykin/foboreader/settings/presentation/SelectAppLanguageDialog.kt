package ru.mamykin.foboreader.settings.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.di.SettingsComponentHolder
import ru.mamykin.foboreader.settings.domain.usecase.GetAppLanguages
import ru.mamykin.foboreader.settings.domain.usecase.SetAppLanguage
import javax.inject.Inject

class SelectAppLanguageDialog : DialogFragment() {

    @Inject
    lateinit var getAppLanguages: GetAppLanguages

    @Inject
    lateinit var setAppLanguage: SetAppLanguage

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (requireActivity().application as SettingsComponentHolder).settingsComponent().inject(this)

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