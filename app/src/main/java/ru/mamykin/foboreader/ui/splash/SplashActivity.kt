package ru.mamykin.foboreader.ui.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.mamykin.foboreader.ui.main.MainActivity

/**
 * Страница со сплэш-скрином
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(MainActivity.getStartIntent(this))
        this.finish()
    }
}