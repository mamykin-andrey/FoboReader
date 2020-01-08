package ru.mamykin.my_books.data.database

import androidx.room.*
import ru.mamykin.my_books.data.model.BookInfoModel

@Dao
interface BookInfoDao {
    @Insert
    suspend fun insert(book: BookInfoModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(book: BookInfoModel): Int

    @Delete
    suspend fun delete(book: BookInfoModel)

    @Query("SELECT * FROM bookinfo")
    suspend fun getBooks(): List<BookInfoModel>

    @Query("SELECT * FROM bookinfo WHERE title LIKE :query")
    suspend fun findBooks(query: String): List<BookInfoModel>

    @Query("SELECT * FROM bookinfo WHERE id = :id LIMIT 1")
    suspend fun getBook(id: Long): BookInfoModel?
}