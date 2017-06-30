package ru.mamykin.foreignbooksreader.models;

import com.google.gson.annotations.SerializedName;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class StoreBook {
    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("author")
    private String author;
    @SerializedName("authorId")
    private Integer authorId;
    @SerializedName("genre")
    private String genre;
    @SerializedName("rating")
    private Double rating;
    @SerializedName("oldPrice")
    private String oldPrice;
    @SerializedName("price")
    private String price;
    @SerializedName("pictureUrl")
    private String pictureUrl;

    /**
     * No args constructor for use in serialization
     */
    public StoreBook() {
    }

    public StoreBook(Integer id, String title, String author, Integer authorId, String genre, Double rating, String oldPrice, String price, String pictureUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.authorId = authorId;
        this.genre = genre;
        this.rating = rating;
        this.oldPrice = oldPrice;
        this.price = price;
        this.pictureUrl = pictureUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getRating() {
        return rating;
    }

    public String getRatingStr() {
        return String.valueOf(rating);
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getOldPrice() {
        return oldPrice + " Р";
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getPrice() {
        return price + " Р";
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}