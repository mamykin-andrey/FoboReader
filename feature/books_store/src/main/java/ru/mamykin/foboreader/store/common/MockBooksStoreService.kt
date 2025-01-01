package ru.mamykin.foboreader.store.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.store.categories.BookCategoriesResponse
import ru.mamykin.foboreader.store.list.BookListResponse
import java.io.IOException
import javax.inject.Inject

internal class MockBooksStoreService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {

        private const val RU_LOCALE = "ru"
        private const val EN_LOCALE = "en"
        private const val DEAD_MANS_ISLAND_LINK = "https://foboreader.pythonanywhere.com/static/dead_mans_island.fb2"
        private const val THE_YOUNG_GIANT_LINK = "https://foboreader.pythonanywhere.com/static/the_young_giant.fbwt"

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

        private val ruThrillerBooks = BookListResponse(
            books = listOf(
                BookListResponse.BookResponse(
                    id = "1",
                    genre = "Триллеры",
                    author = "Джон Эскотт",
                    title = "Остров мертвеца",
                    languages = listOf("Английский", "Русский"),
                    format = "fb2",
                    cover = "https://cdn1.ozone.ru/s3/multimedia-0/c650/6000372312.jpg",
                    link = DEAD_MANS_ISLAND_LINK,
                ),
            )
        )

        private val enThrillerBooks = BookListResponse(
            books = listOf(
                BookListResponse.BookResponse(
                    id = "1",
                    genre = "Thriller",
                    author = "John Escot",
                    title = "Dead island",
                    languages = listOf("English", "Russian"),
                    format = "fb2",
                    cover = "https://cdn1.ozone.ru/s3/multimedia-0/c650/6000372312.jpg",
                    link = DEAD_MANS_ISLAND_LINK,
                ),
            )
        )

        private val ruFairytaleBooks = BookListResponse(
            books = listOf(
                BookListResponse.BookResponse(
                    id = "2",
                    genre = "Сказки",
                    author = "Братья Гримм",
                    title = "Юный великан",
                    languages = listOf("Английский", "Русский"),
                    format = "fbwt",
                    cover = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/GrimmsGoblins-171-TheYoungGiantAndTheTailor.jpg/400px-GrimmsGoblins-171-TheYoungGiantAndTheTailor.jpg",
                    link = DEAD_MANS_ISLAND_LINK,
                ),
            )
        )

        private val enFairytaleBooks = BookListResponse(
            books = listOf(
                BookListResponse.BookResponse(
                    id = "2",
                    genre = "Fairytale",
                    author = "Brothers Grimm",
                    title = "The young giant",
                    languages = listOf("English", "Russian"),
                    format = "fbwt",
                    cover = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/GrimmsGoblins-171-TheYoungGiantAndTheTailor.jpg/400px-GrimmsGoblins-171-TheYoungGiantAndTheTailor.jpg",
                    link = THE_YOUNG_GIANT_LINK,
                ),
            )
        )
    }

    suspend fun getBooks(
        locale: String,
        categoryId: String,
        searchQuery: String?,
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

            EN_LOCALE -> {
                when (categoryId) {
                    "1" -> enThrillerBooks
                    "2" -> enFairytaleBooks
                    else -> throw IllegalStateException("Unknown category id: $categoryId!")
                }
            }

            else -> throw IllegalStateException("Unknown locale: $locale")
        }
        return filterBooks(books, searchQuery)
    }

    private fun filterBooks(
        response: BookListResponse,
        searchQuery: String?
    ): BookListResponse {
        if (searchQuery.isNullOrBlank()) {
            return response
        }
        return BookListResponse(
            books = response.books.filter { it.containsText(searchQuery) }
        )
    }

    private fun BookListResponse.BookResponse.containsText(text: String): Boolean {
        return title.contains(text, ignoreCase = true) || author.contains(text, ignoreCase = true)
    }

    suspend fun getCategories(
        locale: String,
    ): BookCategoriesResponse {
        delay(1_000)
        validateInternetConnection(context)
        val categories = when (locale) {
            RU_LOCALE -> ruCategories
            EN_LOCALE -> enCategories
            else -> {
                Log.warning("Unknown locale for store: $locale")
                enCategories
            }
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