package ru.mamykin.foboreader.onboarding

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class SaveOnboardingDataUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
) {
    fun execute(onboardingData: OnboardingData) {
        appSettingsRepository.setNativeLanguageCode(onboardingData.nativeLanguage.code)
        appSettingsRepository.setTargetLanguageCode(onboardingData.targetLanguage.code)
        appSettingsRepository.setLanguageLevel(onboardingData.languageLevel.code)
        appSettingsRepository.setOnboardingCompleted(true)
    }
} 