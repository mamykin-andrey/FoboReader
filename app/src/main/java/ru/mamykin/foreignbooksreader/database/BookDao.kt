package ru.mamykin.foreignbooksreader.database

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource
import ru.mamykin.foreignbooksreader.models.FictionBook
import java.sql.SQLException
import java.util.*

class BookDao constructor(connectionSource: ConnectionSource,
                          dataClass: Class<FictionBook>
) : BaseDaoImpl<FictionBook, Int>(connectionSource, dataClass) {

    /**
     * Получение списка книг из БД
     * @param name      фильтр по имени (поиск)
     * @param sortOrder тип сортировки, по-умолчанию - по имени
     * @return список с доступными книгами
     */
    fun getBooksList(name: String?, sortOrder: SortOrder?): List<FictionBook>? {
        try {
            val qb = queryBuilder()
            if (name != null) {
                qb.where().like(BookContract.BOOK_TITLE, "%$name%")
            }
            if (sortOrder == null || sortOrder == SortOrder.BY_NAME) {
                return query(qb.orderBy(BookContract.BOOK_TITLE, true).prepare())
            } else if (sortOrder == SortOrder.BY_READED) {
                return query(qb.orderBy(BookContract.CURRENT_PAGE, true).prepare())
            } else if (sortOrder == SortOrder.BY_DATE) {
                return query(qb.orderBy(BookContract.DOC_DATE, true).prepare())
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            return ArrayList()
        }

        return null
    }

    /**
     * Получение книги по ID
     * @param id ID книги
     * @return найденная книга, либо null, если книги с таким ID не существует
     */
    fun getBook(id: Int): FictionBook? {
        try {
            return queryForId(id)
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * Получение книги по пути к файлу
     * @return найденная книга, либо nul, если книги с таким файлом не существует
     */
    fun getBook(filePath: String): FictionBook {
        var book: FictionBook? = null
        try {
            val qb = queryBuilder()
            qb.where().eq(BookContract.FILE_PATH, filePath)
            book = qb.queryForFirst()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        if (book == null) {
            book = FictionBook()
            book.filePath = filePath
            create(book)
        }
        return book
    }

    override fun create(data: FictionBook?): Int {
        try {
            return super.create(data)
        } catch (e: SQLException) {
            e.printStackTrace()
            return -1
        }

    }

    /**
     * Проверка, существует ли книга с таким filePath
     * @param filePath путь к файлу с книгой
     * @return true, если книга с таким путём существует, false - в иных случаях
     */
    fun bookExists(filePath: String): Boolean {
        try {
            val qb = queryBuilder()
            qb.where().eq(BookContract.FILE_PATH, filePath)
            val pq = qb.prepare()
            return countOf(pq) > 0
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }

    }

    override fun delete(data: FictionBook?): Int {
        try {
            return super.delete(data)
        } catch (e: SQLException) {
            e.printStackTrace()
            return -1
        }

    }

    override fun update(data: FictionBook?): Int {
        try {
            return super.update(data)
        } catch (e: SQLException) {
            e.printStackTrace()
            return -1
        }

    }

    /**
     * Порядок сортировки книг в списке
     */
    enum class SortOrder {
        // По имени
        BY_NAME,
        // По количеству прочитанных страниц
        BY_READED,
        // По дате создания книги
        BY_DATE
    }
}