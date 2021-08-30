package ru.mamykin.foboreader.core.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by terrakok 27.11.16
 */
@Singleton
class LocalCiceroneHolder @Inject constructor() {

    private val containers = HashMap<String, Cicerone<Router>>()

    fun getCicerone(containerTag: String): Cicerone<Router> = containers.getOrPut(containerTag) { Cicerone.create() }
}