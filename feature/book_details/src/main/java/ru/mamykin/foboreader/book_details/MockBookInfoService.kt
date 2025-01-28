package ru.mamykin.foboreader.book_details

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

internal class MockBookInfoService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val bookRatings = mutableMapOf<Long, Int>()
    }

    suspend fun rateBook(bookId: Long, rating: Int): Result<Unit> = runCatching {
        delay(1_000)
        validateInternetConnection(context)
        bookRatings[bookId] = rating
    }

    fun getBookRating(bookId: Long): Int? {
        return bookRatings[bookId]
    }

    @SuppressLint("MissingPermission")
    private fun validateInternetConnection(context: Context) {
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectManager.activeNetworkInfo
        if (networkInfo == null || !networkInfo.isConnected) {
            throw IOException("No internet access!")
        }
    }
}