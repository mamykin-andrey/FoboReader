package ru.mamykin.foboreader.read_book.data.model

import com.google.gson.annotations.SerializedName

data class Translation(
    @SerializedName("code")
    val code: Int,
    @SerializedName("lang")
    val lang: String,
    @SerializedName("text")
    val text: List<String>
)