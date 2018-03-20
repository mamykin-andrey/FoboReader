package ru.mamykin.foreignbooksreader.common

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.http.OkHttp3Requestor
import com.dropbox.core.v2.DbxClientV2

object DropboxClientFactory {
    private var dropboxClient: DbxClientV2? = null

    val client: DbxClientV2
        get() {
            if (dropboxClient == null) {
                throw IllegalStateException("Client not initialized.")
            }
            return dropboxClient
        }

    fun init(accessToken: String) {
        if (dropboxClient == null) {
            val requestConfig = DbxRequestConfig.newBuilder("FoBo Reader")
                    .withHttpRequestor(OkHttp3Requestor.INSTANCE)
                    .build()

            dropboxClient = DbxClientV2(requestConfig, accessToken)
        }
    }
}