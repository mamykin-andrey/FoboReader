package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.di.DaggerRootComponent
import ru.mamykin.foboreader.app.platform.PerformanceTracker
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.databinding.ActivityRootBinding
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
    private val binding by lazy { ActivityRootBinding.inflate(LayoutInflater.from(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
        PerformanceTracker.startTracking(binding.root, window)
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