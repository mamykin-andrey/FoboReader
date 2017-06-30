package ru.mamykin.foreignbooksreader.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.presenters.ReadBookPresenter;
import ru.mamykin.foreignbooksreader.ui.controls.SwipeableTextView;
import ru.mamykin.foreignbooksreader.views.ReadBookView;

/**
 * Страница чтения книги
 */
public class ReadBookActivity extends BaseActivity implements SwipeableTextView.SwipeableListener,
        ReadBookView, ViewTreeObserver.OnGlobalLayoutListener {
    private static final String BOOK_PATH_EXTRA = "book_path_extra";
    private static final String BOOK_ID_EXTRA = "book_id_extra";

    @InjectPresenter
    ReadBookPresenter presenter;

    @BindView(R.id.tvReadPercent)
    protected TextView tvReadPercent;
    @BindView(R.id.tvRead)
    protected TextView tvReadPages;
    @BindView(R.id.tvName)
    protected TextView tvBookName;
    @BindView(R.id.tvText)
    protected SwipeableTextView tvText;
    @BindView(R.id.pbLoading)
    protected View pbLoading;
    @BindView(R.id.llBookContent)
    protected View llBookContent;

    @ProvidePresenter
    ReadBookPresenter provideReadBookPresenter() {
        if (getIntent().getExtras().containsKey(BOOK_PATH_EXTRA)) {
            return new ReadBookPresenter(getIntent().getExtras().getString(BOOK_PATH_EXTRA));
        }
        return new ReadBookPresenter(getIntent().getExtras().getInt(BOOK_ID_EXTRA));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvText.setSwipeableListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_read_book;
    }

    @Override
    public void showToast(String text) {
        UiUtils.showToast(this, text);
    }

    @Override
    public void showLoading(boolean loading) {
        pbLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setBookName(String name) {
        tvBookName.setText(name);
    }

    @Override
    public void onClick(String paragraph) {
        presenter.onParagraphClicked(paragraph);
    }

    @Override
    public void onLongClick(String word) {
        presenter.onWordClicked(word);
    }

    @Override
    public void onSwipeLeft() {
        presenter.onSwipeLeft();
    }

    @Override
    public void onSwipeRight() {
        presenter.onSwipeRight();
    }

    @Override
    public void setReadPages(String text) {
        tvReadPages.setText(text);
    }

    @Override
    public void setReadPercent(String text) {
        tvReadPercent.setText(text);
    }

    @Override
    public void setSourceText(CharSequence text) {
        tvText.setText(text);
        tvText.updateWordLinks();
    }

    @Override
    public void setTranslationText(String text) {
        tvText.setTranslation(text);
    }

    @Override
    public void showTranslationPopup(String source, String translation) {
        UiUtils.showWordPopup(this, source, translation, word -> presenter.onSpeakWordClicked(word));
    }

    @Override
    public void initBookView(String title, String text) {
        tvText.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void showBookContent(boolean show) {
        UiUtils.setVisibility(llBookContent, show);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            tvText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            presenter.onGlobalLayout(
                    tvText.getWidth(), tvText.getHeight(), tvText.getPaint(), 1f, 0f, true);
        } else {
            tvText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            presenter.onGlobalLayout(tvText.getWidth(), tvText.getHeight(), tvText.getPaint(),
                    tvText.getLineSpacingMultiplier(), tvText.getLineSpacingExtra(), tvText.getIncludeFontPadding());
        }
    }

    public static Intent getStartIntent(Context context, int bookId) {
        final Intent readIntent = new Intent(context, ReadBookActivity.class);
        readIntent.putExtra(ReadBookActivity.BOOK_ID_EXTRA, bookId);
        return readIntent;
    }

    public static Intent getStartIntent(Context context, String bookPath) {
        final Intent readIntent = new Intent(context, ReadBookActivity.class);
        readIntent.putExtra(ReadBookActivity.BOOK_PATH_EXTRA, bookPath);
        return readIntent;
    }
}