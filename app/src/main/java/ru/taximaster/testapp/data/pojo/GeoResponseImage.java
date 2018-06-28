package ru.taximaster.testapp.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Developer on 21.06.18.
 */

public class GeoResponseImage {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("location")
    @Expose
    private GeoResponseImageLocation location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeoResponseImageLocation getLocation() {
        return location;
    }

    public void setLocation(GeoResponseImageLocation location) {
        this.location = location;
    }
}
