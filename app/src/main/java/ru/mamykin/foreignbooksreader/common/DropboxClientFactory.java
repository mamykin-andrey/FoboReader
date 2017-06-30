package ru.mamykin.foreignbooksreader.common;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class DropboxClientFactory {
    private static DbxClientV2 dropboxClient;

    public static void init(String accessToken) {
        if (dropboxClient == null) {
            DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("FoBo Reader")
                    .withHttpRequestor(OkHttp3Requestor.INSTANCE)
                    .build();

            dropboxClient = new DbxClientV2(requestConfig, accessToken);
        }
    }

    public static DbxClientV2 getClient() {
        if (dropboxClient == null) {
            throw new IllegalStateException("Client not initialized.");
        }
        return dropboxClient;
    }
}