package ru.mamykin.foreignbooksreader.presenters;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.text.TextPaint;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.common.BookXmlSaxParser;
import ru.mamykin.foreignbooksreader.common.Paginator;
import ru.mamykin.foreignbooksreader.common.Utils;
import ru.mamykin.foreignbooksreader.database.BookDao;
import ru.mamykin.foreignbooksreader.events.UpdateEvent;
import ru.mamykin.foreignbooksreader.models.FictionBook;
import ru.mamykin.foreignbooksreader.models.Translation;
import ru.mamykin.foreignbooksreader.retrofit.YandexTranslateService;
import ru.mamykin.foreignbooksreader.views.ReadBookView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
public class ReadBookPresenter extends BasePresenter<ReadBookView> implements TextToSpeech.OnInitListener {
    @Inject
    protected BookDao bookDao;
    @Inject
    protected YandexTranslateService translateService;
    @Inject
    protected Context context;
    private Paginator paginator;
    private FictionBook book;
    private CharSequence prevText;
    private String bookPath;
    private Integer bookId;
    private TextToSpeech tts;
    private boolean ttsInit;

    public ReadBookPresenter(String bookPath) {
        this.bookPath = bookPath;
        ReaderApp.getComponent().inject(this);
    }

    public ReadBookPresenter(int bookId) {
        this.bookId = bookId;
        ReaderApp.getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        setupTextToSpeech();

        if (bookPath != null) {
            loadBook(bookPath);
        } else {
            loadBook(bookId);
        }
    }

    private void setupTextToSpeech() {
        tts = new TextToSpeech(context, this);
    }

    /**
     * Если книги с таким файлом не существует, будет создана новая
     * @param path путь к файлу с книгой
     */
    private void loadBook(String path) {
        book = bookDao.getBook(path);
        book.setFilePath(bookPath);
        bookDao.update(book);
        BookXmlSaxParser.parseBook(book, parseListener);
    }

    /**
     * Загружаем книгу по ID
     */
    private void loadBook(int id) {
        book = bookDao.getBook(id);
        BookXmlSaxParser.parseBook(book, parseListener);
    }

    /**
     * Пользователь кликнул по абзацу
     * @param s строка с абзацем
     */
    public void onParagraphClicked(String s) {
        if (!TextUtils.isEmpty(prevText)) {
            hideParagraph();
        } else if (book.getBookFormat() == FictionBook.Format.FB2) {
            showOnlineTranslation(s);
        } else {
            showOfflineTranslation(s);
        }
    }

    /**
     * Скрываем предыдущий параграф
     */
    private void hideParagraph() {
        getViewState().setSourceText(prevText);
        prevText = null;
    }

    /**
     * Отображаем перевод с помощью Yandex Translate Api
     * @param s выбранный абзац
     */
    private void showOnlineTranslation(String s) {
        prevText = paginator.getCurrentPage();
        getViewState().setSourceText(s);
        Subscription subscription = translateService.translate(
                context.getString(R.string.yandex_api_key), s, "ru", null, null)
                .compose(Utils.applySchedulers())
                .subscribe(new Subscriber<Translation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getViewState().showToast(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Translation translation) {
                        getViewState().setTranslationText(translation.getText()[0]);
                    }
                });
        unsubscribeOnDestroy(subscription);
    }

    /**
     * Отображаем перевод с мгновенным переводом
     * @param s выбранный абзац
     */
    private void showOfflineTranslation(String s) {
        String translation = book.getTransMap().get(s);
        if (translation != null) {
            prevText = paginator.getCurrentPage();
            getViewState().setSourceText(s);
            getViewState().setTranslationText(translation);
        } else {
            // Пробуем загрузить частично
            showOfflineTranslation(book.getTransMap().getKey(s));
        }
    }

    /**
     * Отображаем popup со словом/переводом и озвучкой
     * @param s выбранное слово
     */
    public void onWordClicked(String s) {
        Subscription subscription = translateService.translate(
                context.getString(R.string.yandex_api_key), s, "ru", null, null)
                .compose(Utils.applySchedulers())
                .subscribe(new Subscriber<Translation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getViewState().showToast(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Translation translation) {
                        getViewState().showTranslationPopup(s, translation.getText()[0]);
                    }
                });
        unsubscribeOnDestroy(subscription);
    }

    /**
     * Озвучиваем выбранное слово
     * @param word строка с выбранным словом
     */
    @SuppressWarnings("deprecation")
    public void onSpeakWordClicked(String word) {
        if (TextUtils.isEmpty(word) || !ttsInit) {
            return;
        }
        tts.setLanguage(Locale.ENGLISH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(word, TextToSpeech.QUEUE_ADD, null, null);
        } else {
            tts.speak(word, TextToSpeech.QUEUE_ADD, null);
        }
    }

    BookXmlSaxParser.SaxParserListener parseListener = () -> {
        // Загрузили книгу
        getViewState().showLoading(false);
        getViewState().showBookContent(true);
        getViewState().setBookName(book.getBookTitle());
        book.setLastOpen(System.currentTimeMillis());
        bookDao.update(book);
        // Запускаем подготовку SwipeableTextView
        getViewState().initBookView(book.getBookTitle(), book.getBookText());
    };

    private void loadPage(int page) {
        if (page >= 0 && page < paginator.getPagesCount()) {
            paginator.setCurrentIndex(page);
            book.setCurrentPage(page);
            book.setPagesCount(paginator.getPagesCount());
            bookDao.update(book);
            getViewState().setSourceText(paginator.getCurrentPage());
            getViewState().setReadPages(paginator.getReadPages());
            getViewState().setReadPercent(context.getString(R.string.read_percent_string, paginator.getReadPercent()));
        }
    }

    /**
     * Функция вызывается, когда TextView готов к отрисовке, и у него есть параметры
     * width, height и т д
     */
    public void onGlobalLayout(int width, int height, TextPaint paint, float lineSpacingMultiplier, float lineSpacingExtra, boolean includeFontPadding) {
        paginator = new Paginator(book.getFullText(), width, height, paint, lineSpacingMultiplier, lineSpacingExtra, includeFontPadding);
        EventBus.getDefault().postSticky(new UpdateEvent());
        loadPage(book.getCurrentPage());
    }

    public void onSwipeRight() {
        if (paginator.getCurrentIndex() > 0) {
            loadPage(paginator.getCurrentIndex() - 1);
        }
    }

    public void onSwipeLeft() {
        if (paginator.getCurrentIndex() < paginator.getPagesCount()) {
            loadPage(paginator.getCurrentIndex() + 1);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            ttsInit = true;
        }
    }
}