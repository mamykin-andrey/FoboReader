package ru.mamykin.foboreader.settings.all_settings

import android.app.Activity
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ru.mamykin.foboreader.core.extension.changeLocale
import ru.mamykin.foboreader.core.extension.getActivity
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.settings.custom_color.ChooseColorResult
import ru.mamykin.foboreader.uikit.compose.ColoredCircleCompose
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.GenericLoadingIndicatorComposable
import ru.mamykin.foboreader.uikit.compose.TextStyles

@Composable
fun SettingsTabUI(appNavController: NavHostController, onNightThemeSwitch: (Boolean) -> Unit) {
    val context = LocalContext.current
    val viewModel: SettingsViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(SettingsViewModel.Intent.LoadSettings)
    }
    appNavController.HandleResultState<ChooseColorResult>("choose_color_result") {
        viewModel.sendIntent(SettingsViewModel.Intent.ChangeColor(it.type, it.colorCode))
    }
    appNavController.HandleResultState<String>("choose_app_language_result") {
        viewModel.sendIntent(SettingsViewModel.Intent.ChangeAppLanguage(it))
    }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(
                effect = it,
                activity = context.getActivity(),
                onNightThemeSwitch = onNightThemeSwitch,
                appNavController = appNavController,
            )
        }
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        onIntent = viewModel::sendIntent,
    )
}

@Composable
private fun <R> NavHostController.HandleResultState(resultKey: String, onHandle: (result: R) -> Unit) {
    this.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<R>(resultKey)
        ?.observeAsState()
        ?.value
        ?.let { LaunchedEffect(it) { onHandle(it) } }
}

private fun takeEffect(
    effect: SettingsViewModel.Effect,
    activity: Activity,
    onNightThemeSwitch: (Boolean) -> Unit,
    appNavController: NavHostController,
) {
    when (effect) {
        is SettingsViewModel.Effect.SwitchTheme -> {
            onNightThemeSwitch(effect.isNightTheme)
        }

        is SettingsViewModel.Effect.SwitchLanguage -> {
            activity.changeLocale(effect.languageCode)
        }

        is SettingsViewModel.Effect.ChooseColor -> {
            appNavController.navigate(AppScreen.ChooseColor.createRoute(effect.type))
        }

        is SettingsViewModel.Effect.ChooseAppLanguage -> {
            appNavController.navigate(AppScreen.ChooseAppLanguage.route)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingsViewModel.State,
    onIntent: (SettingsViewModel.Intent) -> Unit,
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.settings_title))
            },
            windowInsets = WindowInsets(0.dp),
        )
    }, content = { innerPadding ->
        Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
            when (state) {
                is SettingsViewModel.State.Loading -> GenericLoadingIndicatorComposable()
                is SettingsViewModel.State.Content -> ContentComposable(state, onIntent)
            }
        }
    })
}

@Composable
private fun ContentComposable(
    state: SettingsViewModel.State.Content,
    onIntent: (SettingsViewModel.Intent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NightThemeComposable(state, onIntent)
        BackgroundColorComposable(state, onIntent)
        TextColorComposable(state, onIntent)
        TranslationColorComposable(state, onIntent)
        TextSizeComposable(state, onIntent)
        AppLanguageComposable(state, onIntent)
        UseVibrationComposable(state, onIntent)
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
        onIntent(SettingsViewModel.Intent.ChooseColor(type = AppScreen.ChooseColor.CustomColorType.BACKGROUND))
    }
}

@Composable
private fun TextColorComposable(
    state: SettingsViewModel.State.Content, onIntent: (SettingsViewModel.Intent) -> Unit
) {
    ColorRowComposable(
        colorHex = state.settings.textColor, titleRes = R.string.settings_text_color_title
    ) {
        onIntent(SettingsViewModel.Intent.ChooseColor(type = AppScreen.ChooseColor.CustomColorType.TEXT))
    }
}

@Composable
private fun TranslationColorComposable(
    state: SettingsViewModel.State.Content, onIntent: (SettingsViewModel.Intent) -> Unit
) {
    ColorRowComposable(
        colorHex = state.settings.translationColor, titleRes = R.string.settings_translate_color_title
    ) {
        onIntent(SettingsViewModel.Intent.ChooseColor(type = AppScreen.ChooseColor.CustomColorType.TRANSLATION))
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
    FoboReaderTheme {
        SettingsScreen(
            state = SettingsViewModel.State.Content(
                AppSettings(
                    true,
                    "#ffffff",
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
}