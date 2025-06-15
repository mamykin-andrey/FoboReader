package ru.mamykin.foboreader.store.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import ru.mamykin.foboreader.store.categories.BookCategoriesResponse
import ru.mamykin.foboreader.store.list.BookListResponse
import ru.mamykin.foboreader.store.search.SearchResultsResponse
import java.io.IOException
import javax.inject.Inject

internal class MockBooksStoreService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {

        private const val RU_LOCALE = "ru"
        private const val KLIMOP_DRIE_VERHALEN_LINK = "https://foboreader.pythonanywhere.com/static/klimop_drie_verhalen.json"
        private const val GOOSE_GIRL_LINK = "https://foboreader.pythonanywhere.com/static/grimm_fairy_tales_goose_girl.json"

        private val ruCategories = listOf(
            BookCategoriesResponse.BookCategoryResponse(
                id = "1",
                name = "Фантастика",
                description = null,
                booksCount = 1,
            ),
            BookCategoriesResponse.BookCategoryResponse(
                id = "2",
                name = "Сказки",
                description = null,
                booksCount = 1
            ),
        )

        private val enCategories = listOf(
            BookCategoriesResponse.BookCategoryResponse(
                id = "1",
                name = "Thriller",
                description = null,
                booksCount = 1,
            ),
            BookCategoriesResponse.BookCategoryResponse(
                id = "2",
                name = "Fairy tales",
                description = null,
                booksCount = 1,
            ),
        )

        private val ruThrillerBooks = BookListResponse(books = emptyList())
        private val enThrillerBooks = BookListResponse(books = emptyList())

        private val ruFairytaleBooks = BookListResponse(
            books = listOf(
                BookListResponse.BookResponse(
                    id = "1",
                    genre = "Сказки",
                    author = "Сюзи Андерсен",
                    title = "Три сказки для мальчиков и девочек",
                    languages = listOf("Голландский", "Английский", "Русский"),
                    format = "fbwt",
                    cover = "https://www.gutenberg.org/cache/epub/59353/pg59353.cover.medium.jpg",
                    link = KLIMOP_DRIE_VERHALEN_LINK,
                    rating = 4.6f,
                ),
                BookListResponse.BookResponse(
                    id = "2",
                    genre = "Сказки",
                    author = "Братья Гримм",
                    title = "Принцесса-гусыня",
                    languages = listOf("Голландский", "Английский", "Русский"),
                    format = "fbwt",
                    cover = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1637098809i/547826.jpg",
                    link = GOOSE_GIRL_LINK,
                    rating = 4.8f,
                ),
            )
        )

        private val enFairytaleBooks = BookListResponse(
            books = listOf(
                BookListResponse.BookResponse(
                    id = "1",
                    genre = "Fairytale",
                    author = "Suze Andriessen",
                    title = "Drie verhalen voor jongens en meisjes",
                    languages = listOf("Dutch", "English"),
                    format = "fbwt",
                    cover = "https://www.gutenberg.org/cache/epub/59353/pg59353.cover.medium.jpg",
                    link = KLIMOP_DRIE_VERHALEN_LINK,
                    rating = 4.6f,
                ),
                BookListResponse.BookResponse(
                    id = "2",
                    genre = "Fairytale",
                    author = "The Grimm brothers",
                    title = "The goose girl",
                    languages = listOf("Dutch", "English", "Russian"),
                    format = "fbwt",
                    cover = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1637098809i/547826.jpg",
                    link = GOOSE_GIRL_LINK,
                    rating = 4.8f,
                ),
            )
        )
    }

    suspend fun getStoreBooks(
        locale: String,
        categoryId: String,
    ): BookListResponse {
        delay(1_000)
        validateInternetConnection(context)
        val books = when (locale) {
            RU_LOCALE -> {
                when (categoryId) {
                    "1" -> ruThrillerBooks
                    "2" -> ruFairytaleBooks
                    else -> throw IllegalStateException("Unknown category id: $categoryId!")
                }
            }

            else -> {
                when (categoryId) {
                    "1" -> enThrillerBooks
                    "2" -> enFairytaleBooks
                    else -> throw IllegalStateException("Unknown category id: $categoryId!")
                }
            }
        }
        return books
    }

    suspend fun searchInStore(
        locale: String,
        searchQuery: String,
    ): SearchResultsResponse {
        delay(1_000)
        validateInternetConnection(context)
        val allBooks = when (locale) {
            RU_LOCALE -> ruThrillerBooks.books + ruFairytaleBooks.books
            else -> enThrillerBooks.books + enFairytaleBooks.books
        }
        val allCategories = when (locale) {
            RU_LOCALE -> ruCategories
            else -> enCategories
        }
        val categories = allCategories.filter {
            it.name.contains(searchQuery, ignoreCase = true) || it.description?.contains(
                searchQuery,
                ignoreCase = true
            ) == true
        }
        val books = allBooks.filter {
            it.title.contains(searchQuery, ignoreCase = true) || it.author.contains(searchQuery, ignoreCase = true)
        }
        return SearchResultsResponse(
            categories = categories,
            books = books,
        )
    }

    suspend fun getStoreCategories(
        locale: String,
    ): BookCategoriesResponse {
        delay(1_000)
        validateInternetConnection(context)
        val categories = when (locale) {
            RU_LOCALE -> ruCategories
            else -> enCategories
        }
        return BookCategoriesResponse(categories)
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