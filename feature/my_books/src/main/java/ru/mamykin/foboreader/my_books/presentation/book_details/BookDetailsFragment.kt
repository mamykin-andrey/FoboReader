package ru.mamykin.foboreader.my_books.presentation.book_details

import android.os.Bundle
import android.view.View
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_book_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mamykin.foboreader.core.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.my_books.R

class BookDetailsFragment : BaseFragment<BookDetailsViewModel, ViewState, Effect>(
    R.layout.fragment_book_detail
) {
    override val viewModel: BookDetailsViewModel by viewModel {
        parametersOf(BookDetailsFragmentArgs.fromBundle(arguments!!).bookId)
    }

//    override val showNavigationIcon = true
//    override val sharedElements: List<Pair<View, String>>
//        get() = listOf(ivBookCover to bookId.toString())
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        postponeEnterTransition()
//        initSharedTransition()
//    }
//
//    private fun initSharedTransition() {
//        val transition = TransitionInflater.from(context).inflateTransition(R.transition.move)
//        sharedElementEnterTransition = transition
//        sharedElementReturnTransition = transition
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = getString(R.string.book_info_title)
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