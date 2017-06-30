package ru.mamykin.foreignbooksreader.ui.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mamykin.foreignbooksreader.R;

/**
 * Элемент отображающий горизонтальную полосу прогресса
 */
public class PercentProgressView extends LinearLayout {
    @BindView(R.id.vFill)
    protected View vFill;
    @BindView(R.id.vEmpty)
    protected View vEmpty;

    public PercentProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_percent_progress, this);
        ButterKnife.bind(this);
    }

    public void setPercents(float percents) {
        LinearLayout.LayoutParams paramsFill = new LinearLayout.LayoutParams(
                0, LayoutParams.MATCH_PARENT, percents / 100f);
        vFill.setLayoutParams(paramsFill);

        LinearLayout.LayoutParams paramsEmpty = new LinearLayout.LayoutParams(
                0, LayoutParams.MATCH_PARENT, (100 - percents) / 100f);
        vEmpty.setLayoutParams(paramsEmpty);
    }
}