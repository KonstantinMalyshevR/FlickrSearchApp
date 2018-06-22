package ru.taximaster.testapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Developer on 19.06.18.
 */

public class FlickrResponse {

    @SerializedName("photos")
    @Expose
    private FlickrResponsePhotos photos;

    @SerializedName("stat")
    @Expose
    private String stat;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("code")
    @Expose
    private String code;

    public FlickrResponsePhotos getPhotos() {
        return photos;
    }

    public String getStat() {
        return stat;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
