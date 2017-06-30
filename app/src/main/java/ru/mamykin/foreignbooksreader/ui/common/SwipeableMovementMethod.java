package ru.mamykin.foreignbooksreader.ui.common;

import android.os.Handler;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Класс для {@link ru.mamykin.foreignbooksreader.ui.controls.SwipeableTextView}, для определения
 * клика, лонг тапа, и горизонталных свайпов
 */
public class SwipeableMovementMethod extends LinkMovementMethod {
    private static SwipeableMovementMethod instance;
    //private boolean longClick = false;
    private Handler longClickHandler;
    private double xStart;
    private long tStart;

    @Override
    public boolean onTouchEvent(final TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        //longClick = false;
        if (action == MotionEvent.ACTION_CANCEL && longClickHandler != null) {
            // Отмена
            longClickHandler.removeCallbacksAndMessages(null);
        } else if (action == MotionEvent.ACTION_DOWN) {
            // Нажатие
            xStart = event.getX();
            tStart = event.getEventTime();
            final SwipeableSpan link = getClickableSpan(event, widget, buffer);
            Selection.setSelection(buffer,
                    buffer.getSpanStart(link),
                    buffer.getSpanEnd(link));
            // Long click
            longClickHandler.postDelayed(() -> {
                //longClick = true;
                link.onLongClick(widget);
            }, 1000);
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            // Отпускание
            final double xDiff = event.getX() - xStart;
            final long tDiff = event.getEventTime() - tStart;
            if (Math.abs(xDiff) > 100) {
                // Horizontal swipe
                cancelLongClick();
                final SwipeableSpan link = getClickableSpan(event, widget, buffer);
                if (xDiff > 0)
                    link.onSwipeRight(widget);
                else if (xDiff < 0)
                    link.onSwipeLeft(widget);
            } else if (tDiff < 1000) {
                // Click
                cancelLongClick();
                final SwipeableSpan link = getClickableSpan(event, widget, buffer);
                link.onClick(widget);
            }
            return true;
        }
        return true;
    }

    private void cancelLongClick() {
        if (longClickHandler != null) {
            longClickHandler.removeCallbacksAndMessages(null);
        }
    }

    private SwipeableSpan getClickableSpan(MotionEvent event, TextView widget, Spannable buffer) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();
        x += widget.getScrollX();
        y += widget.getScrollY();

        final Layout layout = widget.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        SwipeableSpan[] spans = buffer.getSpans(off, off, SwipeableSpan.class);
        return spans[0];
    }

    public static MovementMethod getInstance() {
        if (instance == null) {
            instance = new SwipeableMovementMethod();
            instance.longClickHandler = new Handler();
        }

        return instance;
    }
}