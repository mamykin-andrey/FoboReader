package ru.mamykin.foboreader.data.database

import androidx.room.*
import ru.mamykin.foboreader.domain.entity.FictionBook

@Dao
interface BookDao {

    @Insert
    suspend fun insert(book: FictionBook)

    @Insert
    suspend fun insertAll(books: List<FictionBook>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(data: FictionBook): Int

    @Delete
    suspend fun delete(book: FictionBook)

    @Query("SELECT * FROM fictionbook")
    suspend fun getBooks(): List<FictionBook>

    @Query("SELECT * FROM fictionbook WHERE bookTitle LIKE '%' || :query ORDER BY :sortOrder")
    suspend fun getBooks(query: String, sortOrder: SortOrder): List<FictionBook>

    @Query("SELECT * FROM fictionbook WHERE filePath = :filePath LIMIT 1")
    suspend fun getBook(filePath: String): FictionBook?

    enum class SortOrder {
        BY_NAME,
        BY_READED,
        BY_DATE;
    }
}