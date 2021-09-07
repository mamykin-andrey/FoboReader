package ru.mamykin.foboreader.core.di.api

import okhttp3.OkHttpClient
import ru.mamykin.foboreader.core.di.qualifier.CommonClient

interface NetworkApi {

    @CommonClient
    fun okHttpClient(): OkHttpClient
}