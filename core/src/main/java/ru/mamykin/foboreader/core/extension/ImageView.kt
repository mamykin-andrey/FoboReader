package ru.mamykin.foboreader.core.extension

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.squareup.picasso.Picasso

fun ImageView.loadImage(url: String?, @DrawableRes defaultImageRes: Int) {
    url?.takeIf { it.isNotEmpty() }?.let {
        Picasso.with(context)
            .load(it)
            .error(defaultImageRes)
            .into(this)
    } ?: setImageResource(defaultImageRes)
}

fun ImageView.setColorFilter(colorCode: String) {
    colorFilter = PorterDuffColorFilter(
        Color.parseColor(colorCode),
        PorterDuff.Mode.SRC_ATOP
    )
}