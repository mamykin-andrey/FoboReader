package ru.mamykin.foboreader.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

import java.util.ArrayList

/**
 * Адаптер для ViewPager главного экрана
 */
class MainViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    private val fragmentsList = ArrayList<Fragment>()
    private val titlesList = ArrayList<String>()

    override fun getItem(position: Int): Fragment? {
        return fragmentsList[position]
    }

    override fun getCount(): Int {
        return fragmentsList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titlesList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentsList.add(fragment)
        titlesList.add(title)
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }
}