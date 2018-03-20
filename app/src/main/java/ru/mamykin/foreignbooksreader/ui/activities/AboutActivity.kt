package ru.mamykin.foreignbooksreader.ui.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView

import com.arellomobile.mvp.presenter.InjectPresenter

import butterknife.BindView
import butterknife.OnClick
import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.common.UiUtils
import ru.mamykin.foreignbooksreader.presenters.AboutPresenter
import ru.mamykin.foreignbooksreader.views.AboutView

/**
 * Страница с информацией о приложении
 */
class AboutActivity : BaseActivity(), AboutView {

    @BindView(R.id.btnAppVersion)
    protected var btnAppVersion: TextView? = null
    @BindView(R.id.tvDescription)
    protected var tvDescription: TextView? = null

    @InjectPresenter
    internal var presenter: AboutPresenter? = null

    override val layout: Int
        get() = R.layout.activity_about

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.about_program), true)
    }

    override fun showAppVersion(version: String) {
        btnAppVersion!!.text = getString(R.string.version) + version
    }

    override fun openWebsite(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun showFeedbackDialog(email: String, subject: String, text: String) {
        val mailIntent = Intent(
                Intent.ACTION_SEND, Uri.fromParts("mailto", email, null))
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mailIntent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(mailIntent, getString(R.string.send_email_title)))
    }

    override fun showChangelogDialog(text: String) {
        UiUtils.showDialog(this, getString(R.string.changelog_title),
                text, CHANGELOG_DIALOG_TAG, null)
    }

    override fun showLibrariesDialog() {
        UiUtils.showDialog(this, getString(R.string.libraries_title),
                getString(R.string.libs_text), LIBRARIES_DIALOG_TAG, null)
    }

    override fun showToast(text: String) {
        UiUtils.showToast(this, text)
    }

    @OnClick(R.id.btnAppVersion)
    fun onAppVersionClicked() {
        presenter!!.onAppVersionClicked()
    }

    @OnClick(R.id.btnGitlab)
    fun onGitlabClicked() {
        presenter!!.onGitlabClicked()
    }

    @OnClick(R.id.btnFeedback)
    fun onFeedbackClicked() {
        presenter!!.onFeedbackClicked()
    }

    @OnClick(R.id.btnLibs)
    fun onLibsClicked() {
        presenter!!.onLibrariesClicked()
    }

    @OnClick(R.id.btnChallenge)
    fun onChallengeClicked() {
        presenter!!.onChallengeClicked()
    }

    @OnClick(R.id.btnGooglePlus)
    fun onGooglePlusClicked() {
        presenter!!.onGooglePlusClicked()
    }

    companion object {
        private val CHANGELOG_DIALOG_TAG = "changelog_dialog_tag"
        private val LIBRARIES_DIALOG_TAG = "libraries_dialog_tag"

        fun getStartIntent(context: Context): Intent {
            return Intent(context, AboutActivity::class.java)
        }
    }
}