package ru.mamykin.foboreader.domain.readbook

import ru.mamykin.foboreader.data.model.FictionBook
import ru.mamykin.foboreader.extension.ViewParams
import rx.Single
import javax.inject.Inject

class ReadBookInteractor @Inject constructor(

//        private var paginator: Paginator? = null
//        @Inject
//        lateinit var bookDao: BookDao
//        @Inject
//lateinit var translateService: YandexTranslateService


//        private var book: FictionBook? = null
//                private var prevText: CharSequence? = null
//                private var bookPath: String? = null
//                private var bookId: Int? = null
//                private var tts: TextToSpeech? = null
//                private var ttsInit: Boolean = false
) {
    fun setupTextToSpeech() {
        //tts = TextToSpeech(context, this)
    }

    fun getBook(): Single<FictionBook> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        book!!.lastOpen = System.currentTimeMillis()
//        bookDao!!.update(book)
    }

    private fun loadBook(path: String) {
//        book = bookDao!!.getBook(path)
//        book!!.filePath = bookPath
//        bookDao!!.update(book)
        //BookXmlSaxParser.parseBook(book!!, parseListener)
    }

    /**
     * Загружаем книгу по ID
     */
    private fun loadBook(id: Int) {
        //book = bookDao!!.getBook(id)
        //BookXmlSaxParser.parseBook(book!!, parseListener)
    }

    fun loadPage(page: Int): Single<ReadBookState> {
        TODO("not implemented")
//        if (page >= 0 && page < paginator!!.pagesCount) {
//            paginator!!.currentIndex = page
//            book!!.currentPage = page
//            book!!.pagesCount = paginator!!.pagesCount
//            bookDao!!.update(book)
    }

    fun isParagraphTranslationDisplayed(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getBookFormat(): FictionBook.Format {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getTextTranslation(text: String): Single<Pair<String, String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        /**
         * val subscription = translateService!!.translate(
        context!!.getString(R.string.yandex_api_key), s, "ru", "", "")
        .applySchedulers()
         */

        /**
         * val translation = book!!.transMap!![s]
        if (translation != null) {
        prevText = paginator!!.currentPage
        viewState.displaySourceParagraph(s!!)
        viewState.displayParagraphTranslation(translation)
        } else {
        // Пробуем загрузить частично
        showOfflineTranslation(book!!.transMap!!.getKey(s!!))
        }
         */

        /**
         * val subscription = translateService!!.translate(
        context!!.getString(R.string.yandex_api_key), s, "ru", "", "")
         */
    }

    fun getSourceParagraph(): CharSequence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun voiceWord(word: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        /**
         * if (TextUtils.isEmpty(word) || !ttsInit) {
        return
        }
        tts!!.language = Locale.ENGLISH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        tts!!.speak(word, TextToSpeech.QUEUE_ADD, null, null)
        } else {
        tts!!.speak(word, TextToSpeech.QUEUE_ADD, null)
        }
         */
    }

    fun onViewInitCompleted(viewParams: ViewParams) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        /**
         * paginator = Paginator(
        book!!.fullText,
        viewParams.width,
        viewParams.height,
        viewParams.paint,
        viewParams.lineSpacingMultiplier,
        viewParams.lineSpacingExtra,
        viewParams.includeFontPadding
        )
         */
    }

    fun getLastReadedPage(): Single<ReadBookState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getNextPage(): Single<ReadBookState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        /**
         * if (paginator!!.currentIndex < paginator!!.pagesCount) {
        loadPage(paginator!!.currentIndex + 1)
        }
         */
    }

    fun getPrevPage(): Single<ReadBookState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        /**
         * if (paginator!!.currentIndex > 0) {
        loadPage(paginator!!.currentIndex - 1)
        }
         */
    }

    /**
     * override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
    ttsInit = true
    }
    }
     */
}