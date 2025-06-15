package ru.mamykin.foboreader.onboarding

internal data class OnboardingLanguage(
    val code: String,
    val name: String,
)

internal object SupportedOnboardingLanguages {
    val supportedLanguages = listOf(
        OnboardingLanguage("en", "English"),
        OnboardingLanguage("ru", "Russian"),
        OnboardingLanguage("nl", "Dutch"),
    )
} 