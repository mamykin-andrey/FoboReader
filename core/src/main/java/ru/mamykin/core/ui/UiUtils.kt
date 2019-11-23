package ru.mamykin.core.ui

import android.app.UiModeManager.MODE_NIGHT_NO
import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.view.MenuItemCompat
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import android.view.Menu

object UiUtils {

    fun enableNightMode(enable: Boolean) {
        val nightMode = if (enable) MODE_NIGHT_YES else MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(nightMode)
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