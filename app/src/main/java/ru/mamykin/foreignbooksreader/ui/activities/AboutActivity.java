package ru.mamykin.foreignbooksreader.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.presenters.AboutPresenter;
import ru.mamykin.foreignbooksreader.views.AboutView;

/**
 * Страница с информацией о приложении
 */
public class AboutActivity extends BaseActivity implements AboutView {
    private static final String CHANGELOG_DIALOG_TAG = "changelog_dialog_tag";
    private static final String LIBRARIES_DIALOG_TAG = "libraries_dialog_tag";

    @BindView(R.id.btnAppVersion)
    protected TextView btnAppVersion;
    @BindView(R.id.tvDescription)
    protected TextView tvDescription;

    @InjectPresenter
    AboutPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar(getString(R.string.about_program), true);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void showAppVersion(String version) {
        btnAppVersion.setText(getString(R.string.version) + version);
    }

    @Override
    public void openWebsite(String url) {
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void showFeedbackDialog(String email, String subject, String text) {
        final Intent mailIntent = new Intent(
                Intent.ACTION_SEND, Uri.fromParts("mailto", email, null));
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        mailIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(mailIntent, getString(R.string.send_email_title)));
    }

    @Override
    public void showChangelogDialog(String text) {
        UiUtils.showDialog(this, getString(R.string.changelog_title),
                text, CHANGELOG_DIALOG_TAG, null);
    }

    @Override
    public void showLibrariesDialog() {
        UiUtils.showDialog(this, getString(R.string.libraries_title),
                getString(R.string.libs_text), LIBRARIES_DIALOG_TAG, null);
    }

    @Override
    public void showToast(String text) {
        UiUtils.showToast(this, text);
    }

    @OnClick(R.id.btnAppVersion)
    public void onAppVersionClicked() {
        presenter.onAppVersionClicked();
    }

    @OnClick(R.id.btnGitlab)
    public void onGitlabClicked() {
        presenter.onGitlabClicked();
    }

    @OnClick(R.id.btnFeedback)
    public void onFeedbackClicked() {
        presenter.onFeedbackClicked();
    }

    @OnClick(R.id.btnLibs)
    public void onLibsClicked() {
        presenter.onLibrariesClicked();
    }

    @OnClick(R.id.btnChallenge)
    public void onChallengeClicked() {
        presenter.onChallengeClicked();
    }

    @OnClick(R.id.btnGooglePlus)
    public void onGooglePlusClicked() {
        presenter.onGooglePlusClicked();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }
}