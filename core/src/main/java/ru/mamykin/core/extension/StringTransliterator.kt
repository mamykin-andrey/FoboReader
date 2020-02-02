package ru.mamykin.core.extension

object StringTransliterator {

    private val replacements = mapOf(
            "A" to "A",
            "Б" to "B",
            "В" to "V",
            "Г" to "G",
            "Д" to "D",
            "Е" to "Е",
            "Ё" to "YO",
            "Ж" to "ZH",
            "З" to "Z",
            "И" to "I",
            "Й" to "Y",
            "К" to "K",
            "Л" to "L",
            "М" to "M",
            "Н" to "N",
            "О" to "O",
            "П" to "P",
            "Р" to "R",
            "C" to "S",
            "Т" to "T",
            "У" to "U",
            "Ф" to "F",
            "X" to "KH",
            "Ц" to "TS",
            "Ч" to "CH",
            "Ш" to "SH",
            "Щ" to "SCH",
            "Ъ" to "",
            "Ы" to "Y",
            "Ь" to "",
            "Э" to "E",
            "Ю" to "YU",
            "Я" to "YA",

            "a" to "a",
            "б" to "b",
            "в" to "v",
            "г" to "g",
            "д" to "d",
            "е" to "е",
            "ё" to "yo",
            "ж" to "zh",
            "з" to "z",
            "и" to "i",
            "й" to "y",
            "к" to "k",
            "л" to "l",
            "м" to "m",
            "н" to "n",
            "о" to "o",
            "п" to "p",
            "р" to "r",
            "c" to "s",
            "т" to "t",
            "у" to "u",
            "ф" to "f",
            "x" to "kh",
            "ц" to "ts",
            "ч" to "ch",
            "ш" to "sh",
            "щ" to "sch",
            "ъ" to "",
            "ы" to "y",
            "ь" to "",
            "э" to "e",
            "ю" to "yu",
            "я" to "ya"
    )

    fun transliterate(source: String): String {
        val resultSb = StringBuilder()
        source.forEach {
            val char = it.toString()
            val newChar = replacements[char] ?: char
            resultSb.append(newChar)
        }
        return resultSb.toString()
    }
}