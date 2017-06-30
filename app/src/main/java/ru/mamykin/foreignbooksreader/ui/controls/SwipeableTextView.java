package ru.mamykin.foreignbooksreader.ui.controls;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.mamykin.foreignbooksreader.ui.common.SwipeableMovementMethod;
import ru.mamykin.foreignbooksreader.ui.common.SwipeableSpan;

/**
 * Элемент отображаюший текст книги, перевод и т д, поддерживает клики по абзацам,
 * лонг тапы по словам, горизонтальные свайпы, автоматическое масштабирование текста
 */
public class SwipeableTextView extends android.support.v7.widget.AppCompatTextView {
    private SparseIntArray textCachedSizes = new SparseIntArray();
    private TextPaint paint = new TextPaint(getPaint());
    private RectF availableSpaceRect = new RectF();
    private float maxTextSize = getTextSize();
    private RectF textRect = new RectF();
    private SwipeableListener listener;
    private boolean initializedDimens;
    private int widthLimit;

    public SwipeableTextView(Context context) {
        super(context);

        initSwipeableTextView();
    }

    public SwipeableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initSwipeableTextView();
    }

    private void initSwipeableTextView() {
        setMovementMethod(SwipeableMovementMethod.getInstance());
        setHighlightColor(Color.TRANSPARENT);
    }

    public void setSwipeableListener(SwipeableListener listener) {
        this.listener = listener;
    }

    @Override
    public void setTextSize(float size) {
        maxTextSize = size;
        textCachedSizes.clear();
        adjustTextSize();
    }

    @Override
    public void setTextSize(int unit, float size) {
        Context c = getContext();
        Resources r;

        r = c == null ? Resources.getSystem() : c.getResources();
        maxTextSize = TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
        textCachedSizes.clear();
        adjustTextSize();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        adjustTextSize();
    }

    private void adjustTextSize() {
        if (!initializedDimens)
            return;

        int heightLimit = getMeasuredHeight() - getCompoundPaddingBottom() - getCompoundPaddingTop();
        widthLimit = getMeasuredWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
        availableSpaceRect.right = widthLimit;
        availableSpaceRect.bottom = heightLimit;
        int textSize = efficientTextSizeSearch(20, (int) maxTextSize, availableSpaceRect);

        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public boolean checkSize(int suggestedSize, RectF availableRect) {
        paint.setTextSize(suggestedSize);
        String text = getText().toString();
        float spacingMult = 1.0f;
        float spacingAdd = 0.0f;
        StaticLayout layout = new StaticLayout(text, paint, widthLimit,
                Layout.Alignment.ALIGN_NORMAL, spacingMult, spacingAdd, true);

        textRect.bottom = layout.getHeight();
        int maxWidth = -1;
        for (int i = 0; i < layout.getLineCount(); i++) {
            if (maxWidth < layout.getLineWidth(i)) {
                maxWidth = (int) layout.getLineWidth(i);
            }
        }
        textRect.right = maxWidth;
        return containsRectF(availableRect, textRect);
    }

    private boolean containsRectF(RectF containerRect, RectF actualRect) {
        containerRect.offset(0, 0);
        float aArea = containerRect.width() * containerRect.height();
        actualRect.offset(0, 0);
        float bArea = actualRect.width() * actualRect.height();
        return aArea >= bArea;
    }

    private int efficientTextSizeSearch(int start, int end, RectF availableSpace) {
        int key = getText().toString().length();
        int size = textCachedSizes.get(key);
        if (size != 0) {
            return size;
        }
        textCachedSizes.put(key, size);
        return binarySearch(start, end, availableSpace);
    }

    private int binarySearch(int start, int end, RectF availableSpace) {
        int lastBest = start;
        int lowSize = start;
        int highSize = end - 1;
        int currentSize;
        while (lowSize <= highSize) {
            currentSize = (lowSize + highSize) >>> 1;
            if (checkSize(currentSize, availableSpace)) {
                // Нормальный текст/маленький
                lastBest = currentSize;
                lowSize = currentSize + 1;
            } else {
                // Слишком большой текст
                highSize = currentSize - 1;
                lastBest = lowSize;
            }
        }
        return lastBest;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        initializedDimens = true;
        textCachedSizes.clear();
        if (width != oldWidth || height != oldHeight) {
            adjustTextSize();
        }
    }

    public void updateWordLinks() {
        Spannable spans = (Spannable) getText();
        Integer[] indices = getIndices(getText().toString().trim(), ' ');
        int start = 0, end;
        for (int i = 0; i <= indices.length; i++) {
            final ClickableSpan clickSpan = getClickableSpan();
            end = (i < indices.length ? indices[i] : spans.length());
            spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end + 1;
        }
    }

    @Nullable
    private String getSelectedParagraph(TextView widget) {
        final CharSequence text = widget.getText();
        final int selStart = widget.getSelectionStart();
        final int selEnd = widget.getSelectionEnd();
        int parStart, parEnd;
        // Если кликнули по пустому параграфу
        if (text.subSequence(selStart, selEnd).toString().contains("\n")) {
            // Номер символа, с которого начинается нужный абзац
            parStart = text.subSequence(0, selEnd).toString().lastIndexOf("\n");
            parStart = parStart == -1 ? 0 : parStart;
            // Номер символа на котором кончается абзац
            parEnd = text.subSequence(selEnd, text.length()).toString().indexOf("\n");
            parEnd = parEnd == -1 ? text.length() : parEnd + selEnd;
        } else {
            // Номер символа, с которого начинается нужный абзац
            parStart = text.subSequence(0, selStart).toString().lastIndexOf("\n");
            parStart = parStart == -1 ? 0 : parStart;
            // Номер символа на котором кончается абзац
            parEnd = text.subSequence(selEnd, text.length()).toString().indexOf("\n");
            parEnd = parEnd == -1 ? text.length() : parEnd + selEnd;
        }
        return text.subSequence(parStart, parEnd).toString();
    }

    public void setTranslation(@NonNull CharSequence text) {
        final SpannableString spTrans = new SpannableString(text);
        spTrans.setSpan(new ForegroundColorSpan(
                Color.RED), 0, spTrans.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(TextUtils.concat(getText(), "\n\n", spTrans));
        updateWordLinks();
    }

    private String getSelectedWord() {
        return getText().subSequence(getSelectionStart(), getSelectionEnd()).toString().trim();
    }

    private SwipeableSpan getClickableSpan() {
        return new SwipeableSpan() {
            @Override
            public void onClick(View widget) {
                final String paragraph = getSelectedParagraph((TextView) widget);
                if (!TextUtils.isEmpty(paragraph) && listener != null) {
                    listener.onClick(paragraph.trim());
                }
            }

            @Override
            public void onLongClick(View widget) {
                final String word = getSelectedWord();
                if (!TextUtils.isEmpty(word) && listener != null) {
                    listener.onLongClick(word);
                }
            }

            @Override
            public void onSwipeLeft(View view) {
                listener.onSwipeLeft();
            }

            @Override
            public void onSwipeRight(View view) {
                listener.onSwipeRight();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
            }
        };
    }

    private Integer[] getIndices(String s, char c) {
        int pos = s.indexOf(c, 0);
        final List<Integer> indices = new ArrayList<>();
        while (pos != -1) {
            indices.add(pos);
            pos = s.indexOf(c, pos + 1);
        }
        return indices.toArray(new Integer[0]);
    }

    public interface SwipeableListener {
        void onClick(String paragraph);

        void onLongClick(String word);

//        void onPageLoaded();

        void onSwipeLeft();

        void onSwipeRight();
    }
}