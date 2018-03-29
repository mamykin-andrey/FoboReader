package ru.mamykin.foboreader.domain.readbook

data class ReadBookState(
        val currentPage: Int,
        val pagesRead: Int,
        val readPercent: Float
)