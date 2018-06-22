package ru.taximaster.testapp.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Developer on 22.06.18.
 */

public class PhotoMapClass {
    
    private String id;
    private String url;
    private String title;
    private LatLng geo;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setGeo(LatLng geo) {
        this.geo = geo;
    }

    public LatLng getGeo() {
        return geo;
    }
}