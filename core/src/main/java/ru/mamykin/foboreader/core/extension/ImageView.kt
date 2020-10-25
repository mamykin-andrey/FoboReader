package ru.mamykin.foboreader.core.extension

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