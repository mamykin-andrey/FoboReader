package ru.mamykin.foreignbooksreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ru.mamykin.foreignbooksreader.models.FictionBook;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class BookDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private BookDao bookDao;

    public BookDatabaseHelper(Context context) {
        super(context, BookContract.DB_NAME, null, BookContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, FictionBook.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, FictionBook.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BookDao getBookDao() {
        if (bookDao == null) {
            try {
                bookDao = new BookDao(connectionSource, FictionBook.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bookDao;
    }
}