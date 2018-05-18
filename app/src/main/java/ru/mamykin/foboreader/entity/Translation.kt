package ru.mamykin.foboreader.entity

import com.google.gson.annotations.SerializedName

data class Translation(
        @SerializedName("code")
        val code: Int,
        @SerializedName("lang")
        val lang: String,
        @SerializedName("text")
        val text: List<String>
)