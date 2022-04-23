package ru.mamykin.foboreader.book_details.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import ru.mamykin.foboreader.book_details.R
import ru.mamykin.foboreader.book_details.databinding.FragmentBookDetailsBinding
import ru.mamykin.foboreader.book_details.di.DaggerBookDetailsComponent
import ru.mamykin.foboreader.book_details.presentation.list.BookInfoListAdapter
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.core.presentation.autoCleanedValue
import javax.inject.Inject

class BookDetailsFragment : BaseFragment(R.layout.fragment_book_details) {

    companion object {

        private const val EXTRA_BOOK_ID = "extra_book_id"

        fun newInstance(bookId: Long): Fragment = BookDetailsFragment().apply {
            arguments = Bundle(1).apply {
                putLong(EXTRA_BOOK_ID, bookId)
            }
        }
    }

    override val featureName: String = "book_details"

    @Inject
    internal lateinit var feature: BookDetailsFeature

    private val binding by autoCleanedValue { FragmentBookDetailsBinding.bind(requireView()) }
    private val adapter by lazy { BookInfoListAdapter() }
    private val bookId by lazy { requireArguments().getLong(EXTRA_BOOK_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerBookDetailsComponent.factory().create(
                bookId,
                apiHolder().navigationApi(),
                commonApi(),
            )
        }.inject(this)
    }

    override fun onCleared() {
        feature.onCleared()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = getString(R.string.my_books_book_info_title)
        fabRead.setOnClickListener { feature.sendIntent(BookDetailsFeature.Intent.OpenBook) }
        initBookInfoList()
        feature.stateFlow.collectWithRepeatOnStarted(::showState)
    }

    override fun onDestroyView() {
        binding.rvBookInfo.adapter = null
        super.onDestroyView()
    }

    private fun initBookInfoList() {
        binding.rvBookInfo.adapter = adapter
    }

    private fun showState(state: BookDetailsFeature.State) {
        state.bookDetails?.let {
            binding.tvBookName.text = it.title
            binding.tvBookAuthor.text = it.author
            adapter.submitList(state.items)
            showBookCover(it.coverUrl)
        }
    }

    private fun showBookCover(coverUrl: String?) {
        coverUrl?.takeIf { it.isNotEmpty() }?.let {
            Picasso.with(context)
                .load(it)
                .error(R.drawable.img_no_image)
                .into(binding.ivBookCover)
        }
    }
}