package ru.mamykin.foboreader.core.extension

import android.app.Activity
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.ApiHolderProvider

fun Activity.apiHolder(): ApiHolder {
    return (application as ApiHolderProvider).apiHolder
}