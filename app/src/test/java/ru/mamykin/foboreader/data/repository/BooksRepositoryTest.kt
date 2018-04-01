package ru.mamykin.foboreader.data.repository

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.mamykin.foboreader.data.repository.books.BooksRepository

class BooksRepositoryTest {

    private lateinit var repository: BooksRepository

    @Before
    fun setUp(){
        //repository = BooksRepository()
    }

    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }
}