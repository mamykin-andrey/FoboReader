package ru.mamykin.foboreader.presentation.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.mamykin.foboreader.presentation.main.MainActivity

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