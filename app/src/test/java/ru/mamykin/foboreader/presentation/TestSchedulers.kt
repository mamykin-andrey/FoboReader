package ru.mamykin.foboreader.presentation

import ru.mamykin.foboreader.presentation.global.Schedulers
import rx.Scheduler

class TestSchedulers : Schedulers {

    override fun io(): Scheduler = rx.schedulers.Schedulers.trampoline()

    override fun main(): Scheduler = rx.schedulers.Schedulers.trampoline()
}