package ru.mamykin.foreignbooksreader.domain.readbook

import java.util.HashMap

class TextHashMap : HashMap<String, String>() {

    fun getKey(keyPart: String): String? {
        val mapSet = keys
        for (str in mapSet) {
            if (str.contains(keyPart))
                return str
        }
        return null
    }
}