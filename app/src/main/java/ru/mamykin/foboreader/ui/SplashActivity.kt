package ru.mamykin.foboreader.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.mamykin.foboreader.ui.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.start(this)
        finish()
    }
}