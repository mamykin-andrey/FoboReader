package ru.mamykin.foboreader.core.ui

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

    fun onError(message: String)
}