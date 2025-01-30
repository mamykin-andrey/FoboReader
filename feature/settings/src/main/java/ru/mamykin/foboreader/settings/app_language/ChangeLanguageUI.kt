package ru.mamykin.foboreader.settings.app_language

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ru.mamykin.foboreader.settings.R
import ru.mamykin.foboreader.uikit.compose.TextStyles

@Composable
fun ChooseAppLanguageDialogUI(navController: NavHostController) {
    val viewModel: ChangeLanguageViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(ChangeLanguageViewModel.Intent.LoadLanguages)
    }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect { effect ->
            if (effect is ChangeLanguageViewModel.Effect.Dismiss) {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("choose_app_language_result", effect.selectedLanguageCode)
                navController.popBackStack()
            }
        }
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ChangeLanguageDialogComposable(
        state,
        viewModel::sendIntent,
    )
}

@Composable
private fun ChangeLanguageDialogComposable(
    state: ChangeLanguageViewModel.State,
    onIntent: (ChangeLanguageViewModel.Intent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onIntent(ChangeLanguageViewModel.Intent.Dismiss) },
        title = {
            Text(text = stringResource(R.string.select_app_language))
        },
        text = {
            Column {
                state.languages.forEach { LanguageItemComposable(it, onIntent) }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = {
                onIntent(ChangeLanguageViewModel.Intent.Dismiss)
            }) {
                Text(text = stringResource(android.R.string.cancel))
            }
        }
    )
}

@Composable
private fun LanguageItemComposable(
    language: AppLanguage,
    onIntent: (ChangeLanguageViewModel.Intent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onIntent(ChangeLanguageViewModel.Intent.SelectLanguage(language.code)) })
    ) {
        Text(
            text = language.name,
            style = TextStyles.Subtitle1,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
fun ChangeLanguageScreenPreview() {
    ChangeLanguageDialogComposable(
        state = ChangeLanguageViewModel.State(
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
        ),
        onIntent = {},
    )
}