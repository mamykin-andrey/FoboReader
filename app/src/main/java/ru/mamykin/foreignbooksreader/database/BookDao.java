package ru.mamykin.foreignbooksreader.database;

import android.support.annotation.Nullable;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.mamykin.foreignbooksreader.models.FictionBook;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class BookDao extends BaseDaoImpl<FictionBook, Integer> {
    protected BookDao(ConnectionSource connectionSource, Class<FictionBook> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    /**
     * Получение списка книг из БД
     * @param name      фильтр по имени (поиск)
     * @param sortOrder тип сортировки, по-умолчанию - по имени
     * @return список с доступными книгами
     */
    public List<FictionBook> getBooksList(@Nullable String name, @Nullable SortOrder sortOrder) {
        try {
            QueryBuilder<FictionBook, Integer> qb = queryBuilder();
            if (name != null) {
                qb.where().like(BookContract.BOOK_TITLE, "%" + name + "%");
            }
            if (sortOrder == null || sortOrder == SortOrder.BY_NAME) {
                return query(qb.orderBy(BookContract.BOOK_TITLE, true).prepare());
            } else if (sortOrder == SortOrder.BY_READED) {
                return query(qb.orderBy(BookContract.CURRENT_PAGE, true).prepare());
            } else if (sortOrder == SortOrder.BY_DATE) {
                return query(qb.orderBy(BookContract.DOC_DATE, true).prepare());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return null;
    }

    /**
     * Получение книги по ID
     * @param id ID книги
     * @return найденная книга, либо null, если книги с таким ID не существует
     */
    public FictionBook getBook(int id) {
        try {
            return queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Получение книги по пути к файлу
     * @return найденная книга, либо nul, если книги с таким файлом не существует
     */
    public FictionBook getBook(String filePath) {
        FictionBook book = null;
        try {
            QueryBuilder<FictionBook, Integer> qb = queryBuilder();
            qb.where().eq(BookContract.FILE_PATH, filePath);
            book = qb.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (book == null) {
            book = new FictionBook();
            book.setFilePath(filePath);
            create(book);
        }
        return book;
    }

    @Override
    public int create(FictionBook data) {
        try {
            return super.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Проверка, существует ли книга с таким filePath
     * @param filePath путь к файлу с книгой
     * @return true, если книга с таким путём существует, false - в иных случаях
     */
    public boolean bookExists(String filePath) {
        try {
            QueryBuilder<FictionBook, Integer> qb = queryBuilder();
            qb.where().eq(BookContract.FILE_PATH, filePath);
            PreparedQuery<FictionBook> pq = qb.prepare();
            return countOf(pq) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int delete(FictionBook data) {
        try {
            return super.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int update(FictionBook data) {
        try {
            return super.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Порядок сортировки книг в списке
     */
    public enum SortOrder {
        // По имени
        BY_NAME,
        // По количеству прочитанных страниц
        BY_READED,
        // По дате создания книги
        BY_DATE
    }
}