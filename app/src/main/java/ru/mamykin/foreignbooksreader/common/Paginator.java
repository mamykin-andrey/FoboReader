package ru.mamykin.foreignbooksreader.common;

import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class Paginator {
    private final List<CharSequence> pagesList = new ArrayList<>();
    private int currentPageIndex = 0;

    public Paginator(CharSequence content, int width, int height, TextPaint paint,
                     float spacingMult, float spacingAdd, boolean includePad) {

        final StaticLayout layout = new StaticLayout(content, paint, width,
                Layout.Alignment.ALIGN_NORMAL, spacingMult, spacingAdd, includePad);

        final int lines = layout.getLineCount();
        final CharSequence text = layout.getText();
        int startOffset = 0;
        int lHeight = height;

        for (int i = 0; i < lines; i++) {
            if (lHeight < layout.getLineBottom(i)) {
                addPage(text.subSequence(startOffset, layout.getLineStart(i)));
                startOffset = layout.getLineStart(i);
                lHeight = layout.getLineTop(i) + height;
            }
            if (i == lines - 1) {
                addPage(text.subSequence(startOffset, layout.getLineEnd(i)));
                return;
            }
        }
    }

    private void addPage(CharSequence text) {
        pagesList.add(text);
    }

    public int getPagesCount() {
        return pagesList.size();
    }

    @Nullable
    public CharSequence getPage(int index) {
        return (index >= 0 && index < pagesList.size()) ? pagesList.get(index) : null;
    }

    public CharSequence getCurrentPage() {
        return getPage(currentPageIndex);
    }

    public float getReadPercent() {
        return (((getCurrentIndex() + 1) / (float) getPagesCount()) * 100);
    }

    public String getReadPages() {
        return (getCurrentIndex() + 1) + "/" + getPagesCount();
    }

    public int getCurrentIndex() {
        return currentPageIndex;
    }

    public void setCurrentIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }
}