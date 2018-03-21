package ru.mamykin.foboreader.data.model

import com.google.gson.annotations.SerializedName

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
class StoreBook {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("title")
    var title: String? = null
    @SerializedName("author")
    var author: String? = null
    @SerializedName("authorId")
    var authorId: Int? = null
    @SerializedName("genre")
    var genre: String? = null
    @SerializedName("rating")
    var rating: Double? = null
    @SerializedName("oldPrice")
    var oldPrice: String? = null
        get() = field!! + " Р"
    @SerializedName("price")
    var price: String? = null
        get() = field!! + " Р"
    @SerializedName("pictureUrl")
    var pictureUrl: String? = null

    val ratingStr: String
        get() = rating.toString()

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    constructor(id: Int?, title: String, author: String, authorId: Int?, genre: String, rating: Double?, oldPrice: String, price: String, pictureUrl: String) {
        this.id = id
        this.title = title
        this.author = author
        this.authorId = authorId
        this.genre = genre
        this.rating = rating
        this.oldPrice = oldPrice
        this.price = price
        this.pictureUrl = pictureUrl
    }
}