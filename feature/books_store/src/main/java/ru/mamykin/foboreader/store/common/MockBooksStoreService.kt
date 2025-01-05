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
        private const val DEAD_MANS_ISLAND_LINK = "https://foboreader.pythonanywhere.com/static/dead_mans_island.fb2" // TODO: Migrate to JSON
        private const val THE_YOUNG_GIANT_LINK = "https://foboreader.pythonanywhere.com/static/the_young_giant.json"

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