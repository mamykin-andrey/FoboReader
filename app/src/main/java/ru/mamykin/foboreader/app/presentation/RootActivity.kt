package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.app.di.DaggerRootComponent
import ru.mamykin.foboreader.app.platform.PerformanceTracker
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.changeLocale
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.core.platform.PermissionManager
import ru.mamykin.foboreader.core.platform.RequestedPermission
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import javax.inject.Inject

internal class RootActivity : AppCompatActivity() {

    @Inject
    internal lateinit var appSettingsRepository: AppSettingsRepository

    @Inject
    internal lateinit var permissionManager: PermissionManager

    private lateinit var contentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initDi()
        setContent {
            val isDarkTheme = remember { mutableStateOf(appSettingsRepository.isNightThemeEnabled()) }
            FoboReaderTheme(darkTheme = isDarkTheme.value) {
                AppNavigation {
                    isDarkTheme.value = it
                }
            }
        }
        contentView = findViewById(android.R.id.content)
        initAppPreferences()
        initPermissions()
    }

    private fun initDi() {
        DaggerRootComponent.factory().create(
            apiHolder().settingsApi(),
            apiHolder().commonApi(),
        ).inject(this)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        PerformanceTracker.startTracking(contentView, window)
    }

    override fun onStop() {
        super.onStop()
        PerformanceTracker.stopTracking()
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