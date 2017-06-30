package ru.mamykin.foreignbooksreader.ui.common;

import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Класс для расширения Span лонг тапом и горизонтальными свайпами
 */
public abstract class SwipeableSpan extends ClickableSpan {
    abstract public void onLongClick(View view);

    abstract public void onSwipeLeft(View view);

    abstract public void onSwipeRight(View view);
}