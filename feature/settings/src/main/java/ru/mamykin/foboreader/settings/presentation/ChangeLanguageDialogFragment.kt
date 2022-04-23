package ru.mamykin.foboreader.settings.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.collectWithRepeatOnStarted
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.presentation.BaseDialogFragment
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.databinding.DialogChangeLanuageBinding
import ru.mamykin.foboreader.settings.di.DaggerSettingsComponent
import ru.mamykin.foboreader.settings.presentation.list.LanguageListAdapter
import javax.inject.Inject

internal class ChangeLanguageDialogFragment : BaseDialogFragment() {

    companion object {

        const val TAG = "ChangeLanguageDialogFragment"

        fun newInstance() = ChangeLanguageDialogFragment()
    }

    override val featureName: String = "change_app_language"

    @Inject
    internal lateinit var feature: ChangeLanguageFeature

    private val adapter by lazy {
        LanguageListAdapter {
            feature.sendIntent(ChangeLanguageFeature.Intent.SelectLanguage(it))
        }
    }

    private var dialogView: View? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initDi()

        val inflater = requireActivity().layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_change_lanuage, null)

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.select_app_language)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return requireNotNull(dialogView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(requireNotNull(dialogView))
        initFeature()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerSettingsComponent.factory().create(
                commonApi(),
                apiHolder().settingsApi(),
                apiHolder().navigationApi(),
            )
        }.inject(this)
    }

    private fun initView(view: View) {
        val binding = DialogChangeLanuageBinding.bind(view)
        binding.rvLanguages.adapter = adapter
    }

    private fun initFeature() {
        feature.stateFlow.collectWithRepeatOnStarted(viewLifecycleOwner, ::showState)
        feature.effectFlow.collectWithRepeatOnStarted(viewLifecycleOwner, ::takeEffect)
    }

    private fun showState(state: ChangeLanguageFeature.State) {
        state.languages?.let(adapter::submitList)
    }

    private fun takeEffect(effect: ChangeLanguageFeature.Effect) = when (effect) {
        ChangeLanguageFeature.Effect.Dismiss -> dismiss()
    }

    override fun onDestroyView() {
        dialogView = null
        super.onDestroyView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (parentFragment is DialogDismissedListener) {
            (parentFragment as DialogDismissedListener).onDismiss()
        }
    }

    override fun onCleared() {
        feature.onCleared()
    }
}