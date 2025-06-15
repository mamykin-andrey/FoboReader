package ru.mamykin.foboreader.onboarding

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class GetOnboardingPreferencesUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
) {
    fun execute(): OnboardingPreferences {
        return OnboardingPreferences(
            nativeLanguageCode = appSettingsRepository.getNativeLanguageCode(),
            targetLanguageCode = appSettingsRepository.getTargetLanguageCode(),
            languageLevel = appSettingsRepository.getLanguageLevel(),
            isCompleted = appSettingsRepository.isOnboardingCompleted()
        )
    }
} 