package ru.mamykin.foboreader.core.extension

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.mamykin.foboreader.core.platform.ChannelId

fun Fragment.showSnackbar(@StringRes messageRes: Int, long: Boolean = false) {
    showSnackbar(getString(messageRes), long)
}

fun Fragment.showSnackbar(message: String, long: Boolean = false) {
    val duration = if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
    val contentView = requireActivity().findViewById<View>(android.R.id.content)
    Snackbar.make(contentView, message, duration).show()
}

fun Fragment.showNotification(
    notificationId: Int,
    iconRes: Int,
    title: String,
    text: String,
    channelId: String = ChannelId.GENERAL,
) {
    val notification = NotificationCompat.Builder(requireContext(), channelId)
        .setSmallIcon(iconRes)
        .setContentTitle(title)
        .setContentText(text)
        .build()

    NotificationManagerCompat.from(requireContext())
        .notify(notificationId, notification)
}

fun Context.dpToPx(value: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        resources.displayMetrics
    )
}

@ColorInt
fun Context.getAttrColor(colorAttr: Int): Int {
    return TypedValue().apply {
        theme.resolveAttribute(colorAttr, this, true)
    }.data
}