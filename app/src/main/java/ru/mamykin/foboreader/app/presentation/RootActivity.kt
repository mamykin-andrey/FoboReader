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
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.extension.changeLocale
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.core.platform.PermissionManager
import ru.mamykin.foboreader.core.platform.RequestedPermission
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import javax.inject.Inject

@AndroidEntryPoint
internal class RootActivity : AppCompatActivity() {

    @Inject
    internal lateinit var appSettingsRepository: AppSettingsRepository

    @Inject
    internal lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAppPreferences()
        initPermissions()
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

    private fun initPermissions() = lifecycleScope.launch {
        if (!permissionManager.requestPermission(this@RootActivity, RequestedPermission.NOTIFICATIONS)) {
            Log.debug("Notifications permission isn't granted!")
        }
    }
}