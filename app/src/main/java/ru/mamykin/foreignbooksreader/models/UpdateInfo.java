package ru.mamykin.foreignbooksreader.models;

import com.google.gson.annotations.SerializedName;

/**
 * Creation date: 5/29/2017
 * Creation time: 3:32 PM
 * @author Andrey Mamykin(mamykin_av)
 */
public class UpdateInfo {
    @SerializedName("date")
    private String date;
    @SerializedName("changes")
    private String chages;

    public UpdateInfo() {
    }

    public UpdateInfo(String date, String chages) {
        this.date = date;
        this.chages = chages;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChangelog() {
        return chages;
    }

    public void setChages(String chages) {
        this.chages = chages;
    }
}