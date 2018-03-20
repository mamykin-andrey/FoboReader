package ru.mamykin.foreignbooksreader.ui.activities

import android.os.Bundle
import android.provider.Settings
import android.support.annotation.LayoutRes
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager

import com.arellomobile.mvp.MvpAppCompatActivity

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager
import ru.mamykin.foreignbooksreader.common.UiUtils
import ru.mamykin.foreignbooksreader.preferences.PreferenceNames

abstract class BaseActivity : MvpAppCompatActivity(), PreferenceNames {
    @BindView(R.id.toolbar)
    protected var toolbar: Toolbar? = null

    @Inject
    internal var pm: PreferencesManager? = null

    private val systemBrightness: Float
        get() {
            try {
                return Settings.System.getInt(
                        contentResolver, Settings.System.SCREEN_BRIGHTNESS).toFloat()
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }

            return 1f
        }

    @get:LayoutRes
    abstract val layout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layout)
        ButterKnife.bind(this)
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

    fun setupBrightness() {
        val brightness = if (pm!!.getBoolean(PreferenceNames.Companion.BRIGHTNESS_AUTO_PREF, true))
            systemBrightness
        else
            pm!!.getFloat(PreferenceNames.Companion.BRIGHTNESS_PREF, 1f)
        val layoutParams = window.attributes
        layoutParams.screenBrightness = brightness
        window.attributes = layoutParams
    }

    protected fun initToolbar(title: String, homeEnabled: Boolean) {
        setSupportActionBar(toolbar)
        UiUtils.setTitle(this, title)
        UiUtils.setHomeEnabled(this, homeEnabled)
    }
}