package ru.mamykin.foboreader.settings.presentation

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
import ru.mamykin.foboreader.core.extension.addGlobalLayoutListener
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.dpToPx
import ru.mamykin.foboreader.core.presentation.BaseDialogFragment
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.databinding.DialogColorPickerBinding
import ru.mamykin.foboreader.settings.di.DaggerSettingsComponent
import ru.mamykin.foboreader.settings.presentation.list.ColorListAdapter
import javax.inject.Inject

internal class SelectTranslationColorDialogFragment : BaseDialogFragment() {

    companion object {

        const val TAG = "ColorPickerDialogFragment"

        fun newInstance(): DialogFragment = SelectTranslationColorDialogFragment()
    }

    override val featureName: String = "select_translation_color"

    private val adapter by lazy {
        ColorListAdapter {
            feature.sendIntent(SelectTranslationColorFeature.Intent.SelectColor(it))
        }
    }

    @Inject
    internal lateinit var feature: SelectTranslationColorFeature

    private var dialogView: View? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initDi()

        val inflater = requireActivity().layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_color_picker, null)

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
        val binding = DialogColorPickerBinding.bind(view)
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
        feature.stateData.observe(viewLifecycleOwner, ::showState)
        feature.effectData.observe(viewLifecycleOwner, ::takeEffect)
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerSettingsComponent.factory().create(
                apiHolder().commonApi(),
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

    private fun showState(state: SelectTranslationColorFeature.State) {
        state.colors?.let(adapter::submitList)
    }

    private fun takeEffect(effect: SelectTranslationColorFeature.Effect) = when (effect) {
        is SelectTranslationColorFeature.Effect.Dismiss -> dismiss()
    }
}