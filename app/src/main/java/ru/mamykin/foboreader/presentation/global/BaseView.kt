package ru.mamykin.foboreader.presentation.global

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

    fun onError(message: String)
}