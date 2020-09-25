package ru.mamykin.foboreader.core.platform

import androidx.navigation.NavController

interface Navigator {

    fun bind(controller: NavController)

    fun unbind()
}