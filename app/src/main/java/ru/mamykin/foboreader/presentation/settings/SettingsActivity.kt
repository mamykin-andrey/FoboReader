package ru.mamykin.foboreader.presentation.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_settings.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.ui.BaseActivity

/**
 * Страница настроек
 */
class SettingsActivity : BaseActivity(), SettingsView, SeekBar.OnSeekBarChangeListener {

    companion object {

        fun start(context: Context) = context.startActivity(
                Intent(context, SettingsActivity::class.java)
        )
    }

    override val layout: Int = R.layout.activity_settings

    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    @ProvidePresenter
    fun providePresenter(): SettingsPresenter = getAppComponent()
            .getSettingsComponent()
            .getSettingsPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.settings), true)
        initClickListeners()
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        presenter.onBrightnessProgressChanged(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // Do nothing
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // Do nothing
    }

    override fun setupBrightness() {

    }

    override fun showContentSizeText(size: Int) {
        tvTextSize.text = size.toString()
    }

    override fun showBrightnessPos(position: Int) {
        seekbarBright.progress = position
    }

    override fun showAutoBrightnessEnabled(enabled: Boolean) {
        switchBrightAuto.isChecked = enabled
    }

    override fun showBrightnessControlEnabled(enabled: Boolean) {
        seekbarBright.isEnabled = enabled
    }

    override fun showNightThemeEnabled(enabled: Boolean) {
        switchNightTheme.isChecked = enabled
    }

    override fun restartActivity() {
        recreate()
    }

    private fun initClickListeners() {
        seekbarBright.setOnSeekBarChangeListener(this)
        switchNightTheme.setOnCheckedChangeListener { _, c -> presenter.onNightThemeEnabled(c) }
        switchBrightAuto.setOnCheckedChangeListener { _, c -> presenter.onAutoBrightnessEnabled(c) }
    }
}