package ru.mamykin.foboreader.domain.readbook

data class ReadBookState(
        val currentPage: Int,
        val pagesCount: Int,
        val currentPageText: CharSequence,
        val readPercent: Float
)