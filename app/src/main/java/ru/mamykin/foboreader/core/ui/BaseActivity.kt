package ru.mamykin.foboreader.core.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.core.di.component.AppComponent

abstract class BaseActivity : AndroidXMvpActivity(), BaseView {

    abstract val layout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)

        setContentView(layout)
        //ReaderApp.appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        //setupBrightness()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onError(message: String) {
        showSnackbar(message, false)
    }

    override fun showLoading(show: Boolean) {

    }

    protected fun initToolbar(title: String, homeEnabled: Boolean) {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(homeEnabled)
    }

//    private fun setupBrightness() {
//        val autoBrightnessEnabled = pm.getBoolean(PreferenceNames.BRIGHTNESS_AUTO_PREF)
//        if (autoBrightnessEnabled) {
//            setScreenBrightness(getSystemBrightness())
//        } else {
//            val userBrightness = pm.getFloat(PreferenceNames.BRIGHTNESS_PREF, 1f)
//            setScreenBrightness(userBrightness)
//        }
//    }
//
//    private fun setScreenBrightness(brightness: Float) {
//        val layoutParams = window.attributes
//        layoutParams.screenBrightness = brightness
//        window.attributes = layoutParams
//    }
//
//    private fun getSystemBrightness(): Float {
//        return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS).toFloat()
//    }

    protected open fun injectDependencies() {
    }

    protected fun getAppComponent(): AppComponent = (application as ReaderApp).appComponent

    protected fun showToast(@StringRes messageResId: Int, long: Boolean = false) {
        showToast(getString(messageResId, long))
    }

    protected fun showToast(message: String, long: Boolean = false) {
        val duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(this, message, duration).show()
    }

    protected fun showSnackbar(@StringRes messageResId: Int, long: Boolean = false) {
        showSnackbar(getString(messageResId, long))
    }

    protected fun showSnackbar(message: String, long: Boolean = false) {
        val duration = if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
        findViewById<View>(android.R.id.content)?.let {
            Snackbar.make(it, message, duration)
        }
    }
}