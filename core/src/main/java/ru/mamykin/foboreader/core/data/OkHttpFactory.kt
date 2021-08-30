package ru.mamykin.foboreader.core.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpFactory {

    fun create(logsEnabled: Boolean): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (logsEnabled) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                })
            }
        }.build()
    }
}