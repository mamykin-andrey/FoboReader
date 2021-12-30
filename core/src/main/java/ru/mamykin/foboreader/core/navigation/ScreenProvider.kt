package ru.mamykin.foboreader.core.navigation

import com.github.terrakok.cicerone.Screen

interface ScreenProvider {

    fun readBookScreen(bookId: Long): Screen

    fun bookDetailsScreen(bookId: Long): Screen

    fun mainScreen(): Screen
}