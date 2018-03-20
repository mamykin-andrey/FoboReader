package ru.mamykin.foreignbooksreader.database

object BookContract {
    val DB_NAME = "books_db"
    val DB_VERSION = 1

    val TABLE_NAME = "books"
    val ID = "id"
    val FILE_PATH = "file_path"
    val BOOK_GENRE = "book_genre"
    val COVER_FILE = "cover_file"
    val BOOK_AUTHOR = "book_author"
    val BOOK_TITLE = "book_title"
    val BOOK_LANG = "book_lang"
    val BOOK_SRC_LANG = "book_src_lang"
    val DOC_LIBRARY = "doc_library"
    val DOC_AUTHOR = "doc_author"
    val DOC_URL = "doc_url"
    val DOC_DATE = "doc_date"
    val DOC_VERSION = "doc_version"
    val SECTION_TITLE = "section_title"
    val CURRENT_PAGE = "current_page"
    val PAGES_COUNT = "pages_count"
    val LAST_OPEN = "last_open"
}