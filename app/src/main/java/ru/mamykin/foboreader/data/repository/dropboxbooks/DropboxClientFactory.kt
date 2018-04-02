package ru.mamykin.foboreader.data.repository.dropboxbooks

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.http.OkHttp3Requestor
import com.dropbox.core.v2.DbxClientV2
import javax.inject.Inject

class DropboxClientFactory @Inject constructor() {

    private var client: DbxClientV2? = null

    fun getClient(): DbxClientV2 {
        if (client == null) {
            throw IllegalStateException("Client not initialized")
        }
        return client!!
    }

    fun init(accessToken: String) {
        if (client == null) {
            val requestConfig = DbxRequestConfig.newBuilder("FoBo Reader")
                    .withHttpRequestor(OkHttp3Requestor.INSTANCE)
                    .build()

            client = DbxClientV2(requestConfig, accessToken)
        }
    }
}