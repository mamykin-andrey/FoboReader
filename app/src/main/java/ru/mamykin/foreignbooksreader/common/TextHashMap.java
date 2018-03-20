package ru.mamykin.foreignbooksreader.common;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Set;

public class TextHashMap extends HashMap<String, String> {
    @Nullable
    public String getKey(String keyPart) {
        final Set<String> mapSet = keySet();
        for (String str : mapSet) {
            if (str.contains(keyPart))
                return str;
        }
        return null;
    }
}