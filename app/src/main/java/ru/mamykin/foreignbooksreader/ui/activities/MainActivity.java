package ru.mamykin.foreignbooksreader.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.presenters.MainPresenter;
import ru.mamykin.foreignbooksreader.ui.adapters.MainViewPagerAdapter;
import ru.mamykin.foreignbooksreader.ui.fragments.BooksStoreFragment;
import ru.mamykin.foreignbooksreader.ui.fragments.DeviceBooksFragment;
import ru.mamykin.foreignbooksreader.ui.fragments.DropboxBooksFragment;
import ru.mamykin.foreignbooksreader.ui.fragments.MyBooksFragment;
import ru.mamykin.foreignbooksreader.views.MainView;

/**
 * Основная страница, включает в себя страницу с книгами, файлами на устройстве, файлами Dropbox, магазином
 */
public class MainActivity extends BaseActivity implements MainView {
    private static final String TAG_MY_BOOKS_FRAGMENT = "tag_my_books_fragment";
    private static final String TAG_STORE_FRAGMENT = "tag_store_fragment";
    private static final String TAG_DEVICE_BOOKS_FRAGMENT = "tag_device_books_fragment";
    private static final String TAG_DROPBOX_BOOKS_FRAGMENT = "tag_dropbox_books_fragment";
    private static final int MY_BOOKS_FRAGMENT_POS = 0;
    private static final int DEVICE_BOOKS_FRAGMENT_POS = 1;
    private static final int DROPBOX_BOOKS_FRAGMENT_POS = 2;
    private static final int TABS_COUNT = 4;

    @BindView(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.viewpager)
    protected ViewPager viewPager;
    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;
    @BindView(R.id.navigationDrawer)
    protected NavigationView navigationView;

    @InjectPresenter
    MainPresenter presenter;

    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar(getString(R.string.main_tab), true);
        setupViewPager(viewPager, savedInstanceState);
        tabLayout.setupWithViewPager(viewPager);
        setupDrawerLayout();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    private void setupViewPager(ViewPager viewPager, Bundle savedInstanceState) {
        viewPager.setOffscreenPageLimit(TABS_COUNT);
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        if (savedInstanceState != null) {
            adapter.addFragment(getSupportFragmentManager().getFragment(
                    savedInstanceState, TAG_MY_BOOKS_FRAGMENT), getString(R.string.my_books));
            adapter.addFragment(getSupportFragmentManager().getFragment(
                    savedInstanceState, TAG_STORE_FRAGMENT), getString(R.string.store));
            adapter.addFragment(getSupportFragmentManager().getFragment(
                    savedInstanceState, TAG_DEVICE_BOOKS_FRAGMENT), getString(R.string.device));
            adapter.addFragment(getSupportFragmentManager().getFragment(
                    savedInstanceState, TAG_DROPBOX_BOOKS_FRAGMENT), getString(R.string.dropbox));
        } else {
            adapter.addFragment(MyBooksFragment.newInstance(), getString(R.string.my_books));
            adapter.addFragment(DeviceBooksFragment.newInstance(null), getString(R.string.device));
            adapter.addFragment(DropboxBooksFragment.newInstance(null), getString(R.string.dropbox));
            adapter.addFragment(BooksStoreFragment.newInstance(), getString(R.string.store));
        }
        viewPager.setAdapter(adapter);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.menu_my_books:
                viewPager.setCurrentItem(MY_BOOKS_FRAGMENT_POS);
                return true;
            case R.id.menu_device:
                viewPager.setCurrentItem(DEVICE_BOOKS_FRAGMENT_POS);
                return true;
            case R.id.menu_dropbox:
                viewPager.setCurrentItem(DROPBOX_BOOKS_FRAGMENT_POS);
                return true;
            case R.id.menu_settings:
                startActivity(SettingsActivity.getStartIntent(this));
                return true;
            case R.id.menu_about:
                startActivity(AboutActivity.getStartIntent(this));
                return true;
            case R.id.menu_exit:
                startActivity(MainActivity.getHomeIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void restartActivity() {
        UiUtils.restartActivity(this);
    }

    public static Intent getHomeIntent() {
        final Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return homeIntent;
    }

    protected void setupDrawerLayout() {
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}