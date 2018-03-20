package ru.mamykin.foreignbooksreader.models

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
class Translation {
    var code: Int = 0
    var lang: String? = null
    var text: Array<String>? = null

    constructor() {}

    constructor(code: Int, lang: String, text: Array<String>) {
        this.code = code
        this.lang = lang
        this.text = text
    }
}