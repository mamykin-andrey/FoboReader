package ru.mamykin.foreignbooksreader.ui.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;

import java.lang.ref.WeakReference;

/**
 * Диалог с текстом и заголовком, в каждом диалоге отображаются поля "Ок" и "Отмена"
 */
public class YesNoDialogFragment extends AppCompatDialogFragment implements DialogInterface.OnClickListener {
    private static final String TITLE_EXTRA = "title_extra";
    private static final String MESSAGE_EXTRA = "message_extra";

    private String title;
    private String message;
    private WeakReference<PositiveClickListener> positiveListenerRef;
    private WeakReference<NegativeClickListener> negativeListenerRef;

    public static YesNoDialogFragment newInstance(String title, String message) {
        YesNoDialogFragment dialogFragment = new YesNoDialogFragment();
        final Bundle args = new Bundle();
        args.putString(TITLE_EXTRA, title);
        args.putString(MESSAGE_EXTRA, message);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
        title = args.getString(TITLE_EXTRA);
        message = args.getString(MESSAGE_EXTRA);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setPositiveButton(android.R.string.yes, this)
                .setNegativeButton(android.R.string.no, this)
                .setMessage(message);
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE_EXTRA, title);
        outState.putString(MESSAGE_EXTRA, message);
    }

    public YesNoDialogFragment setPositiveClickListener(PositiveClickListener listener) {
        this.positiveListenerRef = new WeakReference<>(listener);
        return this;
    }

    public YesNoDialogFragment setNegativeClickListener(NegativeClickListener listener) {
        this.negativeListenerRef = new WeakReference<>(listener);
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE && positiveListenerRef != null && positiveListenerRef.get() != null) {
            positiveListenerRef.get().onPositiveClick();
        } else if (which == Dialog.BUTTON_NEGATIVE && negativeListenerRef != null && negativeListenerRef.get() != null) {
            negativeListenerRef.get().onNegativeClick();
        }
        dismiss();
    }

    public interface PositiveClickListener {
        void onPositiveClick();
    }

    public interface NegativeClickListener {
        void onNegativeClick();
    }
}