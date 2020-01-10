package ru.mamykin.store.domain

import androidx.annotation.StringRes

class DownloadBookException(@StringRes val errMsgRes: Int) : RuntimeException()