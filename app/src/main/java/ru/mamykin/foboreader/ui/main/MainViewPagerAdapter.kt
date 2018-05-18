package ru.mamykin.foboreader.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

/**
 * Адаптер для ViewPager главного экрана
 */
class MainViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence = titles[position]

    override fun getItemPosition(obj: Any): Int = PagerAdapter.POSITION_NONE

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        this.titles.add(title)
    }
}