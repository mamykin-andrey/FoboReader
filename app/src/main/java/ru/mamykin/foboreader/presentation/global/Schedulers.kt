package ru.mamykin.foboreader.presentation.global

import rx.Scheduler

interface Schedulers {

    fun io(): Scheduler

    fun main(): Scheduler
}