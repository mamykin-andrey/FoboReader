package ru.mamykin.foboreader.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.di.DaggerMainComponent
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val nightThemeDelegate = NightThemeDelegate(this)
    private val appLanguageDelegate = AppLanguageDelegate(this)

    @Inject
    lateinit var cicerone: Cicerone<Router>

    @Inject
    lateinit var screenProvider: ScreenProvider

    private val router by lazy { cicerone.router }
    private val navigatorHolder by lazy { cicerone.getNavigatorHolder() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMainComponent.factory().create(apiHolder().navigationApi()).inject(this)
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
        appLanguageDelegate.init()
    }
}