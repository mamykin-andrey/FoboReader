package ru.mamykin.store.data.model

import com.google.gson.annotations.SerializedName

data class StoreBooksModel(
        @SerializedName("books")
        val books: List<StoreBookModel>
)