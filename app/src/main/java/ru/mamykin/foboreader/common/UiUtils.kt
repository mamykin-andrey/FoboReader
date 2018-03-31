package ru.mamykin.foboreader.common

import android.app.Activity
import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import ru.mamykin.foboreader.R

object UiUtils {

    fun enableNightMode(enable: Boolean) {
        if (enable) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun restartActivity(activity: Activity) {
        val intent = activity.intent
        activity.finish()
        activity.startActivity(intent)
    }

    fun showToast(context: Context, @StringRes message: Int) {
        showToast(context, context.getString(message))
    }

    fun showToast(context: Context, message: String) {
        showToast(context, message, Toast.LENGTH_LONG)
    }

    fun showToast(context: Context, message: String, length: Int) {
        Toast.makeText(context, message, length).show()
    }

    fun setupSearchView(context: Context, menu: Menu,
                        @IdRes menuRes: Int, @StringRes hint: Int,
                        listener: SearchView.OnQueryTextListener) {
        val searchItem = menu.findItem(menuRes)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.queryHint = context.getString(hint)
        searchView.setOnQueryTextListener(listener)
    }

    fun setupRecyclerView(context: Context,
                          recyclerView: RecyclerView,
                          adapter: RecyclerView.Adapter<*>,
                          manager: RecyclerView.LayoutManager, useDivider: Boolean) {
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        if (useDivider) {
            val itemDecorator = DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL)
            recyclerView.addItemDecoration(itemDecorator)
        }
    }

    fun showWordPopup(context: Context, source: String,
                      translation: String, onSpeakWordClicked: (String) -> Unit) {

        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vPopup = layoutInflater.inflate(R.layout.view_word_popup, null, false)
        val tvWordOriginal = vPopup.findViewById(R.id.tvWordOriginal) as TextView
        val tvWordTranslate = vPopup.findViewById(R.id.tvWordTranslate) as TextView
        val btnSpeaker = vPopup.findViewById(R.id.btnSpeaker)
        btnSpeaker.setOnClickListener { onSpeakWordClicked(source) }
        tvWordOriginal.text = source
        tvWordTranslate.text = translation

        val popup = PopupWindow(vPopup,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popup.showAtLocation(vPopup, Gravity.CENTER, 0, 200)
        vPopup.setOnClickListener { v -> popup.dismiss() }
    }
}