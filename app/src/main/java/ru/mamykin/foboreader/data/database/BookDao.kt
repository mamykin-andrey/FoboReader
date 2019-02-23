package ru.mamykin.foboreader.data.database

import android.arch.persistence.room.*
import ru.mamykin.foboreader.domain.entity.FictionBook

@Dao
interface BookDao {

    @Insert
    fun insert(book: FictionBook)

    @Insert
    fun insertAll(books: List<FictionBook>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(data: FictionBook): Int

    @Delete
    fun delete(book: FictionBook)

    @Query("SELECT * FROM fictionbook")
    fun getBooks(): List<FictionBook>

    @Query("SELECT * FROM fictionbook WHERE bookTitle LIKE '%' || :query ORDER BY :sortOrder")
    fun getBooks(query: String, sortOrder: SortOrder): List<FictionBook>

    @Query("SELECT * FROM fictionbook WHERE filePath = :filePath LIMIT 1")
    fun getBook(filePath: String): FictionBook?

    enum class SortOrder {
        BY_NAME,
        BY_READED,
        BY_DATE;
    }
}