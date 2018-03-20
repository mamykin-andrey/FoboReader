package ru.mamykin.foreignbooksreader.common

import android.app.Activity
import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.ui.global.YesNoDialogFragment

object UiUtils {

    var nightMode: Boolean
        get() = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        set(enabled) = AppCompatDelegate.setDefaultNightMode(if (enabled)
            AppCompatDelegate.MODE_NIGHT_YES
        else
            AppCompatDelegate.MODE_NIGHT_NO)

    fun setTitle(activity: AppCompatActivity, title: String) {
        if (activity.supportActionBar != null) {
            activity.supportActionBar!!.title = title
        }
    }

    fun setHomeEnabled(activity: AppCompatActivity, homeEnabed: Boolean) {
        if (activity.supportActionBar != null) {
            activity.supportActionBar!!.setDisplayHomeAsUpEnabled(homeEnabed)
        }
    }

    fun restartActivity(activity: Activity) {
        val intent = activity.intent
        activity.finish()
        activity.startActivity(intent)
    }

    fun showDialog(activity: AppCompatActivity, @StringRes title: Int,
                   @StringRes text: Int, tag: String,
                   positiveListener: YesNoDialogFragment.PositiveClickListener?,
                   negativeListener: YesNoDialogFragment.NegativeClickListener?) {
        YesNoDialogFragment.newInstance(activity.getString(title), activity.getString(text))
                .setPositiveClickListener(positiveListener!!)
                .setNegativeClickListener(negativeListener!!)
                .show(activity.supportFragmentManager, tag)
    }

    fun showDialog(activity: AppCompatActivity, title: String?,
                   text: String, tag: String,
                   listener: YesNoDialogFragment.PositiveClickListener?) {
        YesNoDialogFragment.newInstance(title!!, text)
                .setPositiveClickListener(listener!!)
                .show(activity.supportFragmentManager, tag)
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, @StringRes message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, message: String, length: Int) {
        Toast.makeText(context, message, length).show()
    }

    fun showToast(context: Context, @StringRes message: Int, length: Int) {
        Toast.makeText(context, context.getString(message), length).show()
    }

    fun setVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
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

    fun showWordPopup(context: Context, source: String, translation: String,
                      listener: OnSpeakWordClickListener) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vPopup = layoutInflater.inflate(R.layout.view_word_popup, null, false)
        val tvWordOriginal = vPopup.findViewById(R.id.tvWordOriginal) as TextView
        val tvWordTranslate = vPopup.findViewById(R.id.tvWordTranslate) as TextView
        val btnSpeaker = vPopup.findViewById(R.id.btnSpeaker)
        btnSpeaker.setOnClickListener { v -> listener.onSpeakWordClicked(source) }
        tvWordOriginal.text = source
        tvWordTranslate.text = translation

        val popup = PopupWindow(vPopup,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popup.showAtLocation(vPopup, Gravity.CENTER, 0, 200)
        vPopup.setOnClickListener { v -> popup.dismiss() }
    }

    interface OnSpeakWordClickListener {
        fun onSpeakWordClicked(word: String)
    }
}