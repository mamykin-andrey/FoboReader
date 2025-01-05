package ru.mamykin.foboreader.common_book_info.data.database

import androidx.room.*
import ru.mamykin.foboreader.common_book_info.data.model.DownloadedBookDBModel

@Dao
interface DownloadedBooksDao {

    @Insert
    suspend fun insert(book: DownloadedBookDBModel)

    @Query("UPDATE downloadedbookdbmodel SET currentPage = :currentPage, totalPages = :totalPages WHERE id = :bookId")
    suspend fun update(bookId: Long, currentPage: Int, totalPages: Int): Int

    @Query("DELETE FROM downloadedbookdbmodel WHERE id = :bookId")
    suspend fun remove(bookId: Long)

    @Query("SELECT * FROM downloadedbookdbmodel")
    suspend fun getBooks(): List<DownloadedBookDBModel>

    @Query("SELECT * FROM downloadedbookdbmodel WHERE id = :id LIMIT 1")
    suspend fun getBook(id: Long): DownloadedBookDBModel
}