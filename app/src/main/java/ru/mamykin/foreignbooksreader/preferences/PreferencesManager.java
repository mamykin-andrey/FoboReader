package ru.mamykin.foreignbooksreader.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.preferences.PreferenceNames;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class PreferencesManager implements PreferenceNames {
    @Inject
    protected Context context;

    public PreferencesManager() {
        ReaderApp.getComponent().inject(this);
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(key, defValue);
    }

    public boolean getBoolean(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    public String getString(String key, String defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(key, defValue);
    }

    public String getString(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(key, null);
    }

    public void putInt(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value).apply();
    }

    public int getInt(String key, int defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getInt(key, defValue);
    }

    public int getInt(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getInt(key, -1);
    }

    public void putFloat(String key, float value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value).apply();
    }

    public float getFloat(String key, float defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getFloat(key, defValue);
    }

    public float getFloat(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getFloat(key, -1f);
    }

    public void removeValue(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).apply();
    }

    public boolean contains(String prefName) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.contains(prefName);
    }
}