package ru.mamykin.foboreader.settings.custom_color

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.presentation.BaseDialogFragment
import ru.mamykin.foboreader.settings.DaggerSettingsComponent
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import javax.inject.Inject

internal class ChooseCustomColorDialogFragment : BaseDialogFragment() {

    companion object {

        const val TAG = "ChooseCustomColorDialogFragment"
        const val RETURN_RESULT_COLOR_CODE = "result_color_code"

        private const val COLOR_CIRCLE_SIZE = 45
        private const val COLOR_CIRCLE_PADDING = 4

        fun newInstance(
            requestKey: String,
            currentColorCode: String?,
        ): DialogFragment = ChooseCustomColorDialogFragment().apply {
            arguments = Bundle().apply {
                putString(FragmentParams.REQUEST_KEY, requestKey)
                putString(FragmentParams.CURRENT_COLOR_CODE, currentColorCode)
            }
        }
    }

    private object FragmentParams {
        const val REQUEST_KEY = "request_key"
        const val CURRENT_COLOR_CODE = "current_color_code"
    }

    override val featureName: String = "choose_custom_color"

    @Inject
    internal lateinit var feature: ChooseCustomColorFeature

    private var dialogView: View? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initDi()

        val currentColorCode = arguments?.getString(FragmentParams.CURRENT_COLOR_CODE)
        feature.sendIntent(ChooseCustomColorFeature.Intent.LoadColors(currentColorCode))

        dialogView = ComposeView(requireActivity()).apply {
            setContent {
                ChooseCustomColorScreen(state = feature.state)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.select_color)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
    }

    @Composable
    private fun ChooseCustomColorScreen(state: ChooseCustomColorFeature.State) {
        FoboReaderTheme {
            if (state is ChooseCustomColorFeature.State.Loaded) {
                CustomColorsScreen(state.colors)
            }
        }
    }

    @Composable
    private fun CustomColorsScreen(colors: List<ColorItem>) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = (COLOR_CIRCLE_SIZE + COLOR_CIRCLE_PADDING * 2).dp),
            modifier = Modifier.padding(12.dp)
        ) {
            items(colors.size) { pos ->
                val color = colors[pos]
                ColorCircleComposable(color)
            }
        }
    }

    @Composable
    private fun ColorCircleComposable(color: ColorItem) {
        Surface(
            modifier = Modifier
                .padding(all = COLOR_CIRCLE_PADDING.dp)
                .aspectRatio(1f)
                .clickable {
                    feature.sendIntent(ChooseCustomColorFeature.Intent.SelectColor(color.colorCode))
                },
            shape = CircleShape,
            color = color.colorCode.fromHex()
        ) {
            Box(modifier = Modifier.size(size = COLOR_CIRCLE_SIZE.dp))
            if (color.selected) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    tint = color.checkColorCode.fromHex(),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

    private fun String.fromHex() = Color(android.graphics.Color.parseColor(this))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return requireNotNull(dialogView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onCleared() {
        feature.onCleared()
    }

    private fun takeEffect(effect: ChooseCustomColorFeature.Effect) = when (effect) {
        is ChooseCustomColorFeature.Effect.Dismiss -> {
            val requestCode = requireNotNull(arguments?.getString(FragmentParams.REQUEST_KEY))
            requireActivity().supportFragmentManager.setFragmentResult(
                requestCode, Bundle().apply {
                    putString(RETURN_RESULT_COLOR_CODE, effect.selectedColorCode)
                }
            )
            dismiss()
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        ChooseCustomColorScreen(
            state = ChooseCustomColorFeature.State.Loaded(
                listOf(
                    ColorItem(
                        "#000000",
                        "#ffffff", // TODO: Automatic detection?
                        true,
                    ),
                    ColorItem(
                        "#000000",
                        "#ffffff",
                        true,
                    ),
                    ColorItem(
                        "#ff0000",
                        "#00ffff",
                        true,
                    ),
                    ColorItem(
                        "#0000ff",
                        "#ffff00",
                        true,
                    ),
                )
            )
        )
    }
}