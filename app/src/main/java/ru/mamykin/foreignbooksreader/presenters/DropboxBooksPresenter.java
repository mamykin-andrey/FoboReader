package ru.mamykin.foreignbooksreader.presenters;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.dropbox.core.DbxException;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.common.FolderToFilesListMapper;
import ru.mamykin.foreignbooksreader.common.Utils;
import ru.mamykin.foreignbooksreader.models.DropboxFile;
import ru.mamykin.foreignbooksreader.common.DropboxClientFactory;
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager;
import ru.mamykin.foreignbooksreader.preferences.PreferenceNames;
import ru.mamykin.foreignbooksreader.views.DropboxView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
public class DropboxBooksPresenter extends BasePresenter<DropboxView> implements PreferenceNames {
    @Inject
    protected PreferencesManager pm;
    @Inject
    protected FolderToFilesListMapper folderToListMapper;
    @Inject
    protected Context context;
    private String currentDir;

    // TODO: изменить фрагмент DROPBOX на 2 фрагмента - авторизация, файлы
    public DropboxBooksPresenter(String currentDir) {
        this.currentDir = currentDir;
        ReaderApp.getComponent().inject(this);
    }

    @Override
    public void attachView(DropboxView view) {
        super.attachView(view);

        String token = pm.getString(DROPBOX_TOKEN_PREF, null);
        if (token != null) {
            setupDropbox(token);
        } else if (!pm.getBoolean(DROPBOX_LOGOUT_PREF) && (token = Auth.getOAuth2Token()) != null) {
            pm.putString(DROPBOX_TOKEN_PREF, token);
            setupDropbox(token);
        } else {
            getViewState().hideFiles();
            getViewState().showAuth();
        }
    }

    private void setupDropbox(String token) {
        DropboxClientFactory.INSTANCE.init(token);
        displayFiles();
        loadAccountInfo();
    }

    public void onLoginClicked() {
        pm.removeValue(PreferenceNames.DROPBOX_LOGOUT_PREF);
        Auth.startOAuth2Authentication(context, context.getString(R.string.dropbox_api_key));
    }

    public void onItemClicked(int position, DropboxFile item) {
        if (item.isDirectory()) {
            FolderMetadata folder = (FolderMetadata) item.getFile();

            currentDir = folder.getPathLower();
            displayFiles();
        } else {
            getViewState().showLoadingItem(position);
            FileMetadata file = (FileMetadata) item.getFile();

            final File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            final File dropboxFile = new File(downloadsDir, file.getName());

            if (!downloadsDir.exists() && !downloadsDir.mkdirs() && downloadsDir.isDirectory()) {
                Log.d(null, "Error in creating Downloads dir: " + downloadsDir);
            }

            Subscription subscription = Observable.<Void>create(subscriber -> {
                try {
                    OutputStream outputStream = new FileOutputStream(dropboxFile);
                    DropboxClientFactory.INSTANCE.getClient()
                            .files()
                            .download(file.getPathLower(), file.getRev())
                            .download(outputStream);
                    subscriber.onNext(null);
                } catch (DbxException | IOException e) {
                    e.printStackTrace();
                }
            })
                    .compose(Utils.INSTANCE.applySchedulers())
                    .subscribe(aVoid -> {
                        getViewState().hideLoadingItem();
                        getViewState().openBook(dropboxFile.getAbsolutePath());
                    });
            unsubscribeOnDestroy(subscription);
        }
    }

    public void onUpClicked() {
        currentDir = currentDir.substring(0, currentDir.lastIndexOf("/"));
        displayFiles();
    }

    private void displayFiles() {
        getViewState().showLoading();
        Subscription subscription = Observable.<ListFolderResult>create(
                subscriber -> {
                    try {
                        subscriber.onNext(
                                DropboxClientFactory.INSTANCE.getClient().files().listFolder(currentDir));
                        subscriber.onCompleted();
                    } catch (DbxException e) {
                        e.printStackTrace();
                    }
                })
                .compose(Utils.INSTANCE.applySchedulers())
                .map(folderToListMapper)
                .subscribe(new Subscriber<List<DropboxFile>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().hideFiles();
                        getViewState().hideLoading();
                        getViewState().showAuth();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<DropboxFile> filesList) {
                        getViewState().showUpButton(!TextUtils.isEmpty(currentDir));
                        getViewState().hideLoading();
                        getViewState().hideAuth();
                        getViewState().showFiles(filesList);
                        getViewState().showCurrentDir(currentDir.length() == 0 ? "/" : currentDir);
                    }
                });
        unsubscribeOnDestroy(subscription);
    }

    private void loadAccountInfo() {
        Subscription subscription = Observable.<String>create(
                subscriber -> {
                    try {
                        subscriber.onNext(DropboxClientFactory.INSTANCE.getClient().users().getCurrentAccount().getEmail());
                        subscriber.onCompleted();
                    } catch (DbxException e) {
                        e.printStackTrace();
                    }
                })
                .compose(Utils.INSTANCE.applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        pm.putString(DROPBOX_EMAIL_PREF, s);
                    }
                });
        unsubscribeOnDestroy(subscription);
    }
}