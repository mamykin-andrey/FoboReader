package ru.mamykin.foboreader.app.presentation.tabs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TabsViewModel : ViewModel() {

    val selectedItemIdData = MutableLiveData<Int>()
}