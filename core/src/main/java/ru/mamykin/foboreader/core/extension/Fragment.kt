package ru.mamykin.foboreader.core.extension

import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.ApiHolderProvider

fun Fragment.apiHolder(): ApiHolder {
    return (requireActivity().application as ApiHolderProvider).apiHolder
}