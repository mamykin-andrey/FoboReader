package ru.mamykin.foboreader.extension

import android.view.View

var View.isVisible: Boolean
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }
    get() {
        return this.visibility == View.VISIBLE
    }