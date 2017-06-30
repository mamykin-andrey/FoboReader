package ru.mamykin.foreignbooksreader.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.ui.dialogfragments.YesNoDialogFragment;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class UiUtils {
    public static void setTitle(AppCompatActivity activity, String title) {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }
    }

    public static void setHomeEnabled(AppCompatActivity activity, boolean homeEnabed) {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(homeEnabed);
        }
    }

    public static void restartActivity(Activity activity) {
        final Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }

    public static void showDialog(@NonNull AppCompatActivity activity, @StringRes int title,
                                  @StringRes int text, @NonNull String tag,
                                  @Nullable YesNoDialogFragment.PositiveClickListener positiveListener,
                                  @Nullable YesNoDialogFragment.NegativeClickListener negativeListener) {
        YesNoDialogFragment.newInstance(activity.getString(title), activity.getString(text))
                .setPositiveClickListener(positiveListener)
                .setNegativeClickListener(negativeListener)
                .show(activity.getSupportFragmentManager(), tag);
    }

    public static void showDialog(@NonNull AppCompatActivity activity, @Nullable String title,
                                  @NonNull String text, @NonNull String tag,
                                  @Nullable YesNoDialogFragment.PositiveClickListener listener) {
        YesNoDialogFragment.newInstance(title, text)
                .setPositiveClickListener(listener)
                .show(activity.getSupportFragmentManager(), tag);
    }

    public static void showToast(@NonNull Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(@NonNull Context context, @StringRes int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(@NonNull Context context, String message, int length) {
        Toast.makeText(context, message, length).show();
    }

    public static void showToast(@NonNull Context context, @StringRes int message, int length) {
        Toast.makeText(context, context.getString(message), length).show();
    }

    public static void setVisibility(@NonNull View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static void setupSearchView(@NonNull Context context, @NonNull Menu menu,
                                       @IdRes int menuRes, @StringRes int hint,
                                       SearchView.OnQueryTextListener listener) {
        final MenuItem searchItem = menu.findItem(menuRes);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(context.getString(hint));
        searchView.setOnQueryTextListener(listener);
    }

    public static void setupRecyclerView(@NonNull Context context,
                                         @NonNull RecyclerView recyclerView,
                                         @NonNull RecyclerView.Adapter adapter,
                                         RecyclerView.LayoutManager manager, boolean useDivider) {
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if (useDivider) {
            final DividerItemDecoration itemDecorator = new DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(itemDecorator);
        }
    }

    public static void setNightMode(boolean enabled) {
        AppCompatDelegate.setDefaultNightMode(enabled
                ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static boolean getNightMode() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

    public static void showWordPopup(Context context, String source, String translation,
                                     OnSpeakWordClickListener listener) {
        final LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopup = layoutInflater.inflate(R.layout.view_word_popup, null, false);
        final TextView tvWordOriginal = (TextView) vPopup.findViewById(R.id.tvWordOriginal);
        final TextView tvWordTranslate = (TextView) vPopup.findViewById(R.id.tvWordTranslate);
        final View btnSpeaker = vPopup.findViewById(R.id.btnSpeaker);
        btnSpeaker.setOnClickListener(v -> listener.onSpeakWordClicked(source));
        tvWordOriginal.setText(source);
        tvWordTranslate.setText(translation);

        final PopupWindow popup = new PopupWindow(vPopup,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup.showAtLocation(vPopup, Gravity.CENTER, 0, 200);
        vPopup.setOnClickListener(v -> popup.dismiss());
    }

    public interface OnSpeakWordClickListener {
        void onSpeakWordClicked(String word);
    }
}