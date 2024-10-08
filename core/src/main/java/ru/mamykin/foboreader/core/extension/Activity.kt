package ru.mamykin.foboreader.core.extension

import android.app.Activity
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.ApiHolderProvider
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.CommonApiProvider
import java.util.*

fun Activity.apiHolder(): ApiHolder {
    return (application as ApiHolderProvider).apiHolder
}

fun Activity.commonApi(): CommonApi {
    return (application as CommonApiProvider).commonApi
}