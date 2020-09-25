package ru.mamykin.foboreader.my_books.presentation.book_details

import android.os.Bundle
import android.view.View
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_book_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.my_books.R

class BookDetailsFragment : BaseFragment<BookDetailsViewModel, ViewState, Effect>(
    R.layout.fragment_book_detail
) {
    companion object {

        private const val EXTRA_BOOK_ID = "BOOK_ID"

        fun bundle(bookId: Long) = Bundle().apply {
            putLong(EXTRA_BOOK_ID, bookId)
        }
    }

    override val viewModel: BookDetailsViewModel by viewModel {
        parametersOf(arguments?.getLong(EXTRA_BOOK_ID) ?: throw IllegalStateException("No book ID!"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = getString(R.string.my_books_book_info_title)
        fabRead.setOnClickListener { viewModel.sendEvent(Event.ReadBookClicked) }
    }

    private fun showBookCover(coverUrl: String?) {
        Picasso.with(context)
            .load(coverUrl)
            .error(R.drawable.img_no_image)
            .into(ivBookCover, object : Callback {
                override fun onSuccess() {
                    startPostponedEnterTransition()
                }

                override fun onError() {
                    startPostponedEnterTransition()
                }
            })
    }

    override fun showState(state: ViewState) {
        state.bookInfo?.let(::showBookInfo)
    }

    private fun showBookInfo(book: BookInfo) {
        tvBookName.text = book.title
        tvBookAuthor.text = book.author
        tvBookPath.text = book.filePath
        tvCurrentPage.text = book.currentPage.toString()
        tvBookGenre.text = book.genre
        showBookCover(book.coverUrl)
    }

    override fun takeEffect(effect: Effect) {
        when (effect) {
            is Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}