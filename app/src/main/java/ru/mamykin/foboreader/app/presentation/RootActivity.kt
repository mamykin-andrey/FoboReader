package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.di.DaggerRootComponent
import ru.mamykin.foboreader.app.platform.PerformanceTracker
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.extension.getAttrColor
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.platform.PermissionManager
import ru.mamykin.foboreader.core.platform.RequestedPermission
import javax.inject.Inject

internal class RootActivity : AppCompatActivity() {

    @Inject
    internal lateinit var appSettingsRepository: AppSettingsRepository

    @Inject
    internal lateinit var cicerone: Cicerone<Router>

    @Inject
    internal lateinit var screenProvider: ScreenProvider

    @Inject
    internal lateinit var permissionManager: PermissionManager

    private val router by lazy { cicerone.router }
    private val navigatorHolder by lazy { cicerone.getNavigatorHolder() }
    private val rootView by lazy {
        FragmentContainerView(this).apply {
            id = R.id.fcv_root
            setBackgroundColor(getAttrColor(android.R.attr.colorBackground))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rootView)
        initDi()
        permissionManager.init(this)
        initTheme()
        requestNotificationPermission()
        if (savedInstanceState == null) {
            router.newRootChain(screenProvider.mainScreen())
        }
    }

    private fun initDi() {
        DaggerRootComponent.factory().create(
            apiHolder().navigationApi(),
            apiHolder().settingsApi(),
            commonApi(),
        ).inject(this)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(AppNavigator(this, R.id.fcv_root))
        PerformanceTracker.startTracking(rootView, window)
    }

    override fun onStop() {
        super.onStop()
        navigatorHolder.removeNavigator()
        PerformanceTracker.stopTracking()
    }

    private fun initTheme() {
        setTheme(R.style.AppTheme)
        NightThemeDelegate(this, appSettingsRepository).init()
        AppLanguageDelegate(this, appSettingsRepository).init()
    }

    private fun requestNotificationPermission() {
        lifecycleScope.launch {
            // TODO: Show rationale message if not granted
            permissionManager.requestPermissions(RequestedPermission.NOTIFICATIONS)
        }
    }
}