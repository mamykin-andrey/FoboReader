package ru.mamykin.foboreader.data.model

import android.text.Html
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.AlignmentSpan

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

import java.util.Date

import ru.mamykin.foboreader.domain.readbook.TextHashMap
import ru.mamykin.foboreader.domain.Utils
import ru.mamykin.foboreader.data.database.BookContract

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 * @see [Описание формата Fiction Book](http://www.fictionbook.org/index.php/%D0%9E%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5_%D1%84%D0%BE%D1%80%D0%BC%D0%B0%D1%82%D0%B0_FB2_%D0%BE%D1%82_Sclex)
 */
@DatabaseTable(tableName = BookContract.TABLE_NAME)
class FictionBook {
    @DatabaseField(generatedId = true, columnName = BookContract.ID)
    var id: Int = 0
    @DatabaseField(columnName = BookContract.FILE_PATH, canBeNull = false)
    var filePath: String? = null
    @DatabaseField(columnName = BookContract.BOOK_GENRE)
    var fileName: String = ""
    var bookGenre: String? = null
    @DatabaseField(columnName = BookContract.COVER_FILE)
    var coverFile: String? = null
        private set
    @DatabaseField(columnName = BookContract.BOOK_AUTHOR)
    var bookAuthor: String? = null
    @DatabaseField(columnName = BookContract.BOOK_TITLE)
    var bookTitle: String? = null
    @DatabaseField(columnName = BookContract.BOOK_LANG)
    var bookLang: String? = null
    @DatabaseField(columnName = BookContract.BOOK_SRC_LANG)
    var bookSrcLang: String? = null
    @DatabaseField(columnName = BookContract.DOC_LIBRARY)
    var docLibrary: String? = null
    @DatabaseField(columnName = BookContract.DOC_AUTHOR)
    var docAuthor: String? = null
    @DatabaseField(columnName = BookContract.DOC_URL)
    var docUrl: String? = null
    @DatabaseField(columnName = BookContract.DOC_DATE)
    var docDate: Date? = null
    @DatabaseField(columnName = BookContract.DOC_VERSION)
    var docVersion: Double = 0.toDouble()
    @DatabaseField(columnName = BookContract.SECTION_TITLE)
    var sectionTitle: String? = null
    @DatabaseField(columnName = BookContract.CURRENT_PAGE)
    var currentPage: Int = 0
    @DatabaseField(columnName = BookContract.PAGES_COUNT)
    var pagesCount: Int = 0
    @DatabaseField(columnName = BookContract.LAST_OPEN)
    var lastOpen: Long = 0
    var bookText: String? = null
    var transMap: TextHashMap? = null // Todo: sortableHashmap, и bookText не нужен

    val bookFormat: Format
        get() = if (filePath!!.endsWith(".fb2"))
            Format.FB2
        else
            Format.FBWT

    val percents: Float
        get() = currentPage.toFloat() / pagesCount * 100f

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
        get() = currentPage.toString() + Utils.getRightEnding(currentPage.toLong(), "страниц", "страница", "страницы") + " из " + pagesCount

    // Прошло меньше одной недели
    val lastOpenString: String
        get() {
            val diffInDays = (Date().time - lastOpen) / (1000 * 60 * 60 * 24)
            return if (diffInDays == 0L) {
                "Открывалась сегодня"
            } else if (diffInDays < 7) {
                "Открывалась " + diffInDays + Utils.getRightEnding(diffInDays, "дней", "день", "дня") + " назад"
            } else if (diffInDays < 30) {
                "Открывалась " + diffInDays / 7 + Utils.getRightEnding(diffInDays / 7, "недель", "неделю", "недели") + " назад"
            } else if (diffInDays < 365) {
                "Открывалась " + diffInDays / 30 + Utils.getRightEnding(diffInDays / 30, "месяцев", "месяц", "месяца") + " назад"
            } else {
                "Открывалась " + diffInDays / 365 + Utils.getRightEnding(diffInDays / 365, "лет", "год", "года") + " назад"
            }
        }

    constructor() {}

    constructor(filePath: String, bookGenre: String, coverFile: String, bookAuthor: String, bookTitle: String, bookLang: String, bookSrcLang: String, docLibrary: String, docAuthor: String, docUrl: String, docDate: Date, docVersion: Double, bookText: String, transMap: TextHashMap, sectionTitle: String, id: Int, currentPage: Int, pagesCount: Int, lastOpen: Long) {
        this.filePath = filePath
        this.bookGenre = bookGenre
        this.coverFile = coverFile
        this.bookAuthor = bookAuthor
        this.bookTitle = bookTitle
        this.bookLang = bookLang
        this.bookSrcLang = bookSrcLang
        this.docLibrary = docLibrary
        this.docAuthor = docAuthor
        this.docUrl = docUrl
        this.docDate = docDate
        this.docVersion = docVersion
        this.bookText = bookText
        this.transMap = transMap
        this.sectionTitle = sectionTitle
        this.id = id
        this.currentPage = currentPage
        this.pagesCount = pagesCount
        this.lastOpen = lastOpen
    }

    fun setCover(coverFile: String) {
        this.coverFile = coverFile
    }

    enum class Format {
        FB2, FBWT
    }
}