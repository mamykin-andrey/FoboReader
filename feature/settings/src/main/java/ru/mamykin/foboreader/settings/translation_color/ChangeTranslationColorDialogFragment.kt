package ru.mamykin.foboreader.settings.translation_color

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.*
import ru.mamykin.foboreader.core.presentation.BaseDialogFragment
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.databinding.DialogChangeTranslationColorBinding
import ru.mamykin.foboreader.settings.di.DaggerSettingsComponent
import ru.mamykin.foboreader.settings.DialogDismissedListener
import javax.inject.Inject

internal class ChangeTranslationColorDialogFragment : BaseDialogFragment() {

    companion object {

        const val TAG = "ColorPickerDialogFragment"

        fun newInstance(): DialogFragment = ChangeTranslationColorDialogFragment()
    }

    override val featureName: String = "select_translation_color"

    private val adapter by lazy {
        ColorListAdapter {
            feature.sendIntent(ChangeTranslationColorFeature.Intent.SelectColor(it))
        }
    }

    @Inject
    internal lateinit var feature: ChangeTranslationColorFeature

    private var dialogView: View? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initDi()

        val inflater = requireActivity().layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_change_translation_color, null)

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.select_color)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return requireNotNull(dialogView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initFeature()
    }

    override fun onDestroyView() {
        dialogView = null
        super.onDestroyView()
    }

    private fun initView(view: View) {
        val binding = DialogChangeTranslationColorBinding.bind(view)
        binding.rvColors.adapter = adapter
        binding.rvColors.addGlobalLayoutListener {
            val recyclerViewWidth = binding.rvColors.width - requireContext().dpToPx(16 * 2).toInt()
            val itemWidth = requireContext().dpToPx(40 + (8 * 2)).toInt()
            val itemsCount = recyclerViewWidth / itemWidth - 1
            val layoutManager = GridLayoutManager(requireContext(), itemsCount)
            binding.rvColors.layoutManager = layoutManager
        }
    }

    private fun initFeature() {
        feature.stateFlow.collectWithRepeatOnStarted(::showState)
        feature.effectFlow.collectWithRepeatOnStarted(::takeEffect)
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (parentFragment is DialogDismissedListener) {
            (parentFragment as DialogDismissedListener).onDismiss()
        }
    }

    override fun onCleared() {
        feature.onCleared()
    }

    private fun showState(state: ChangeTranslationColorFeature.State) {
        state.colors?.let(adapter::submitList)
    }

    private fun takeEffect(effect: ChangeTranslationColorFeature.Effect) = when (effect) {
        is ChangeTranslationColorFeature.Effect.Dismiss -> dismiss()
    }
}