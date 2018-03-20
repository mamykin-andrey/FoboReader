package ru.mamykin.foreignbooksreader.presenters

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.util.Log

import com.arellomobile.mvp.InjectViewState
import com.dropbox.core.DbxException
import com.dropbox.core.android.Auth
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.FolderMetadata
import com.dropbox.core.v2.files.ListFolderResult

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

import javax.inject.Inject

import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.common.FolderToFilesListMapper
import ru.mamykin.foreignbooksreader.common.Utils
import ru.mamykin.foreignbooksreader.models.DropboxFile
import ru.mamykin.foreignbooksreader.common.DropboxClientFactory
import ru.mamykin.foreignbooksreader.extension.applySchedulers
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager
import ru.mamykin.foreignbooksreader.preferences.PreferenceNames
import ru.mamykin.foreignbooksreader.views.DropboxView
import rx.Observable
import rx.Subscriber
import rx.Subscription

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
class DropboxBooksPresenter// TODO: изменить фрагмент DROPBOX на 2 фрагмента - авторизация, файлы
(private var currentDir: String?) : BasePresenter<DropboxView>(), PreferenceNames {
    @Inject
    protected var pm: PreferencesManager? = null
    @Inject
    protected var folderToListMapper: FolderToFilesListMapper? = null
    @Inject
    protected var context: Context? = null

    init {
        ReaderApp.component.inject(this)
    }

    override fun attachView(view: DropboxView) {
        super.attachView(view)

        var token = pm!!.getString(PreferenceNames.Companion.DROPBOX_TOKEN_PREF, null!!)
        if (token != null) {
            setupDropbox(token)
        } else if (!pm!!.getBoolean(PreferenceNames.Companion.DROPBOX_LOGOUT_PREF)
                && Auth.getOAuth2Token() != null) {
            pm!!.putString(PreferenceNames.Companion.DROPBOX_TOKEN_PREF, token!!)
            setupDropbox(token)
        } else {
            viewState.hideFiles()
            viewState.showAuth()
        }
    }

    private fun setupDropbox(token: String) {
        DropboxClientFactory.init(token)
        displayFiles()
        loadAccountInfo()
    }

    fun onLoginClicked() {
        pm!!.removeValue(PreferenceNames.DROPBOX_LOGOUT_PREF)
        Auth.startOAuth2Authentication(context!!, context!!.getString(R.string.dropbox_api_key))
    }

    fun onItemClicked(position: Int, item: DropboxFile) {
        if (item.isDirectory) {
            val folder = item.file as FolderMetadata?

            currentDir = folder!!.pathLower
            displayFiles()
        } else {
            viewState.showLoadingItem(position)
            val file = item.file as FileMetadata?

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val dropboxFile = File(downloadsDir, file!!.name)

            if (!downloadsDir.exists() && !downloadsDir.mkdirs() && downloadsDir.isDirectory) {
                Log.d(null, "Error in creating Downloads dir: $downloadsDir")
            }

            val subscription = Observable.create<Void> { subscriber ->
                try {
                    val outputStream = FileOutputStream(dropboxFile)
                    DropboxClientFactory.client
                            .files()
                            .download(file.pathLower, file.rev)
                            .download(outputStream)
                    subscriber.onNext(null)
                } catch (e: DbxException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.applySchedulers()
                    .subscribe {
                        viewState.hideLoadingItem()
                        viewState.openBook(dropboxFile.absolutePath)
                    }
            unsubscribeOnDestroy(subscription)
        }
    }

    fun onUpClicked() {
        currentDir = currentDir!!.substring(0, currentDir!!.lastIndexOf("/"))
        displayFiles()
    }

    private fun displayFiles() {
        viewState.showLoading()
        val subscription = Observable.create<ListFolderResult> { subscriber ->
            try {
                subscriber.onNext(
                        DropboxClientFactory.client.files().listFolder(currentDir!!))
                subscriber.onCompleted()
            } catch (e: DbxException) {
                e.printStackTrace()
            }
        }.applySchedulers()
                .map(folderToListMapper)
                .subscribe(object : Subscriber<List<DropboxFile>>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        viewState.hideFiles()
                        viewState.hideLoading()
                        viewState.showAuth()
                        e.printStackTrace()
                    }

                    override fun onNext(filesList: List<DropboxFile>) {
                        viewState.showUpButton(!TextUtils.isEmpty(currentDir))
                        viewState.hideLoading()
                        viewState.hideAuth()
                        viewState.showFiles(filesList)
                        viewState.showCurrentDir(if (currentDir!!.length == 0) "/" else currentDir!!)
                    }
                })
        unsubscribeOnDestroy(subscription)
    }

    private fun loadAccountInfo() {
        val subscription = Observable.create<String> { subscriber ->
            try {
                subscriber.onNext(DropboxClientFactory.client.users().currentAccount.email)
                subscriber.onCompleted()
            } catch (e: DbxException) {
                e.printStackTrace()
            }
        }.applySchedulers()
                .subscribe(object : Subscriber<String>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(s: String) {
                        pm!!.putString(PreferenceNames.Companion.DROPBOX_EMAIL_PREF, s)
                    }
                })
        unsubscribeOnDestroy(subscription)
    }
}