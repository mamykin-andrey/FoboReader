package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.di.DaggerRootComponent
import ru.mamykin.foboreader.app.platform.PerformanceTracker
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.getAttrColor
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import javax.inject.Inject

internal class RootActivity : AppCompatActivity() {

    @Inject
    internal lateinit var appSettingsRepository: AppSettingsRepository

    @Inject
    internal lateinit var cicerone: Cicerone<Router>

    @Inject
    internal lateinit var screenProvider: ScreenProvider

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
        DaggerRootComponent.factory().create(
            apiHolder().navigationApi(),
            apiHolder().settingsApi()
        ).inject(this)
        initTheme()
        if (savedInstanceState == null) {
            router.newRootChain(screenProvider.mainScreen())
        }
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
}