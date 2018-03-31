package ru.mamykin.foboreader.ui.global

import android.os.Bundle
import android.provider.Settings
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.data.storage.PreferenceNames
import ru.mamykin.foboreader.data.storage.PreferencesManager
import javax.inject.Inject

abstract class BaseActivity : MvpAppCompatActivity() {

    @Inject
    lateinit var pm: PreferencesManager

    abstract val layout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layout)
        ReaderApp.component.inject(this)
    }

    override fun onResume() {
        super.onResume()
        setupBrightness()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun initToolbar(title: String, homeEnabled: Boolean) {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(homeEnabled)
    }

    private fun setupBrightness() {
        val autoBrightnessEnabled = pm.getBoolean(PreferenceNames.BRIGHTNESS_AUTO_PREF)
        if (autoBrightnessEnabled) {
            setScreenBrightness(getSystemBrightness())
        } else {
            val userBrightness = pm.getFloat(PreferenceNames.BRIGHTNESS_PREF, 1f)
            setScreenBrightness(userBrightness)
        }
    }

    private fun setScreenBrightness(brightness: Float) {
        val layoutParams = window.attributes
        layoutParams.screenBrightness = brightness
        window.attributes = layoutParams
    }

    private fun getSystemBrightness(): Float {
        return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS).toFloat()
    }
}