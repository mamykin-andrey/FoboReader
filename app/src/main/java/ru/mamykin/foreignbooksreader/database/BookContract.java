package ru.mamykin.foreignbooksreader.database;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class BookContract {
    public static final String DB_NAME = "books_db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "books";
    public static final String ID = "id";
    public static final String FILE_PATH = "file_path";
    public static final String BOOK_GENRE = "book_genre";
    public static final String COVER_FILE = "cover_file";
    public static final String BOOK_AUTHOR = "book_author";
    public static final String BOOK_TITLE = "book_title";
    public static final String BOOK_LANG = "book_lang";
    public static final String BOOK_SRC_LANG = "book_src_lang";
    public static final String DOC_LIBRARY = "doc_library";
    public static final String DOC_AUTHOR = "doc_author";
    public static final String DOC_URL = "doc_url";
    public static final String DOC_DATE = "doc_date";
    public static final String DOC_VERSION = "doc_version";
    public static final String SECTION_TITLE = "section_title";
    public static final String CURRENT_PAGE = "current_page";
    public static final String PAGES_COUNT = "pages_count";
    public static final String LAST_OPEN = "last_open";
}