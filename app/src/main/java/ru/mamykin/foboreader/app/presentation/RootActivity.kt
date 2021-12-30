package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.di.DaggerRootComponent
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import javax.inject.Inject

class RootActivity : AppCompatActivity(R.layout.activity_root) {

    private val nightThemeDelegate = NightThemeDelegate(this)

    @Inject
    lateinit var appSettingsStorage: AppSettingsStorage

    @Inject
    lateinit var cicerone: Cicerone<Router>

    @Inject
    lateinit var screenProvider: ScreenProvider

    private val router by lazy { cicerone.router }
    private val navigatorHolder by lazy { cicerone.getNavigatorHolder() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerRootComponent.factory().create(
            apiHolder().navigationApi(),
            apiHolder().settingsApi()
        ).inject(this)
        initTheme()
        if (savedInstanceState == null) {
            router.newRootChain(screenProvider.tabsScreen())
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(AppNavigator(this, R.id.fcv_root))
    }

    override fun onStop() {
        super.onStop()
        navigatorHolder.removeNavigator()
    }

    private fun initTheme() {
        setTheme(R.style.AppTheme)
        nightThemeDelegate.init()
        AppLanguageDelegate(this, appSettingsStorage).init()
    }
}