package ru.mamykin.foboreader.core.ui

import android.content.Context
import android.view.Menu
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

@Deprecated("Remove")
object UiUtils {

    fun setupSearchView(
        context: Context,
        menu: Menu,
        @IdRes menuRes: Int,
        @StringRes hint: Int,
        onQueryChanged: (String) -> Unit = {},
        onQuerySubmit: (String) -> Unit = {}
    ) {
        val searchItem = menu.findItem(menuRes)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.queryHint = context.getString(hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { onQuerySubmit(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { onQueryChanged(it) }
                return true
            }
        })
    }

    fun setupRecyclerView(
        context: Context,
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        manager: RecyclerView.LayoutManager,
        useDivider: Boolean = false
    ) {
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        if (useDivider) {
            val itemDecorator = DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
            recyclerView.addItemDecoration(itemDecorator)
        }
    }
}