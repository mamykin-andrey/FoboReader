package ru.mamykin.foreignbooksreader.presenters

import android.text.TextUtils

import com.arellomobile.mvp.InjectViewState

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import javax.inject.Inject

import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.database.BookDao
import ru.mamykin.foreignbooksreader.events.UpdateEvent
import ru.mamykin.foreignbooksreader.models.FictionBook
import ru.mamykin.foreignbooksreader.views.MyBooksView

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
class MyBooksPresenter : BasePresenter<MyBooksView>() {
    private var booksList: List<FictionBook>? = null
    private var searchQuery: String? = null
    private var sortOrder: BookDao.SortOrder? = null
    @Inject
    protected var bookDao: BookDao? = null

    init {
        ReaderApp.component.inject(this)
    }

    override fun attachView(view: MyBooksView) {
        super.attachView(view)

        loadBooksList()

        EventBus.getDefault().register(this)
        onMessageEvent(EventBus.getDefault().getStickyEvent<UpdateEvent>(UpdateEvent::class.java!!))
    }

    @Subscribe
    fun onMessageEvent(e: UpdateEvent?) {
        if (e != null) {
            EventBus.getDefault().removeStickyEvent(UpdateEvent::class.java)
            loadBooksList()
        }
    }

    override fun detachView(view: MyBooksView) {
        super.detachView(view)

        EventBus.getDefault().unregister(this)
    }

    fun onActionSortNameSelected() {
        sortOrder = BookDao.SortOrder.BY_NAME
        loadBooksList()
    }

    fun onActionSortReadedSelected() {
        sortOrder = BookDao.SortOrder.BY_READED
        loadBooksList()
    }

    fun onActionSortDateSelected() {
        sortOrder = BookDao.SortOrder.BY_DATE
        loadBooksList()
    }

    fun onBookClicked(position: Int) {
        viewState.openBook(booksList!![position].id)
    }

    fun onBookAboutClicked(position: Int) {
        viewState.openBookDetails(booksList!![position].id)
    }

    fun onBookShareClicked(position: Int) {
        val book = booksList!![position]
        if (TextUtils.isEmpty(book.docUrl)) {
            viewState.showBookShareDialog(book.bookTitle!!)
        } else {
            viewState.showBookShareDialog(book.bookTitle!!, book.docUrl!!)
        }
    }

    fun onBookRemoveClicked(position: Int) {
        bookDao!!.delete(booksList!![position])
        loadBooksList()
    }

    fun onQueryTextChange(text: String) {
        searchQuery = text
        loadBooksList()
    }

    fun loadBooksList() {
        booksList = bookDao!!.getBooksList(searchQuery, sortOrder)
        if (booksList!!.size == 0) {
            viewState.showEmptyStateView()
        } else {
            viewState.showBooksList(booksList!!)
        }
    }
}