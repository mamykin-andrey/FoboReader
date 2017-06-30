package ru.mamykin.foreignbooksreader.presenters;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.common.Utils;
import ru.mamykin.foreignbooksreader.models.UpdateInfo;
import ru.mamykin.foreignbooksreader.retrofit.UpdateService;
import ru.mamykin.foreignbooksreader.views.AboutView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
public class AboutPresenter extends BasePresenter<AboutView> {
    @Inject
    protected Context context;
    @Inject
    protected UpdateService updateService;

    public AboutPresenter() {
        ReaderApp.getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        loadAppInfo();
    }

    private void loadAppInfo() {
        getViewState().showAppVersion(getAppVersion());
    }

    private String getAppVersion() {
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onAppVersionClicked() {
        Subscription subscription = updateService.getChangelog()
                .compose(Utils.applySchedulers())
                .subscribe(new Subscriber<UpdateInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getViewState().showToast(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(UpdateInfo updateInfo) {
                        getViewState().showChangelogDialog(updateInfo.getChangelog());
                    }
                });
        unsubscribeOnDestroy(subscription);
    }

    public void onGitlabClicked() {
        getViewState().openWebsite(context.getString(R.string.gitlab_profile));
    }

    public void onFeedbackClicked() {
        getViewState().showFeedbackDialog(
                context.getString(R.string.mail), context.getString(R.string.feedback_hint), null);
    }

    public void onLibrariesClicked() {
        getViewState().showLibrariesDialog();
    }

    public void onChallengeClicked() {
        getViewState().openWebsite(context.getString(R.string.challenge_link));
    }

    public void onGooglePlusClicked() {
        getViewState().openWebsite(context.getString(R.string.google_profile));
    }
}