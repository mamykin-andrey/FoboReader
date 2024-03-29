package ru.mamykin.foboreader.store.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.delay
import ru.mamykin.foboreader.store.data.model.BookCategoriesResponse
import ru.mamykin.foboreader.store.data.model.BookListResponse
import java.io.IOException
import javax.inject.Inject

internal class TestBooksStoreService @Inject constructor(
    private val context: Context
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
                booksCount = 2
            ),
            BookCategoriesResponse.BookCategoryResponse(
                id = "2",
                name = "Сказки",
                description = null,
                booksCount = 1
            ),
            BookCategoriesResponse.BookCategoryResponse(
                id = "3",
                name = "Приключения",
                description = null,
                booksCount = 1
            ),
        )

        private val enCategories = listOf(
            BookCategoriesResponse.BookCategoryResponse(
                id = "1",
                name = "Fantastic",
                description = null,
                booksCount = 2
            ),
            BookCategoriesResponse.BookCategoryResponse(
                id = "2",
                name = "Fairytale",
                description = null,
                booksCount = 1
            ),
            BookCategoriesResponse.BookCategoryResponse(
                id = "3",
                name = "Adventure",
                description = null,
                booksCount = 1
            ),
        )

        private val ruFantasticBooks = BookListResponse(
            books = listOf(
                BookListResponse.BookResponse(
                    id = "0",
                    genre = "Фантастика",
                    author = "Герберт Джордж Уэллс",
                    title = "Человек-невидимка",
                    lang = "Английский/Русский",
                    format = "fbwt",
                    cover = "https://m.media-amazon.com/images/I/41urypNXYyL.jpg",
                    link = DEAD_MANS_ISLAND_LINK,
                ),
                BookListResponse.BookResponse(
                    id = "1",
                    genre = "Фантастика",
                    author = "Джон Эскотт",
                    title = "Остров мертвеца",
                    lang = "Английский",
                    format = "fb2",
                    cover = "https://cdn1.ozone.ru/s3/multimedia-0/c650/6000372312.jpg",
                    link = THE_YOUNG_GIANT_LINK
                ),
            )
        )

        private val enFantasticBooks = BookListResponse(
            books = listOf(
                BookListResponse.BookResponse(
                    id = "0",
                    genre = "Fantastic",
                    author = "Herbert George Wells",
                    title = "The invisible man",
                    lang = "English/Russian",
                    format = "fbwt",
                    cover = "https://m.media-amazon.com/images/I/41urypNXYyL.jpg",
                    link = DEAD_MANS_ISLAND_LINK,
                ),
                BookListResponse.BookResponse(
                    id = "1",
                    genre = "Fantastic",
                    author = "John Escot",
                    title = "Dead island",
                    lang = "English/Russian",
                    format = "fb2",
                    cover = "https://cdn1.ozone.ru/s3/multimedia-0/c650/6000372312.jpg",
                    link = THE_YOUNG_GIANT_LINK
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
                    lang = "Английский/Русский",
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
                    lang = "English/Russian",
                    format = "fbwt",
                    cover = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/GrimmsGoblins-171-TheYoungGiantAndTheTailor.jpg/400px-GrimmsGoblins-171-TheYoungGiantAndTheTailor.jpg",
                    link = DEAD_MANS_ISLAND_LINK,
                ),
            )
        )

        private val ruAdventureBooks = BookListResponse(
            books = listOf(
                BookListResponse.BookResponse(
                    id = "3",
                    genre = "Приключения",
                    author = "Агата Кристи",
                    title = "Убийство в Месопотамии",
                    lang = "Английский/Русский",
                    format = "fbwt",
                    cover = "https://s1.livelib.ru/boocover/1002005641/o/fc01/Agatha_Christie__Murder_in_Mesopotamia.jpeg",
                    link = DEAD_MANS_ISLAND_LINK,
                ),
            )
        )

        private val enAdventureBooks = BookListResponse(
            books = listOf(
                BookListResponse.BookResponse(
                    id = "3",
                    genre = "Adventure",
                    author = "Agatha Christie",
                    title = "Murder in Mesopotamia",
                    lang = "English/Russian",
                    format = "fbwt",
                    cover = "https://s1.livelib.ru/boocover/1002005641/o/fc01/Agatha_Christie__Murder_in_Mesopotamia.jpeg",
                    link = DEAD_MANS_ISLAND_LINK,
                ),
            )
        )
    }

    suspend fun getBooks(
        locale: String,
        categoryId: String,
    ): BookListResponse {
        delay(1_000)
        validateInternetConnection(context)
        return when (locale) {
            RU_LOCALE -> {
                when (categoryId) {
                    "1" -> ruFantasticBooks
                    "2" -> ruFairytaleBooks
                    "3" -> ruAdventureBooks
                    else -> throw IllegalStateException("Unknown category id: $categoryId!")
                }
            }
            EN_LOCALE -> {
                when (categoryId) {
                    "1" -> enFantasticBooks
                    "2" -> enFairytaleBooks
                    "3" -> enAdventureBooks
                    else -> throw IllegalStateException("Unknown category id: $categoryId!")
                }
            }
            else -> throw IllegalStateException("Unknown locale: $locale")
        }
    }

    suspend fun getCategories(
        locale: String,
    ): BookCategoriesResponse {
        delay(1_000)
        validateInternetConnection(context)
        val categories = when (locale) {
            RU_LOCALE -> ruCategories
            EN_LOCALE -> enCategories
            else -> throw IllegalStateException("Unknown locale: $locale")
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