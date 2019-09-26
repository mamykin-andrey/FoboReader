package ru.mamykin.foboreader.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

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
        titles.add(title)
    }
}