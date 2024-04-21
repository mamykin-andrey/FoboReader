package ru.mamykin.foboreader.settings.app_language

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.presentation.BaseDialogFragment
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.di.DaggerSettingsComponent
import ru.mamykin.foboreader.settings.DialogDismissedListener
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles
import javax.inject.Inject

internal class ChangeLanguageDialogFragment : BaseDialogFragment() {

    companion object {

        const val TAG = "ChangeLanguageDialogFragment"

        fun newInstance() = ChangeLanguageDialogFragment()
    }

    override val featureName: String = "change_app_language"

    @Inject
    internal lateinit var feature: ChangeLanguageFeature

    private var dialogView: View? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initDi()
        dialogView = ComposeView(requireActivity()).apply {
            setContent {
                ChangeLanguageScreen(feature.state)
            }
        }

        return AlertDialog.Builder(requireActivity())
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
        feature.effectFlow.collectWithRepeatOnStarted(::takeEffect)
    }

    @Composable
    private fun ChangeLanguageScreen(state: ChangeLanguageFeature.State) {
        FoboReaderTheme {
            if (state is ChangeLanguageFeature.State.Loaded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    state.languages.forEach { LanguageItemComposable(it) }
                }
            }
        }
    }

    @Composable
    private fun LanguageItemComposable(language: AppLanguage) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    feature.sendIntent(ChangeLanguageFeature.Intent.SelectLanguage(language.code))
                }
            )) {
            Text(
                text = language.name,
                style = TextStyles.Subtitle1,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(horizontal = 16.dp)
            )
        }
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

    private fun takeEffect(effect: ChangeLanguageFeature.Effect) = when (effect) {
        ChangeLanguageFeature.Effect.Dismiss -> dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as? DialogDismissedListener)?.onDismiss()
    }

    override fun onCleared() {
        feature.onCleared()
    }

    @Preview
    @Composable
    fun ChangeLanguageScreenPreview() {
        ChangeLanguageScreen(
            state = ChangeLanguageFeature.State.Loaded(
                listOf(
                    AppLanguage(
                        "ru",
                        "Русский",
                    ),
                    AppLanguage(
                        "en",
                        "English",
                    ),
                )
            )
        )
    }
}