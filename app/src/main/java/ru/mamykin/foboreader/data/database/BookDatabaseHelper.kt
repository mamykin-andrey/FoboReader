package ru.mamykin.foboreader.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import ru.mamykin.foboreader.data.model.FictionBook
import java.sql.SQLException

class BookDatabaseHelper(context: Context) : OrmLiteSqliteOpenHelper(
        context,
        BookContract.DB_NAME,
        null,
        BookContract.DB_VERSION
) {
    private var bookDao: BookDao? = null

    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTable(connectionSource, FictionBook::class.java)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    override fun onUpgrade(database: SQLiteDatabase,
                           connectionSource: ConnectionSource,
                           oldVersion: Int,
                           newVersion: Int
    ) {
        try {
            TableUtils.dropTable<FictionBook, Any>(connectionSource, FictionBook::class.java, true)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getBookDao(): BookDao? {
        if (bookDao == null) {
            try {
                bookDao = BookDao(connectionSource, FictionBook::class.java)
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        return bookDao
    }
}