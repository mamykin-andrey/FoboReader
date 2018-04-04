package ru.mamykin.foboreader.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.text.*
import android.text.style.AbsoluteSizeSpan
import android.text.style.AlignmentSpan
import ru.mamykin.foboreader.domain.Utils
import ru.mamykin.foboreader.domain.readbook.TextHashMap
import java.util.*


@Entity
class FictionBook(
        @PrimaryKey
        var id: Int,
        var filePath: String,
        var bookGenre: String?,
        var coverFile: String?,
        var bookAuthor: String?,
        var bookTitle: String,
        var bookLang: String?,
        var bookSrcLang: String?,
        var docLibrary: String?,
        var docAuthor: String?,
        var docUrl: String?,
        var docDate: Date?,
        var docVersion: Double?,
        var sectionTitle: String?,
        var currentPage: Int,
        var pagesCount: Int,
        var lastOpen: Long,
        var bookText: String,
        var transMap: TextHashMap
) {
    val bookFormat: BookFormat
        get() {
            if (filePath.endsWith(".fb2"))
                return BookFormat.FB2
            else
                return BookFormat.FBWT
        }


    val readPercent: Float
        get() = (currentPage / pagesCount) * 100f

    val fullText: Spannable
        get() {
            val htmlTitle = Html.fromHtml(bookTitle).toString() + "\n\n"
            val spTitle = SpannableString(htmlTitle)
            spTitle.setSpan(AbsoluteSizeSpan(55), 0, htmlTitle.length, Spanned.SPAN_COMPOSING)
            spTitle.setSpan({ Layout.Alignment.ALIGN_CENTER } as AlignmentSpan, 0, htmlTitle.length, Spanned.SPAN_COMPOSING)

            val htmlText = Html.fromHtml(bookText).toString()
            val spText = SpannableString(htmlText)
            return SpannableString(TextUtils.concat(spTitle, spText))
        }

    val pagesCountString: String
        get() {
            val pagesFormat = Utils.getRightEnding(currentPage, "страниц", "страница", "страницы")
            return "$currentPage $pagesFormat из $pagesCount"
        }

    val lastOpenString: String
        get() {
            val diffInDays = ((Date().time - lastOpen) / (1000 * 60 * 60 * 24)).toInt()
            return when {
                diffInDays == 0 -> "Открывалась сегодня"
                diffInDays < 7 -> "Открывалась " + diffInDays + Utils.getRightEnding(diffInDays, "дней", "день", "дня") + " назад"
                diffInDays < 30 -> "Открывалась " + diffInDays / 7 + Utils.getRightEnding(diffInDays / 7, "недель", "неделю", "недели") + " назад"
                diffInDays < 365 -> "Открывалась " + diffInDays / 30 + Utils.getRightEnding(diffInDays / 30, "месяцев", "месяц", "месяца") + " назад"
                else -> "Открывалась " + diffInDays / 365 + Utils.getRightEnding(diffInDays / 365, "лет", "год", "года") + " назад"
            }
        }

    enum class BookFormat {
        FB2, FBWT
    }
}