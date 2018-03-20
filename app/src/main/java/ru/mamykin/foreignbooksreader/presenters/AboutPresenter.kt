package ru.mamykin.foreignbooksreader.presenters

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

import com.arellomobile.mvp.InjectViewState

import javax.inject.Inject

import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.common.Utils
import ru.mamykin.foreignbooksreader.models.UpdateInfo
import ru.mamykin.foreignbooksreader.retrofit.UpdateService
import ru.mamykin.foreignbooksreader.views.AboutView
import rx.Subscriber
import rx.Subscription

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
class AboutPresenter : BasePresenter<AboutView>() {
    @Inject
    protected var context: Context? = null
    @Inject
    protected var updateService: UpdateService? = null

    private val appVersion: String?
        get() {
            try {
                val packageInfo = context!!.packageManager.getPackageInfo(context!!.packageName, 0)
                return packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return null
        }

    init {
        ReaderApp.component.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadAppInfo()
    }

    private fun loadAppInfo() {
        viewState.showAppVersion(appVersion)
    }

    fun onAppVersionClicked() {
        val subscription = updateService!!.changelog
                .compose(Utils.applySchedulers())
                .subscribe(object : Subscriber<UpdateInfo>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        viewState.showToast(e.localizedMessage)
                    }

                    override fun onNext(updateInfo: UpdateInfo) {
                        viewState.showChangelogDialog(updateInfo.changelog)
                    }
                })
        unsubscribeOnDestroy(subscription)
    }

    fun onGitlabClicked() {
        viewState.openWebsite(context!!.getString(R.string.gitlab_profile))
    }

    fun onFeedbackClicked() {
        viewState.showFeedbackDialog(
                context!!.getString(R.string.mail), context!!.getString(R.string.feedback_hint), null)
    }

    fun onLibrariesClicked() {
        viewState.showLibrariesDialog()
    }

    fun onChallengeClicked() {
        viewState.openWebsite(context!!.getString(R.string.challenge_link))
    }

    fun onGooglePlusClicked() {
        viewState.openWebsite(context!!.getString(R.string.google_profile))
    }
}