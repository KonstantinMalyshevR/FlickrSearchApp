package ru.taximaster.testapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Developer on 21.06.18.
 */

public class GeoResponse {
    @SerializedName("photo")
    @Expose
    private GeoResponseImage imageLocation;

    @SerializedName("stat")
    @Expose
    private String stat;

    public GeoResponseImage getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(GeoResponseImage imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
