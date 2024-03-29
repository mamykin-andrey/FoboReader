package ru.mamykin.foboreader.common_book_info.data.database

import androidx.room.*
import ru.mamykin.foboreader.common_book_info.data.model.BookInfoModel

@Dao
interface BookInfoDao {

    @Insert
    suspend fun insertAll(books: List<BookInfoModel>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(book: BookInfoModel): Int

    @Query("DELETE FROM bookinfomodel WHERE id = :bookId")
    suspend fun remove(bookId: Long)

    @Query("SELECT * FROM bookinfomodel")
    suspend fun getBooks(): List<BookInfoModel>

    @Query("SELECT * FROM bookinfomodel WHERE id = :id LIMIT 1")
    suspend fun getBook(id: Long): BookInfoModel
}