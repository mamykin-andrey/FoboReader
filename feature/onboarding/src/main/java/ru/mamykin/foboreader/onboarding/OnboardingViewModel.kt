package ru.mamykin.foboreader.onboarding

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
            levels = LanguageLevel.entries
        )
    }

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.SelectNativeLanguage -> {
                val newState = state.copy(selectedNativeLanguage = intent.language)
                state = newState.copy(isContinueEnabled = areFieldsComplete(newState))
            }

            is Intent.SelectTargetLanguage -> {
                val newState = state.copy(selectedTargetLanguage = intent.language)
                state = newState.copy(isContinueEnabled = areFieldsComplete(newState))
            }

            is Intent.SelectLanguageLevel -> {
                val newState = state.copy(selectedLevel = intent.level)
                state = newState.copy(isContinueEnabled = areFieldsComplete(newState))
            }

            is Intent.ContinueOnboarding -> {
                continueOnboarding(state)
            }
        }
    }

    private fun areFieldsComplete(state: State): Boolean {
        return state.selectedNativeLanguage != null
            && state.selectedTargetLanguage != null
            && state.selectedLevel != null
    }

    private suspend fun continueOnboarding(state: State) {
        if (state.selectedNativeLanguage != null &&
            state.selectedTargetLanguage != null &&
            state.selectedLevel != null
        ) {
            val onboardingData = OnboardingData(
                nativeLanguage = state.selectedNativeLanguage,
                targetLanguage = state.selectedTargetLanguage,
                languageLevel = state.selectedLevel
            )
            saveOnboardingDataUseCase.execute(onboardingData)
            sendEffect(Effect.NavigateToMain)
        }
    }

    sealed class Intent {
        data class SelectNativeLanguage(val language: OnboardingLanguage) : Intent()
        data class SelectTargetLanguage(val language: OnboardingLanguage) : Intent()
        data class SelectLanguageLevel(val level: LanguageLevel) : Intent()
        data object ContinueOnboarding : Intent()
    }

    data class State(
        val supportedLanguages: List<OnboardingLanguage> = emptyList(),
        val levels: List<LanguageLevel> = emptyList(),
        val selectedNativeLanguage: OnboardingLanguage? = null,
        val selectedTargetLanguage: OnboardingLanguage? = null,
        val selectedLevel: LanguageLevel? = null,
        val isContinueEnabled: Boolean = false,
    )

    sealed class Effect {
        data object NavigateToMain : Effect()
    }
} 