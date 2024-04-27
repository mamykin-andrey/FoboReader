package ru.mamykin.foboreader.settings.all_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.settings.DaggerSettingsComponent
import ru.mamykin.foboreader.settings.DialogDismissedListener
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.app_language.ChangeLanguageDialogFragment
import ru.mamykin.foboreader.settings.custom_color.ChooseCustomColorDialogFragment
import ru.mamykin.foboreader.uikit.compose.ColoredCircleCompose
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles
import javax.inject.Inject

class SettingsFragment : BaseFragment(), DialogDismissedListener {

    companion object {
        fun newInstance(): Fragment = SettingsFragment()
    }

    override val featureName: String = "settings"

    @Inject
    internal lateinit var feature: SettingsFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            SettingsScreen(feature.state)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFeature()
    }

    override fun onDismiss() {
        feature.sendIntent(SettingsFeature.Intent.LoadSettings)
    }

    private fun initFeature() {
        feature.effectFlow.collectWithRepeatOnStarted(::takeEffect)
    }

    private fun takeEffect(effect: SettingsFeature.Effect) {
        when (effect) {
            is SettingsFeature.Effect.SelectReadColor -> {
                ChooseCustomColorDialogFragment.newInstance().show(
                    childFragmentManager,
                    ChooseCustomColorDialogFragment.TAG
                )
            }
            // TODO: Update background color, not read color
            is SettingsFeature.Effect.SelectBackgroundColor -> {
                ChooseCustomColorDialogFragment.newInstance().show(
                    childFragmentManager,
                    ChooseCustomColorDialogFragment.TAG
                )
            }
            is SettingsFeature.Effect.SelectAppLanguage -> {
                ChangeLanguageDialogFragment.newInstance().show(
                    childFragmentManager,
                    ChangeLanguageDialogFragment.TAG
                )
            }
        }
    }

    @Composable
    private fun SettingsScreen(state: SettingsFeature.State) {
        FoboReaderTheme {
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.settings_title))
                    },
                    elevation = 12.dp
                )
            }, content = {
                when (state) {
                    is SettingsFeature.State.Loading -> LoadingComposable()
                    is SettingsFeature.State.Content -> ContentComposable(state)
                }
            })
        }
    }

    @Composable
    private fun LoadingComposable() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        }
    }

    @Composable
    private fun ContentComposable(state: SettingsFeature.State.Content) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // TODO: Fix the items clickable area
            NightThemeComposable(state)
            BackgroundColorComposable(state)
            TranslationColorComposable(state)
            TextSizeComposable(state)
            AppLanguageComposable(state)
            UseVibrationComposable(state)
        }
    }

    @Composable
    private fun NightThemeComposable(state: SettingsFeature.State.Content) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                stringResource(id = R.string.settings_night_theme),
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
            )
            Switch(
                checked = state.settings.isNightThemeEnabled,
                onCheckedChange = {
                    feature.sendIntent(SettingsFeature.Intent.ChangeNightTheme(it))
                }
            )
        }
    }

    @Composable
    private fun BackgroundColorComposable(state: SettingsFeature.State.Content) {
        ColorRowComposable(
            colorHex = state.settings.backgroundColor,
            titleRes = R.string.settings_bg_color_title
        ) {
            feature.sendIntent(SettingsFeature.Intent.SelectBackgroundColor)
        }
    }

    private fun getBackgroundColor(): String {
        return "#FF0033"
    }

    @Composable
    private fun TranslationColorComposable(state: SettingsFeature.State.Content) {
        ColorRowComposable(
            colorHex = getBackgroundColor(),
            titleRes = R.string.settings_translate_color_title
        ) {
            // TODO:
            feature.sendIntent(SettingsFeature.Intent.SelectReadColor)
        }
    }

    @Composable
    private fun ColorRowComposable(
        colorHex: String,
        @StringRes titleRes: Int,
        onClick: () -> Unit,
    ) {
        val color = remember { mutableStateOf(colorHex) }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    onClick()
                }
        ) {
            Text(
                text = stringResource(id = titleRes),
                modifier = Modifier.weight(1f),
            )
            ColoredCircleCompose(
                colorHexCode = color.value,
                size = 24.dp,
            )
        }
    }

    @Composable
    private fun TextSizeComposable(state: SettingsFeature.State.Content) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.settings_text_size),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
            )
            Button(
                onClick = { feature.sendIntent(SettingsFeature.Intent.IncreaseTextSize) },
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray, contentColor = Color.White),
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterVertically),
                contentPadding = PaddingValues(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
            Text(
                text = state.settings.textSize.toString(),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically),
            )
            Button(
                onClick = { feature.sendIntent(SettingsFeature.Intent.DecreaseTextSize) },
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray, contentColor = Color.White),
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterVertically),
                contentPadding = PaddingValues(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }

    @Composable
    private fun AppLanguageComposable(state: SettingsFeature.State.Content) {
        val language = state.settings.languageName
        Text(
            text = language,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    feature.sendIntent(SettingsFeature.Intent.SelectAppLanguage)
                }
        )
    }

    @Composable
    private fun UseVibrationComposable(state: SettingsFeature.State.Content) {
        val useVibration = state.settings.isVibrationEnabled
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.use_vibration_option_name)
                )
                Text(
                    text = stringResource(id = R.string.use_vibration_option_description),
                    style = TextStyles.Body2,
                )
            }
            Checkbox(
                checked = useVibration,
                onCheckedChange = {
                    feature.sendIntent(SettingsFeature.Intent.ChangeUseVibration(it))
                }
            )
        }
    }

    @Composable
    @Preview
    fun SettingsScreenPreview() {
        SettingsScreen(
            state = SettingsFeature.State.Content(
                AppSettings(
                    true,
                    "#ffffff",
                    "#ffffff",
                    20,
                    "Russian",
                    true,
                )
            )
        )
    }
}