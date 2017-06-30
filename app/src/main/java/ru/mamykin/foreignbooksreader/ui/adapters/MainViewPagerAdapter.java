package ru.mamykin.foreignbooksreader.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для ViewPager главного экрана
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentsList = new ArrayList<>();
    private List<String> titlesList = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    @Nullable
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlesList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentsList.add(fragment);
        titlesList.add(title);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}