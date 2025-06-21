package ru.mamykin.foboreader.onboarding

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class OnboardingViewModel @Inject constructor(
    private val saveOnboardingDataUseCase: SaveOnboardingDataUseCase,
) : BaseViewModel<OnboardingViewModel.Intent, OnboardingViewModel.State, OnboardingViewModel.Effect>(
    State()
) {
    init {
        state = State(
            supportedLanguages = SupportedOnboardingLanguages.supportedLanguages,
            levels = LanguageLevel.entries,
            currentStep = OnboardingStep.LANGUAGE_SELECTION,
            totalSteps = OnboardingStep.entries.size
        )
    }

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.SelectNativeLanguage -> {
                state = state.copy(selectedNativeLanguage = intent.language)
            }

            is Intent.SelectTargetLanguage -> {
                state = state.copy(selectedTargetLanguage = intent.language)
            }

            is Intent.SelectLanguageLevel -> {
                state = state.copy(selectedLevel = intent.level)
            }

            is Intent.ContinueToNextStep -> continueToNextStep()

            is Intent.SkipNotificationPermission -> completeOnboarding()

            is Intent.NotificationPermissionGranted -> completeOnboarding()

            is Intent.NotificationPermissionDenied -> completeOnboarding() // could be used to show a dialog to the user
        }
    }

    private fun continueToNextStep() {
        when (state.currentStep) {
            OnboardingStep.LANGUAGE_SELECTION -> {
                if (isLanguageSelectionComplete(state)) {
                    state = state.copy(currentStep = OnboardingStep.NOTIFICATION_PERMISSION)
                }
            }

            OnboardingStep.NOTIFICATION_PERMISSION -> {
                throw IllegalStateException("Trying to continue to the next step on the last step")
            }
        }
    }

    private fun isLanguageSelectionComplete(state: State): Boolean {
        return state.selectedNativeLanguage != null &&
            state.selectedTargetLanguage != null &&
            state.selectedLevel != null
    }

    private suspend fun completeOnboarding() {
        val currentState = state
        if (currentState.selectedNativeLanguage != null &&
            currentState.selectedTargetLanguage != null &&
            currentState.selectedLevel != null
        ) {
            val onboardingData = OnboardingData(
                nativeLanguage = currentState.selectedNativeLanguage,
                targetLanguage = currentState.selectedTargetLanguage,
                languageLevel = currentState.selectedLevel
            )
            saveOnboardingDataUseCase.execute(onboardingData)
            sendEffect(Effect.NavigateToMain)
        }
    }

    sealed class Intent {
        data class SelectNativeLanguage(val language: OnboardingLanguage) : Intent()
        data class SelectTargetLanguage(val language: OnboardingLanguage) : Intent()
        data class SelectLanguageLevel(val level: LanguageLevel) : Intent()
        data object ContinueToNextStep : Intent()
        data object SkipNotificationPermission : Intent()
        data object NotificationPermissionGranted : Intent()
        data object NotificationPermissionDenied : Intent()
    }

    data class State(
        val supportedLanguages: List<OnboardingLanguage> = emptyList(),
        val levels: List<LanguageLevel> = emptyList(),
        val selectedNativeLanguage: OnboardingLanguage? = null,
        val selectedTargetLanguage: OnboardingLanguage? = null,
        val selectedLevel: LanguageLevel? = null,
        val currentStep: OnboardingStep = OnboardingStep.LANGUAGE_SELECTION,
        val totalSteps: Int = 2,
    ) {
        val isContinueEnabled: Boolean
            get() = when (currentStep) {
                OnboardingStep.LANGUAGE_SELECTION -> selectedNativeLanguage != null &&
                    selectedTargetLanguage != null && selectedLevel != null

                OnboardingStep.NOTIFICATION_PERMISSION -> true
            }

        val currentStepNumber: Int
            get() = currentStep.stepNumber
    }

    sealed class Effect {
        data object NavigateToMain : Effect()
    }
}

internal enum class OnboardingStep(val stepNumber: Int) {
    LANGUAGE_SELECTION(1),
    NOTIFICATION_PERMISSION(2);
} 