package ru.mamykin.foreignbooksreader.data.model

import com.google.gson.annotations.SerializedName

/**
 * Creation date: 5/29/2017
 * Creation time: 3:32 PM
 * @author Andrey Mamykin(mamykin_av)
 */
class UpdateInfo {
    @SerializedName("date")
    var date: String? = null
    @SerializedName("changes")
    var changelog: String? = null
        private set

    constructor() {}

    constructor(date: String, chages: String) {
        this.date = date
        this.changelog = chages
    }

    fun setChages(chages: String) {
        this.changelog = chages
    }
}