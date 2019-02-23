package ru.mamykin.foboreader.core.platform

import rx.Scheduler

interface Schedulers {

    fun io(): Scheduler

    fun main(): Scheduler
}