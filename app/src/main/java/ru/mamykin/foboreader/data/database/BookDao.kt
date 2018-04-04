package ru.mamykin.foboreader.data.database

import android.arch.persistence.room.*
import ru.mamykin.foboreader.entity.FictionBook

@Dao
interface BookDao {

    @Insert
    fun insertAll(books: List<FictionBook>)

    @Insert
    fun insert(book: FictionBook)

    @Delete
    fun delete(book: FictionBook)

    @Query("SELECT * FROM fictionbook")
    fun getBooks(): List<FictionBook>

    @Query("SELECT * FROM fictionbook WHERE id = :id LIMIT 1")
    fun getBook(id: Int): FictionBook?

    @Query("SELECT * FROM fictionbook WHERE filePath = :filePath LIMIT 1")
    fun getBook(filePath: String): FictionBook?

    @Update
    fun update(data: FictionBook): Int

    enum class SortOrder {
        BY_NAME,
        BY_READED,
        BY_DATE
    }
}