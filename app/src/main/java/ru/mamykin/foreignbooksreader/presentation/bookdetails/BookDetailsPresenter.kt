package ru.mamykin.foreignbooksreader.presentation.bookdetails

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.common.Utils
import ru.mamykin.foreignbooksreader.database.BookDatabaseHelper
import ru.mamykin.foreignbooksreader.models.FictionBook
import ru.mamykin.foreignbooksreader.presentation.global.BasePresenter
import javax.inject.Inject

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
class BookDetailsPresenter(private val bookId: Int) : BasePresenter<BookDetailsView>() {
    private var book: FictionBook? = null

    @Inject
    lateinit var dbHelper: BookDatabaseHelper

    init {
        ReaderApp.component.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadBook()
    }

    private fun loadBook() {
        book = dbHelper!!.getBookDao()!!.getBook(bookId)

        viewState.showBookName(book!!.bookTitle!!)
        viewState.showBookAuthor(book!!.bookAuthor!!)
        viewState.showBookPath(getFileFromPath(book!!.filePath))
        viewState.showBookCurrentPage(book!!.currentPage.toString())
        viewState.showBookGenre(book!!.bookGenre!!)
        viewState.showBookOriginalLang(book!!.bookSrcLang!!)
        viewState.showBookCreatedDate(Utils.getFormattedDate(book!!.docDate!!)!!)
    }

    private fun getFileFromPath(filePath: String?): String {
        return filePath!!.substring(filePath.lastIndexOf("/") + 1, filePath.length)
    }

    fun onReadClicked() {
        viewState.openBook(book!!.id)
    }
}