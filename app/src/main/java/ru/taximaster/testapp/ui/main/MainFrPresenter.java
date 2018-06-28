package ru.taximaster.testapp.ui.main;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.taximaster.testapp.data.DataManager;
import ru.taximaster.testapp.data.ServiceApi;
import ru.taximaster.testapp.data.pojo.FlickrResponse;
import ru.taximaster.testapp.data.pojo.FlickrResponseSinglePhoto;
import ru.taximaster.testapp.data.pojo.GeoResponseImageLocation;
import ru.taximaster.testapp.ui.base.BasePresenter;
import ru.taximaster.testapp.util.PhotoMapClass;
import ru.taximaster.testapp.util.SupportClass;

/**
 * Created by Developer on 28.06.18.
 */

public class MainFrPresenter extends BasePresenter<MainFrMvpView> {

    private Disposable mDisposable;
    private List<PhotoMapClass> list_objects;
    private int pageNumber;

    public MainFrPresenter(int pageNumber) {
        this.pageNumber = pageNumber;

        list_objects = new ArrayList<>();
    }

    @Override
    public void attachView(MainFrMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public List<PhotoMapClass> getList_objects() {
        return list_objects;
    }

    public void downloadPage(String text){

        checkViewAttached();

        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

        list_objects.clear();

        getMvpView().setProgress(true);

        ServiceApi serviceApi = DataManager.getApi();

        Observable<FlickrResponse> searchPhotos = serviceApi.searchPhotos(SupportClass.KEY, "relevance", "1", SupportClass.PER_PAGE_COUNT, pageNumber + 1, 1, "photos", "json", "1", text);

        searchPhotos
                .map(flickrResponse -> flickrResponse.getPhotos())
                .map(photos -> photos.getPhoto())
                .flatMapIterable(photo -> photo)
                .flatMap(photo -> serviceApi.getPhotoLocaton(SupportClass.KEY, photo.getId(),"json", "1"),
                        (photo, geoResponse) -> Observable.just(
                                toPhotoMapClass(photo, getPhotoUrl(photo), geoResponse.getImageLocation().getLocation())))
                .flatMap(pMC -> pMC)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private String getPhotoUrl(FlickrResponseSinglePhoto photo) {

        int farm = photo.getFarm();
        String server = photo.getServer();
        String id = photo.getId();
        String secret = photo.getSecret();

        return "http://farm" + farm + ".static.flickr.com/"
                + server + "/" + id + "_" + secret + ".jpg";
    }

    private PhotoMapClass toPhotoMapClass(FlickrResponseSinglePhoto photo, String url, GeoResponseImageLocation location){
        PhotoMapClass photoMapClass = new PhotoMapClass();
        photoMapClass.setId(photo.getId());
        photoMapClass.setTitle(photo.getTitle());
        photoMapClass.setGeo(new LatLng(location.getLatitude(), location.getLongitude()));
        photoMapClass.setUrl(url);
        return photoMapClass;
    }

    private Observer<PhotoMapClass> observer = new Observer<PhotoMapClass>() {
        @Override
        public void onSubscribe(Disposable d) {
            mDisposable = d;
        }

        @Override
        public void onNext(PhotoMapClass url) {
            list_objects.add(url);
        }

        @Override
        public void onError(Throwable e) {
            getMvpView().setProgress(false);
            getMvpView().showToastMessage("Что-то пошло не так=( " + e.getMessage());
        }

        @Override
        public void onComplete() {
            getMvpView().setProgress(false);
            getMvpView().setItems(list_objects);
        }
    };
}