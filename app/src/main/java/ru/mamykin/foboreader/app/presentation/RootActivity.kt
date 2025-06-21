package ru.mamykin.foboreader.app.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.extension.changeLocale
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import javax.inject.Inject

@AndroidEntryPoint
internal class RootActivity : AppCompatActivity() {

    @Inject
    internal lateinit var appSettingsRepository: AppSettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAppPreferences()
        setContent {
            var isDarkTheme by remember { mutableStateOf(appSettingsRepository.isNightThemeEnabled()) }
            DisposableEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.BLACK, Color.WHITE) { isDarkTheme }
                )
                onDispose { }
            }
            FoboReaderTheme(darkTheme = isDarkTheme) {
                AppNavigation(
                    onNightThemeSwitch = { isDarkTheme = it },
                    isOnboardingCompleted = appSettingsRepository.isOnboardingCompleted()
                )
            }
        }
    }

    private fun initAppPreferences() {
        changeLocale(appSettingsRepository.getAppLanguageCode())
    }
}