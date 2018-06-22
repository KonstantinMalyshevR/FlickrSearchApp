package ru.taximaster.testapp.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.taximaster.testapp.retrofit.FlickrResponseSinglePhoto;
import ru.taximaster.testapp.retrofit.ServiceApi;

/**
 * Created by Developer on 19.06.18.
 */

public class Controller {

    public static ServiceApi getApi() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SupportClass.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(ServiceApi.class);
    }

    public static String getPhotoUrl(FlickrResponseSinglePhoto photo) {

        int farm = photo.getFarm();
        String server = photo.getServer();
        String id = photo.getId();
        String secret = photo.getSecret();

        return "http://farm" + farm + ".static.flickr.com/"
                + server + "/" + id + "_" + secret + ".jpg";
    }
}