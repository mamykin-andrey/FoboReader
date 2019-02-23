package ru.mamykin.foboreader.core.ui

import android.app.UiModeManager.MODE_NIGHT_NO
import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.Toast

object UiUtils {

    fun enableNightMode(enable: Boolean) {
        val nightMode = if (enable) MODE_NIGHT_YES else MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(nightMode)
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
                          manager: RecyclerView.LayoutManager,
                          useDivider: Boolean = false) {
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        if (useDivider) {
            val itemDecorator = DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL)
            recyclerView.addItemDecoration(itemDecorator)
        }
    }
}