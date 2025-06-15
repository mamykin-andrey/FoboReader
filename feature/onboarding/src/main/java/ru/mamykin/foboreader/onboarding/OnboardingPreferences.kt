package ru.mamykin.foboreader.onboarding

internal data class OnboardingPreferences(
    val nativeLanguageCode: String,
    val targetLanguageCode: String,
    val languageLevel: String,
    val isCompleted: Boolean
)