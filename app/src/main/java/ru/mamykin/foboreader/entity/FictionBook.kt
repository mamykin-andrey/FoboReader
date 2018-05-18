package ru.mamykin.foboreader.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.text.*
import android.text.style.AbsoluteSizeSpan
import ru.mamykin.foboreader.domain.readbook.TextHashMap
import ru.mamykin.foboreader.extension.getPluralsString
import java.util.*

@Entity
class FictionBook {
    @PrimaryKey
    var filePath: String = ""
    var bookGenre: String? = null
    var coverFile: String? = null
    var bookAuthor: String? = null
    var bookTitle: String = ""
    var bookLang: String? = null
    var bookSrcLang: String? = null
    var docLibrary: String? = null
    var docAuthor: String? = null
    var docUrl: String? = null
    var docDate: Date? = null
    var docVersion: Double? = null
    var sectionTitle: String? = null
    var currentPage: Int = 0
    var pagesCount: Int = 0
    var lastOpen: Long = 0
    var bookText: String = ""
    @Ignore
    var transMap: TextHashMap = TextHashMap()

    @Ignore
    private val bookFormat: BookFormat = when {
        filePath.endsWith(".fb2") -> BookFormat.FB2
        else -> BookFormat.FBWT
    }

    @Ignore
    val isFbWtBook: Boolean = bookFormat == BookFormat.FBWT

    @Ignore
    val readPercent: Float = when (pagesCount) {
        0 -> 0f
        else -> (currentPage / pagesCount) * 100f
    }

    @Suppress("deprecation")
    val fullText: Spannable
        get() {
            val htmlTitle = Html.fromHtml(bookTitle).toString() + "\n\n"
            val spTitle = SpannableString(htmlTitle)
            spTitle.setSpan(AbsoluteSizeSpan(55), 0, htmlTitle.length, Spanned.SPAN_COMPOSING)
            spTitle.setSpan(Layout.Alignment.ALIGN_CENTER, 0, 0, Spanned.SPAN_COMPOSING)

            val htmlText = Html.fromHtml(bookText).toString()
            val spText = SpannableString(htmlText)
            return SpannableString(TextUtils.concat(spTitle, spText))
        }

    val pagesCountString: String
        get() {
            val pagesFormat = currentPage.getPluralsString("страниц", "страница", "страницы")
            return "$currentPage $pagesFormat из $pagesCount"
        }

    val lastOpenString: String
        get() {
            val diffInDays = ((Date().time - lastOpen) / (1000 * 60 * 60 * 24)).toInt()
            return when {
                diffInDays == 0 -> "Открывалась сегодня"
                diffInDays < 7 -> getLastDaysOpenString(diffInDays)
                diffInDays < 30 -> getLastWeeksOpenString(diffInDays / 7)
                diffInDays < 365 -> getLastMonthsOpenString(diffInDays / 30)
                else -> getLastYearsOpenString(diffInDays / 365)
            }
        }

    private fun getLastDaysOpenString(days: Int): String {
        val quantityStr = days.getPluralsString("дней", "день", "дня") + " назад"
        return "Открывалась $days $quantityStr"
    }

    private fun getLastWeeksOpenString(weeks: Int): String {
        val quantityStr = weeks.getPluralsString("недель", "неделю", "недели") + " назад"
        return "Открывалась $weeks $quantityStr"
    }

    private fun getLastMonthsOpenString(months: Int): String {
        val quantityStr = months.getPluralsString("месяцев", "месяц", "месяца") + " назад"
        return "Открывалась $months $quantityStr"
    }

    private fun getLastYearsOpenString(years: Int): String {
        val quantityStr = years.getPluralsString("лет", "год", "года") + " назад"
        return "Открывалась $years $quantityStr"
    }

    enum class BookFormat {
        FB2, FBWT
    }
}