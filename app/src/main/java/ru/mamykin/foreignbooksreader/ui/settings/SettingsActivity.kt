package ru.mamykin.foreignbooksreader.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.AppCompatSeekBar
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView

import com.arellomobile.mvp.presenter.InjectPresenter

import butterknife.BindView
import butterknife.OnCheckedChanged
import butterknife.OnClick
import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.common.UiUtils
import ru.mamykin.foreignbooksreader.presentation.settings.SettingsPresenter
import ru.mamykin.foreignbooksreader.ui.global.YesNoDialogFragment
import ru.mamykin.foreignbooksreader.presentation.settings.SettingsView
import ru.mamykin.foreignbooksreader.ui.global.BaseActivity

/**
 * Страница настроек
 */
class SettingsActivity(override val layout: Int = R.layout.activity_settings) : BaseActivity(), SeekBar.OnSeekBarChangeListener, SettingsView {

    @BindView(R.id.switchBrightAuto)
    protected var switchBrightAuto: SwitchCompat? = null
    @BindView(R.id.switchNightTheme)
    protected var switchNightTheme: SwitchCompat? = null
    @BindView(R.id.seekbarBright)
    protected var seekbarBright: AppCompatSeekBar? = null
    @BindView(R.id.btnTranslateColor)
    protected var btnTranslateColor: View? = null
    @BindView(R.id.ivTranslateColor)
    protected var ivTranslateColor: ImageView? = null
    @BindView(R.id.btnDropboxLogout)
    protected var btnDropboxLogout: View? = null
    @BindView(R.id.btnTextSizeMinus)
    protected var btnTextSizeMinus: View? = null
    @BindView(R.id.btnTextSizePlus)
    protected var btnTextSizePlus: View? = null
    @BindView(R.id.tvTextSize)
    protected var tvTextSize: TextView? = null
    @BindView(R.id.tvDropboxAccount)
    protected var tvDropboxAccount: TextView? = null

    @InjectPresenter
    internal var presenter: SettingsPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.settings), true)
        seekbarBright!!.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        presenter!!.onBrightnessProgressChanged(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {

    }

    override fun setDropboxAccount(account: String) {
        tvDropboxAccount!!.text = account
    }

    override fun showDropboxLogoutDialog() {
        UiUtils.showDialog(this, R.string.dropbox_logout_title, R.string.dropbox_logout_message,
                DROPBOX_DIALOG_TAG, object : YesNoDialogFragment.PositiveClickListener{
            override fun onPositiveClick() {
                presenter!!.onDropboxLogoutPositive()
            }
        }, null)
    }

    override fun setContentSizeText(text: String) {
        tvTextSize!!.text = text
    }

    override fun setBrightnessPos(progress: Int) {
        seekbarBright!!.progress = progress
    }

    override fun setAutoBrightnessChecked(enabled: Boolean) {
        switchBrightAuto!!.isChecked = enabled
    }

    override fun setBrightnessControlEnabled(enabled: Boolean) {
        seekbarBright!!.isEnabled = enabled
    }

    override fun setNightThemeEnabled(enabled: Boolean) {
        switchNightTheme!!.isChecked = enabled
    }

    override fun setTitle(title: String) {
        UiUtils.setTitle(this, title)
    }

    override fun setHomeAsUpEnabled(enabled: Boolean) {
        UiUtils.setHomeEnabled(this, enabled)
    }

    override fun restartActivity() {
        UiUtils.restartActivity(this)
    }

    @OnCheckedChanged(R.id.switchNightTheme)
    fun onNightThemeCheckedChanged(isChecked: Boolean) {
        presenter!!.onNightThemeCheckedChanged(isChecked)
    }

    @OnCheckedChanged(R.id.switchBrightAuto)
    fun onBrightnessAutoCheckedChanged(isChecked: Boolean) {
        presenter!!.onBrightnessAutoCheckedChanged(isChecked)
    }

    @OnClick(R.id.btnDropboxLogout)
    fun onDropboxLogoutClicked() {
        presenter!!.onDropboxLogoutClick()
    }

    companion object {
        val DROPBOX_DIALOG_TAG = "dropbox_dialog_tag"

        fun getStartIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}