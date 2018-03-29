package ru.mamykin.foboreader.presentation.readbook

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import com.arellomobile.mvp.InjectViewState
import org.greenrobot.eventbus.EventBus
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.common.events.UpdateEvent
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.data.model.FictionBook
import ru.mamykin.foboreader.data.model.Translation
import ru.mamykin.foboreader.data.network.YandexTranslateService
import ru.mamykin.foboreader.domain.readbook.Paginator
import ru.mamykin.foboreader.extension.ViewParams
import ru.mamykin.foboreader.extension.applySchedulers
import ru.mamykin.foboreader.presentation.global.BasePresenter
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
class ReadBookPresenter : BasePresenter<ReadBookView>, TextToSpeech.OnInitListener {
    @Inject
    lateinit var bookDao: BookDao
    @Inject
    lateinit var translateService: YandexTranslateService
    @Inject
    lateinit var context: Context
    private var paginator: Paginator? = null
    private var book: FictionBook? = null
    private var prevText: CharSequence? = null
    private var bookPath: String? = null
    private var bookId: Int? = null
    private var tts: TextToSpeech? = null
    private var ttsInit: Boolean = false

    internal var parseListener = {
        // Загрузили книгу
        viewState.showLoading(false)
        viewState.showBookContent(true)
        viewState.setBookName(book!!.bookTitle!!)
        book!!.lastOpen = System.currentTimeMillis()
        bookDao!!.update(book)
        // Запускаем подготовку SwipeableTextView
        viewState.initBookView(book!!.bookTitle!!, book!!.bookText!!)
    }

    constructor(bookPath: String) {
        this.bookPath = bookPath
        ReaderApp.component.inject(this)
    }

    constructor(bookId: Int) {
        this.bookId = bookId
        ReaderApp.component.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        setupTextToSpeech()

        if (bookPath != null) {
            loadBook(bookPath!!)
        } else {
            loadBook(bookId!!)
        }
    }

    private fun setupTextToSpeech() {
        tts = TextToSpeech(context, this)
    }

    /**
     * Если книги с таким файлом не существует, будет создана новая
     * @param path путь к файлу с книгой
     */
    private fun loadBook(path: String) {
        book = bookDao!!.getBook(path)
        book!!.filePath = bookPath
        bookDao!!.update(book)
        //BookXmlSaxParser.parseBook(book!!, parseListener)
    }

    /**
     * Загружаем книгу по ID
     */
    private fun loadBook(id: Int) {
        book = bookDao!!.getBook(id)
        //BookXmlSaxParser.parseBook(book!!, parseListener)
    }

    /**
     * Пользователь кликнул по абзацу
     * @param s строка с абзацем
     */
    fun onParagraphClicked(s: String) {
        if (!TextUtils.isEmpty(prevText)) {
            hideParagraph()
        } else if (book!!.bookFormat === FictionBook.Format.FB2) {
            showOnlineTranslation(s)
        } else {
            showOfflineTranslation(s)
        }
    }

    /**
     * Скрываем предыдущий параграф
     */
    private fun hideParagraph() {
        viewState.setSourceText(prevText!!)
        prevText = null
    }

    /**
     * Отображаем перевод с помощью Yandex Translate Api
     * @param s выбранный абзац
     */
    private fun showOnlineTranslation(s: String) {
        prevText = paginator!!.currentPage
        viewState.setSourceText(s)
        val subscription = translateService!!.translate(
                context!!.getString(R.string.yandex_api_key), s, "ru", "", "")
                .applySchedulers()
                .subscribe(object : Subscriber<Translation>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        viewState.showToast(e.localizedMessage)
                    }

                    override fun onNext(translation: Translation) {
                        viewState.setTranslationText(translation.text!![0])
                    }
                })
        unsubscribeOnDestroy(subscription)
    }

    /**
     * Отображаем перевод с мгновенным переводом
     * @param s выбранный абзац
     */
    private fun showOfflineTranslation(s: String?) {
        val translation = book!!.transMap!![s]
        if (translation != null) {
            prevText = paginator!!.currentPage
            viewState.setSourceText(s!!)
            viewState.setTranslationText(translation)
        } else {
            // Пробуем загрузить частично
            showOfflineTranslation(book!!.transMap!!.getKey(s!!))
        }
    }

    /**
     * Отображаем popup со словом/переводом и озвучкой
     * @param s выбранное слово
     */
    fun onWordClicked(s: String) {
        val subscription = translateService!!.translate(
                context!!.getString(R.string.yandex_api_key), s, "ru", "", "")
                .applySchedulers()
                .subscribe(object : Subscriber<Translation>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        viewState.showToast(e.localizedMessage)
                    }

                    override fun onNext(translation: Translation) {
                        viewState.showTranslationPopup(s, translation.text!![0])
                    }
                })
        unsubscribeOnDestroy(subscription)
    }

    /**
     * Озвучиваем выбранное слово
     * @param word строка с выбранным словом
     */
    fun onSpeakWordClicked(word: String) {
        if (TextUtils.isEmpty(word) || !ttsInit) {
            return
        }
        tts!!.language = Locale.ENGLISH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts!!.speak(word, TextToSpeech.QUEUE_ADD, null, null)
        } else {
            tts!!.speak(word, TextToSpeech.QUEUE_ADD, null)
        }
    }

    private fun loadPage(page: Int) {
        if (page >= 0 && page < paginator!!.pagesCount) {
            paginator!!.currentIndex = page
            book!!.currentPage = page
            book!!.pagesCount = paginator!!.pagesCount
            bookDao!!.update(book)
            viewState.setSourceText(paginator!!.currentPage!!)
            viewState.setReadPages(paginator!!.readPages)
            viewState.setReadPercent(context!!.getString(R.string.read_percent_string, paginator!!.readPercent))
        }
    }

    /**
     * Функция вызывается, когда TextView готов к отрисовке, и у него есть параметры
     * width, height и т д
     */
    fun onGlobalLayout(viewParams: ViewParams) {
        paginator = Paginator(
                book!!.fullText,
                viewParams.width,
                viewParams.height,
                viewParams.paint,
                viewParams.lineSpacingMultiplier,
                viewParams.lineSpacingExtra,
                viewParams.includeFontPadding
        )
        EventBus.getDefault().postSticky(UpdateEvent())
        loadPage(book!!.currentPage)
    }

    fun onSwipeRight() {
        if (paginator!!.currentIndex > 0) {
            loadPage(paginator!!.currentIndex - 1)
        }
    }

    fun onSwipeLeft() {
        if (paginator!!.currentIndex < paginator!!.pagesCount) {
            loadPage(paginator!!.currentIndex + 1)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            ttsInit = true
        }
    }
}