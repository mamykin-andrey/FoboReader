package ru.mamykin.foboreader.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.core.navigation.MainTabScreenRoutes
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme

@Composable
fun OnboardingScreen(appNavController: NavHostController) {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is OnboardingViewModel.Effect.NavigateToMain -> {
                    appNavController.navigate(AppScreen.Main.createRoute(MainTabScreenRoutes.BOOKS_STORE)) {
                        popUpTo(AppScreen.Onboarding.route) { inclusive = true }
                    }
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        OnboardingScreenUI(
            state = state, onIntent = viewModel::sendIntent
        )
    }
}

@Composable
private fun OnboardingScreenUI(
    state: OnboardingViewModel.State, onIntent: (OnboardingViewModel.Intent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Title
        Text(
            text = stringResource(R.string.welcome_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.welcome_subtitle),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Native Language Dropdown
        LanguageDropdown(label = stringResource(R.string.native_language_label),
            selectedLanguage = state.selectedNativeLanguage,
            languages = state.supportedLanguages,
            onLanguageSelected = { onIntent(OnboardingViewModel.Intent.SelectNativeLanguage(it)) })

        Spacer(modifier = Modifier.height(24.dp))

        // Target Language Dropdown
        LanguageDropdown(label = stringResource(R.string.target_language_label),
            selectedLanguage = state.selectedTargetLanguage,
            languages = state.supportedLanguages,
            onLanguageSelected = { onIntent(OnboardingViewModel.Intent.SelectTargetLanguage(it)) })

        Spacer(modifier = Modifier.height(24.dp))

        // Language Level Dropdown
        LevelDropdown(label = stringResource(R.string.language_level_label),
            selectedLevel = state.selectedLevel,
            levels = state.levels,
            onLevelSelected = { onIntent(OnboardingViewModel.Intent.SelectLanguageLevel(it)) })

        Spacer(modifier = Modifier.weight(1f))

        // Continue Button
        Button(
            onClick = { onIntent(OnboardingViewModel.Intent.ContinueOnboarding) },
            enabled = state.isContinueEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = stringResource(R.string.continue_button), fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageDropdown(
    label: String,
    selectedLanguage: OnboardingLanguage?,
    languages: List<OnboardingLanguage>,
    onLanguageSelected: (OnboardingLanguage) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedLanguage?.name ?: stringResource(R.string.select_language),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                languages.forEach { language ->
                    DropdownMenuItem(text = { Text(language.name) }, onClick = {
                        onLanguageSelected(language)
                        expanded = false
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LevelDropdown(
    label: String, selectedLevel: LanguageLevel?, levels: List<LanguageLevel>, onLevelSelected: (LanguageLevel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedLevel?.displayName ?: stringResource(R.string.select_level),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                levels.forEach { level ->
                    DropdownMenuItem(text = { Text(level.displayName) }, onClick = {
                        onLevelSelected(level)
                        expanded = false
                    })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    FoboReaderTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            OnboardingScreenUI(state = OnboardingViewModel.State(
                supportedLanguages = SupportedOnboardingLanguages.supportedLanguages,
                levels = LanguageLevel.entries
            ), onIntent = {})
        }
    }
} 