package ru.mamykin.foboreader.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.mamykin.foboreader.presentation.main.MainActivity

/**
 * Страница со сплэш-скрином
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.start(this)
        finish()
    }
}