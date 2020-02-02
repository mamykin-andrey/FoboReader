package ru.mamykin.foboreader.ui

import android.content.Context
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.root_activity.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.mamykin.core.data.SettingsStorage
import ru.mamykin.core.di.coreModule
import ru.mamykin.core.extension.startActivity
import ru.mamykin.core.platform.Router
import ru.mamykin.core.ui.BaseActivity
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.platform.MainRouter
import ru.mamykin.my_books.di.myBooksModule
import ru.mamykin.read_book.di.readBookModule
import ru.mamykin.settings.di.settingsModule
import ru.mamykin.store.di.booksStoreModule

class RootActivity : BaseActivity(R.layout.root_activity) {

    companion object {

        fun start(context: Context) {
            context.startActivity<RootActivity>()
        }
    }

    private val settingsStorage: SettingsStorage by inject()
    private val rootModule by lazy {
        module {
            single<Router> {
                MainRouter(this@RootActivity)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDi()
        setupTheme()
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        val navController = findNavController(R.id.frMainNavHost)
        bnvMain.setupWithNavController(navController)
    }

    private fun setupTheme() {
        val nightModeEnabled = settingsStorage.isNightTheme
        UiUtils.enableNightMode(nightModeEnabled)
    }

    private fun setupDi() {
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(listOf(
                    rootModule,
                    coreModule,
                    myBooksModule,
                    booksStoreModule,
                    settingsModule,
                    readBookModule
            ))
        }
    }
}