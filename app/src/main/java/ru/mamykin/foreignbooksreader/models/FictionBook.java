package ru.mamykin.foreignbooksreader.models;

import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import ru.mamykin.foreignbooksreader.common.TextHashMap;
import ru.mamykin.foreignbooksreader.common.Utils;
import ru.mamykin.foreignbooksreader.database.BookContract;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 * @see <a href="http://www.fictionbook.org/index.php/%D0%9E%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5_%D1%84%D0%BE%D1%80%D0%BC%D0%B0%D1%82%D0%B0_FB2_%D0%BE%D1%82_Sclex">Описание формата Fiction Book</a>
 */
@DatabaseTable(tableName = BookContract.INSTANCE.getTABLE_NAME())
public class FictionBook {
    @DatabaseField(generatedId = true, columnName = BookContract.INSTANCE.getID())
    private int id;
    @DatabaseField(columnName = BookContract.INSTANCE.getFILE_PATH(), canBeNull = false)
    private String filePath;
    @DatabaseField(columnName = BookContract.INSTANCE.getBOOK_GENRE())
    private String bookGenre;
    @DatabaseField(columnName = BookContract.INSTANCE.getCOVER_FILE())
    private String coverFile;
    @DatabaseField(columnName = BookContract.INSTANCE.getBOOK_AUTHOR())
    private String bookAuthor;
    @DatabaseField(columnName = BookContract.INSTANCE.getBOOK_TITLE())
    private String bookTitle;
    @DatabaseField(columnName = BookContract.INSTANCE.getBOOK_LANG())
    private String bookLang;
    @DatabaseField(columnName = BookContract.INSTANCE.getBOOK_SRC_LANG())
    private String bookSrcLang;
    @DatabaseField(columnName = BookContract.INSTANCE.getDOC_LIBRARY())
    private String docLibrary;
    @DatabaseField(columnName = BookContract.INSTANCE.getDOC_AUTHOR())
    private String docAuthor;
    @DatabaseField(columnName = BookContract.INSTANCE.getDOC_URL())
    private String docUrl;
    @DatabaseField(columnName = BookContract.INSTANCE.getDOC_DATE())
    private Date docDate;
    @DatabaseField(columnName = BookContract.INSTANCE.getDOC_VERSION())
    private double docVersion;
    @DatabaseField(columnName = BookContract.INSTANCE.getSECTION_TITLE())
    private String sectionTitle;
    @DatabaseField(columnName = BookContract.INSTANCE.getCURRENT_PAGE())
    private int currentPage;
    @DatabaseField(columnName = BookContract.INSTANCE.getPAGES_COUNT())
    private int pagesCount;
    @DatabaseField(columnName = BookContract.INSTANCE.getLAST_OPEN())
    private long lastOpen;
    private String bookText;
    private TextHashMap transMap; // Todo: sortableHashmap, и bookText не нужен

    public FictionBook() {
    }

    public FictionBook(String filePath, String bookGenre, String coverFile, String bookAuthor, String bookTitle, String bookLang, String bookSrcLang, String docLibrary, String docAuthor, String docUrl, Date docDate, double docVersion, String bookText, TextHashMap transMap, String sectionTitle, int id, int currentPage, int pagesCount, long lastOpen) {
        this.filePath = filePath;
        this.bookGenre = bookGenre;
        this.coverFile = coverFile;
        this.bookAuthor = bookAuthor;
        this.bookTitle = bookTitle;
        this.bookLang = bookLang;
        this.bookSrcLang = bookSrcLang;
        this.docLibrary = docLibrary;
        this.docAuthor = docAuthor;
        this.docUrl = docUrl;
        this.docDate = docDate;
        this.docVersion = docVersion;
        this.bookText = bookText;
        this.transMap = transMap;
        this.sectionTitle = sectionTitle;
        this.id = id;
        this.currentPage = currentPage;
        this.pagesCount = pagesCount;
        this.lastOpen = lastOpen;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Format getBookFormat() {
        if (getFilePath().endsWith(".fb2"))
            return Format.FB2;
        else
            return Format.FBWT;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

    public String getCoverFile() {
        return coverFile;
    }

    public void setCover(String coverFile) {
        this.coverFile = coverFile;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookLang() {
        return bookLang;
    }

    public void setBookLang(String bookLang) {
        this.bookLang = bookLang;
    }

    public String getBookSrcLang() {
        return bookSrcLang;
    }

    public void setBookSrcLang(String bookSrcLang) {
        this.bookSrcLang = bookSrcLang;
    }

    public String getDocLibrary() {
        return docLibrary;
    }

    public void setDocLibrary(String docLibrary) {
        this.docLibrary = docLibrary;
    }

    public String getDocAuthor() {
        return docAuthor;
    }

    public void setDocAuthor(String docAuthor) {
        this.docAuthor = docAuthor;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public double getDocVersion() {
        return docVersion;
    }

    public void setDocVersion(double docVersion) {
        this.docVersion = docVersion;
    }

    public String getBookText() {
        return bookText;
    }

    public void setBookText(String bookText) {
        this.bookText = bookText;
    }

    public TextHashMap getTransMap() {
        return transMap;
    }

    public void setTransMap(TextHashMap transMap) {
        this.transMap = transMap;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    public long getLastOpen() {
        return lastOpen;
    }

    public float getPercents() {
        return ((float) getCurrentPage() / getPagesCount()) * 100f;
    }

    @SuppressWarnings("deprecation")
    public Spannable getFullText() {
        final String htmlTitle = Html.fromHtml(getBookTitle()).toString() + "\n\n";
        final Spannable spTitle = new SpannableString(htmlTitle);
        spTitle.setSpan(new AbsoluteSizeSpan(55), 0, htmlTitle.length(), Spanned.SPAN_COMPOSING);
        spTitle.setSpan((AlignmentSpan) () -> Layout.Alignment.ALIGN_CENTER, 0, htmlTitle.length(), Spanned.SPAN_COMPOSING);

        final String htmlText = Html.fromHtml(getBookText()).toString();
        final Spannable spText = new SpannableString(htmlText);
        return new SpannableString(TextUtils.concat(spTitle, spText));
    }

    public String getPagesCountString() {
        return currentPage + Utils.INSTANCE.getRightEnding(currentPage, "страниц", "страница", "страницы") + " из " + pagesCount;
    }

    public String getLastOpenString() {
        final long diffInDays = (new Date().getTime() - lastOpen) / (1000 * 60 * 60 * 24);
        if (diffInDays == 0) {
            return "Открывалась сегодня";
        } else if (diffInDays < 7) {
            // Прошло меньше одной недели
            return "Открывалась " + diffInDays + Utils.INSTANCE.getRightEnding(diffInDays, "дней", "день", "дня") + " назад";
        } else if (diffInDays < 30) {
            return "Открывалась " + diffInDays / 7 + Utils.INSTANCE.getRightEnding(diffInDays / 7, "недель", "неделю", "недели") + " назад";
        } else if (diffInDays < 365) {
            return "Открывалась " + diffInDays / 30 + Utils.INSTANCE.getRightEnding(diffInDays / 30, "месяцев", "месяц", "месяца") + " назад";
        } else {
            return "Открывалась " + diffInDays / 365 + Utils.INSTANCE.getRightEnding(diffInDays / 365, "лет", "год", "года") + " назад";
        }
    }

    public void setLastOpen(long lastOpen) {
        this.lastOpen = lastOpen;
    }

    public enum Format {
        FB2, FBWT
    }
}