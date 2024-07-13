package ru.mamykin.foboreader.core.extension

import android.view.LayoutInflater
import android.view.ViewGroup

fun ViewGroup.getLayoutInflater(): LayoutInflater = LayoutInflater.from(context)