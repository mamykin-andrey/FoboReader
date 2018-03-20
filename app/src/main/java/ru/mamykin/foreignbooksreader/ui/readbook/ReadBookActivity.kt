package ru.mamykin.foreignbooksreader.ui.readbook

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter

import butterknife.BindView
import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.common.UiUtils
import ru.mamykin.foreignbooksreader.presentation.readbook.ReadBookPresenter
import ru.mamykin.foreignbooksreader.ui.global.control.SwipeableTextView
import ru.mamykin.foreignbooksreader.presentation.readbook.ReadBookView
import ru.mamykin.foreignbooksreader.ui.global.BaseActivity

/**
 * Страница чтения книги
 */
class ReadBookActivity(override val layout: Int = R.layout.activity_read_book) : BaseActivity(), SwipeableTextView.SwipeableListener, ReadBookView, ViewTreeObserver.OnGlobalLayoutListener {

    @InjectPresenter
    internal var presenter: ReadBookPresenter? = null

    @BindView(R.id.tvReadPercent)
    protected var tvReadPercent: TextView? = null
    @BindView(R.id.tvRead)
    protected var tvReadPages: TextView? = null
    @BindView(R.id.tvName)
    protected var tvBookName: TextView? = null
    @BindView(R.id.tvText)
    protected var tvText: SwipeableTextView? = null
    @BindView(R.id.pbLoading)
    protected var pbLoading: View? = null
    @BindView(R.id.llBookContent)
    protected var llBookContent: View? = null

    @ProvidePresenter
    internal fun provideReadBookPresenter(): ReadBookPresenter {
        return if (intent.extras!!.containsKey(BOOK_PATH_EXTRA)) {
            ReadBookPresenter(intent.extras!!.getString(BOOK_PATH_EXTRA)!!)
        } else ReadBookPresenter(intent.extras!!.getInt(BOOK_ID_EXTRA))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tvText!!.setSwipeableListener(this)
    }

    override fun showToast(text: String) {
        UiUtils.showToast(this, text)
    }

    override fun showLoading(loading: Boolean) {
        pbLoading!!.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun setBookName(name: String) {
        tvBookName!!.text = name
    }

    override fun onClick(paragraph: String) {
        presenter!!.onParagraphClicked(paragraph)
    }

    override fun onLongClick(word: String) {
        presenter!!.onWordClicked(word)
    }

    override fun onSwipeLeft() {
        presenter!!.onSwipeLeft()
    }

    override fun onSwipeRight() {
        presenter!!.onSwipeRight()
    }

    override fun setReadPages(text: String) {
        tvReadPages!!.text = text
    }

    override fun setReadPercent(text: String) {
        tvReadPercent!!.text = text
    }

    override fun setSourceText(text: CharSequence) {
        tvText!!.text = text
        tvText!!.updateWordLinks()
    }

    override fun setTranslationText(text: String) {
        tvText!!.setTranslation(text)
    }

    override fun showTranslationPopup(original: String, translation: String) {
        UiUtils.showWordPopup(this, original, translation, object : UiUtils.OnSpeakWordClickListener {
            override fun onSpeakWordClicked(word: String) {
                presenter!!.onSpeakWordClicked(word)
            }
        })
    }

    override fun initBookView(title: String, text: String) {
        tvText!!.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun showBookContent(show: Boolean) {
        UiUtils.setVisibility(llBookContent!!, show)
    }

    override fun onGlobalLayout() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            tvText!!.viewTreeObserver.removeGlobalOnLayoutListener(this)
            presenter!!.onGlobalLayout(
                    tvText!!.width, tvText!!.height, tvText!!.paint, 1f, 0f, true)
        } else {
            tvText!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
            presenter!!.onGlobalLayout(tvText!!.width, tvText!!.height, tvText!!.paint,
                    tvText!!.lineSpacingMultiplier, tvText!!.lineSpacingExtra, tvText!!.includeFontPadding)
        }
    }

    companion object {
        private val BOOK_PATH_EXTRA = "book_path_extra"
        private val BOOK_ID_EXTRA = "book_id_extra"

        fun getStartIntent(context: Context, bookId: Int): Intent {
            val readIntent = Intent(context, ReadBookActivity::class.java)
            readIntent.putExtra(BOOK_ID_EXTRA, bookId)
            return readIntent
        }

        fun getStartIntent(context: Context, bookPath: String): Intent {
            val readIntent = Intent(context, ReadBookActivity::class.java)
            readIntent.putExtra(BOOK_PATH_EXTRA, bookPath)
            return readIntent
        }
    }
}