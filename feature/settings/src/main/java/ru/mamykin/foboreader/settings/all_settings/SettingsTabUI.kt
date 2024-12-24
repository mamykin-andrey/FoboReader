package ru.mamykin.foboreader.settings.all_settings

import android.annotation.SuppressLint
import android.app.Activity
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.changeLocale
import ru.mamykin.foboreader.core.extension.getActivity
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.app_language.ChangeLanguageDialogUI
import ru.mamykin.foboreader.settings.custom_color.ChooseCustomColorDialogUI
import ru.mamykin.foboreader.uikit.compose.ColoredCircleCompose
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles

// TODO: Use viewModelStore instead
private const val SCREEN_KEY = "settings"

private fun createAndInitViewModel(
    apiHolder: ApiHolder,
): SettingsViewModel {
    return ComponentHolder.getOrCreateComponent(key = SCREEN_KEY) {
        DaggerSettingsComponent.factory().create(
            apiHolder.commonApi(),
            apiHolder.settingsApi(),
        )
    }.settingsViewModel().also { it.sendIntent(SettingsViewModel.Intent.LoadSettings) }
}

@Composable
fun SettingsTabUI() {
    val context = LocalContext.current
    val viewModel = remember { createAndInitViewModel(context.getActivity().apiHolder()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        onDispose {
            if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                ComponentHolder.clearComponent(SCREEN_KEY)
            }
        }
    }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(it, context.getActivity())
        }
    }
    SettingsScreen(viewModel.state, viewModel::sendIntent)
}

private fun takeEffect(effect: SettingsViewModel.Effect, activity: Activity) {
    when (effect) {
        is SettingsViewModel.Effect.SwitchTheme -> {
            setNightModeEnabled(effect.isNightTheme)
        }

        is SettingsViewModel.Effect.SwitchLanguage -> {
            activity.changeLocale(effect.languageCode)
        }
    }
}

private fun setNightModeEnabled(enabled: Boolean) {
    val newMode = if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
    AppCompatDelegate.setDefaultNightMode(newMode)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun SettingsScreen(
    state: SettingsViewModel.State,
    onIntent: (SettingsViewModel.Intent) -> Unit,
) {
    FoboReaderTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.settings_title))
                }, elevation = 12.dp, backgroundColor = MaterialTheme.colors.primary
            )
        }, content = {
            when (state) {
                is SettingsViewModel.State.Loading -> LoadingComposable()
                is SettingsViewModel.State.Content -> ContentComposable(state, onIntent)
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
private fun ContentComposable(state: SettingsViewModel.State.Content, onIntent: (SettingsViewModel.Intent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // TODO: Fix the items clickable area
        NightThemeComposable(state, onIntent)
        BackgroundColorComposable(state, onIntent)
        TranslationColorComposable(state, onIntent)
        TextSizeComposable(state, onIntent)
        AppLanguageComposable(state, onIntent)
        UseVibrationComposable(state, onIntent)
        if (state.backgroundColorDialogCode != null) {
            ChooseCustomColorDialogUI(
                currentColorCode = state.backgroundColorDialogCode,
                title = "Select background color",
            ) {
                onIntent(SettingsViewModel.Intent.ChangeBackgroundColor(it))
            }
        }
        if (state.translationColorDialogCode != null) {
            ChooseCustomColorDialogUI(
                currentColorCode = state.translationColorDialogCode,
                title = "Select translation color",
            ) {
                onIntent(SettingsViewModel.Intent.ChangeTranslationColor(it))
            }
        }
        if (state.isChangeLanguageDialogShown) {
            ChangeLanguageDialogUI {
                onIntent(SettingsViewModel.Intent.AppLanguageChanged(it))
            }
        }
    }
}

@Composable
private fun NightThemeComposable(state: SettingsViewModel.State.Content, onIntent: (SettingsViewModel.Intent) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            stringResource(id = R.string.settings_night_theme),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
        )
        Switch(checked = state.settings.isNightThemeEnabled, onCheckedChange = {
            onIntent(SettingsViewModel.Intent.SwitchTheme(it))
        })
    }
}

@Composable
private fun BackgroundColorComposable(
    state: SettingsViewModel.State.Content, onIntent: (SettingsViewModel.Intent) -> Unit
) {
    ColorRowComposable(
        colorHex = state.settings.backgroundColor, titleRes = R.string.settings_bg_color_title
    ) {
        onIntent(SettingsViewModel.Intent.SelectBackgroundColor)
    }
}

@Composable
private fun TranslationColorComposable(
    state: SettingsViewModel.State.Content, onIntent: (SettingsViewModel.Intent) -> Unit
) {
    ColorRowComposable(
        colorHex = state.settings.translationColor, titleRes = R.string.settings_translate_color_title
    ) {
        onIntent(SettingsViewModel.Intent.SelectTranslationColor)
    }
}

@Composable
private fun ColorRowComposable(
    colorHex: String,
    @StringRes titleRes: Int,
    onClick: () -> Unit,
) {
    Row(modifier = Modifier
        .padding(16.dp)
        .clickable {
            onClick()
        }) {
        Text(
            text = stringResource(id = titleRes),
            modifier = Modifier.weight(1f),
        )
        ColoredCircleCompose(
            colorHexCode = colorHex,
            size = 24.dp,
        )
    }
}

@Composable
private fun TextSizeComposable(state: SettingsViewModel.State.Content, onIntent: (SettingsViewModel.Intent) -> Unit) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.settings_text_size),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
        )
        Button(
            onClick = { onIntent(SettingsViewModel.Intent.IncreaseTextSize) },
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
            onClick = { onIntent(SettingsViewModel.Intent.DecreaseTextSize) },
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
private fun AppLanguageComposable(
    state: SettingsViewModel.State.Content, onIntent: (SettingsViewModel.Intent) -> Unit
) {
    val language = state.settings.languageName
    Text(text = language, modifier = Modifier
        .padding(16.dp)
        .clickable {
            onIntent(SettingsViewModel.Intent.SelectAppLanguage)
        })
}

@Composable
private fun UseVibrationComposable(
    state: SettingsViewModel.State.Content, onIntent: (SettingsViewModel.Intent) -> Unit
) {
    val useVibration = state.settings.isVibrationEnabled
    Row(
        modifier = Modifier.padding(16.dp)
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
        Checkbox(checked = useVibration, onCheckedChange = {
            onIntent(SettingsViewModel.Intent.ChangeUseVibration(it))
        })
    }
}

@Composable
@Preview
fun SettingsScreenPreview() {
    SettingsScreen(
        state = SettingsViewModel.State.Content(
            AppSettings(
                true,
                "#ffffff",
                "#ffffff",
                20,
                "Russian",
                true,
            )
        ),
        onIntent = {},
    )
}