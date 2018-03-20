package ru.mamykin.foreignbooksreader.database

object BookContract {
    const val DB_NAME = "books_db"
    const val DB_VERSION = 1

    const val TABLE_NAME = "books"
    const val ID = "id"
    const val FILE_PATH = "file_path"
    const val BOOK_GENRE = "book_genre"
    const val COVER_FILE = "cover_file"
    const val BOOK_AUTHOR = "book_author"
    const val BOOK_TITLE = "book_title"
    const val BOOK_LANG = "book_lang"
    const val BOOK_SRC_LANG = "book_src_lang"
    const val DOC_LIBRARY = "doc_library"
    const val DOC_AUTHOR = "doc_author"
    const val DOC_URL = "doc_url"
    const val DOC_DATE = "doc_date"
    const val DOC_VERSION = "doc_version"
    const val SECTION_TITLE = "section_title"
    const val CURRENT_PAGE = "current_page"
    const val PAGES_COUNT = "pages_count"
    const val LAST_OPEN = "last_open"
}