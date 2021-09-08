package ru.mamykin.foboreader.book_details.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.squareup.picasso.Picasso
import ru.mamykin.foboreader.book_details.R
import ru.mamykin.foboreader.book_details.databinding.FragmentBookDetailsBinding
import ru.mamykin.foboreader.book_details.databinding.ItemBookInfoBinding
import ru.mamykin.foboreader.book_details.di.DaggerBookDetailsComponent
import ru.mamykin.foboreader.book_details.presentation.list.BookInfoViewHolder
import ru.mamykin.foboreader.book_details.presentation.model.BookInfoItem
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import javax.inject.Inject

class BookDetailsFragment : Fragment(R.layout.fragment_book_details) {

    companion object {

        private const val EXTRA_BOOK_ID = "extra_book_id"

        fun newInstance(bookId: Long): Fragment = BookDetailsFragment().apply {
            arguments = Bundle(1).apply {
                putLong(EXTRA_BOOK_ID, bookId)
            }
        }
    }

    @Inject
    lateinit var viewModel: BookDetailsViewModel

    private val binding by autoCleanedValue { FragmentBookDetailsBinding.bind(requireView()) }
    private val bookInfoDataSource = dataSourceTypedOf<BookInfoItem>()
    private val bookId by lazy { requireArguments().getLong(EXTRA_BOOK_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerBookDetailsComponent.factory().create(
            bookId,
            apiHolder().navigationApi(),
            apiHolder().commonApi()
        ).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        vToolbar.toolbar.title = getString(R.string.my_books_book_info_title)
        fabRead.setOnClickListener { viewModel.sendEvent(Event.ReadBookClicked) }
        initBookInfoList()
        viewModel.stateLiveData.observe(viewLifecycleOwner, ::showState)
        viewModel.effectLiveData.observe(viewLifecycleOwner, ::takeEffect)
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

    private fun showState(state: ViewState) {
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

    private fun takeEffect(effect: Effect) {
        when (effect) {
            is Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}