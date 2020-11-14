package ru.mamykin.foboreader.book_details.presentation

import android.os.Bundle
import android.view.View
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mamykin.foboreader.book_details.R
import ru.mamykin.foboreader.book_details.databinding.FragmentBookDetailsBinding
import ru.mamykin.foboreader.book_details.databinding.ItemBookInfoBinding
import ru.mamykin.foboreader.book_details.presentation.list.BookInfoViewHolder
import ru.mamykin.foboreader.book_details.presentation.model.BookInfoItem
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.viewBinding

class BookDetailsFragment : BaseFragment<BookDetailsViewModel, ViewState, Effect>(R.layout.fragment_book_details) {

    override val viewModel: BookDetailsViewModel by viewModel {
        parametersOf(BookDetailsFragmentArgs.fromBundle(requireArguments()).bookId)
    }

    private val binding by viewBinding { FragmentBookDetailsBinding.bind(requireView()) }
    private val bookInfoDataSource = dataSourceTypedOf<BookInfoItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar!!.title = getString(R.string.my_books_book_info_title)
        binding.fabRead.setOnClickListener { viewModel.sendEvent(Event.ReadBookClicked) }
        initBookInfoList()
    }

    private fun initBookInfoList() {
        binding.rvBookInfo.setup {
            withDataSource(bookInfoDataSource)
            withItem<BookInfoItem, BookInfoViewHolder>(R.layout.item_book_info) {
                onBind({ BookInfoViewHolder(ItemBookInfoBinding.bind(it)) }) { _, item ->
                    bind(item)
                }
            }
        }
    }

    override fun showState(state: ViewState) {
        state.bookInfo?.let(::showBookInfo)
    }

    private fun showBookInfo(book: BookInfo) = binding.apply {
        tvBookName.text = book.title
        tvBookAuthor.text = book.author
        bookInfoDataSource.set(
            listOf(
                BookInfoItem(
                    getString(R.string.my_books_bookmarks),
                    getString(R.string.my_books_no_bookmarks)
                ),
                BookInfoItem(
                    getString(R.string.my_books_book_path),
                    book.filePath
                ),
                BookInfoItem(
                    getString(R.string.my_books_current_page),
                    book.currentPage.toString()
                ),
                BookInfoItem(
                    getString(R.string.my_books_book_genre),
                    book.genre
                )
            )
        )
        showBookCover(book.coverUrl)
    }

    private fun showBookCover(coverUrl: String?) {
        Picasso.with(context)
            .load(coverUrl)
            .error(R.drawable.img_no_image)
            .into(binding.ivBookCover)
    }

    override fun takeEffect(effect: Effect) {
        when (effect) {
            is Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}