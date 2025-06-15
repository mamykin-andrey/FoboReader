package ru.mamykin.foboreader.onboarding

internal data class OnboardingData(
    val nativeLanguage: OnboardingLanguage,
    val targetLanguage: OnboardingLanguage,
    val languageLevel: LanguageLevel,
) 