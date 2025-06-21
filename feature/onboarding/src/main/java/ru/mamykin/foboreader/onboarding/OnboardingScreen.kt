package ru.mamykin.foboreader.onboarding

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
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
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.sendIntent(OnboardingViewModel.Intent.NotificationPermissionGranted)
        } else {
            viewModel.sendIntent(OnboardingViewModel.Intent.NotificationPermissionDenied)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        when (state.currentStep) {
            OnboardingStep.LANGUAGE_SELECTION -> {
                LanguageSelectionScreen(
                    state = state,
                    onIntent = viewModel::sendIntent
                )
            }

            OnboardingStep.NOTIFICATION_PERMISSION -> {
                NotificationPermissionScreen(
                    state = state,
                    onIntent = viewModel::sendIntent,
                    launcher = launcher,
                )
            }
        }
    }
}

@Composable
private fun ProgressIndicator(currentStep: Int, totalSteps: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            progress = { currentStep.toFloat() / totalSteps.toFloat() },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.step_progress, currentStep, totalSteps),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LanguageSelectionScreen(
    state: OnboardingViewModel.State,
    onIntent: (OnboardingViewModel.Intent) -> Unit
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

        Spacer(modifier = Modifier.height(24.dp))

        // Progress Indicator
        ProgressIndicator(state.currentStepNumber, state.totalSteps)

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
            onClick = { onIntent(OnboardingViewModel.Intent.ContinueToNextStep) },
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

@Composable
private fun NotificationPermissionScreen(
    state: OnboardingViewModel.State,
    onIntent: (OnboardingViewModel.Intent) -> Unit,
    launcher: ActivityResultLauncher<String>,
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
            text = stringResource(R.string.notifications_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.notifications_subtitle),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Progress Indicator
        ProgressIndicator(state.currentStepNumber, state.totalSteps)

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.notifications_description),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        val activity = LocalContext.current as AppCompatActivity
        // Allow Button
        Button(
            onClick = { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = stringResource(R.string.allow_notifications),
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Skip Button
        OutlinedButton(
            onClick = { onIntent(OnboardingViewModel.Intent.SkipNotificationPermission) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.skip_notifications),
                fontSize = 14.sp
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
            LanguageSelectionScreen(state = OnboardingViewModel.State(
                supportedLanguages = SupportedOnboardingLanguages.supportedLanguages,
                levels = LanguageLevel.entries
            ), onIntent = {})
        }
    }
} 