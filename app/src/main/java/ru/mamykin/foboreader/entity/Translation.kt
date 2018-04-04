package ru.mamykin.foboreader.entity

class Translation(var code: Int, lang: String, text: Array<String>) {
    var lang: String? = lang
    var text: Array<String>? = text
}