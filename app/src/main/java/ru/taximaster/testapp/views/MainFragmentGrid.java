package ru.taximaster.testapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.taximaster.testapp.R;
import ru.taximaster.testapp.model.Controller;
import ru.taximaster.testapp.model.PhotoMapClassList;
import ru.taximaster.testapp.model.PhotoMapClass;
import ru.taximaster.testapp.model.SupportClass;
import ru.taximaster.testapp.retrofit.FlickrResponse;
import ru.taximaster.testapp.retrofit.GeoResponseImageLocation;
import ru.taximaster.testapp.retrofit.FlickrResponseSinglePhoto;
import ru.taximaster.testapp.retrofit.ServiceApi;


public class MainFragmentGrid extends Fragment implements MainActivity.Updateable{

    protected ImageLoader imageLoader;
    private int bkgColor;
    private int pageNumber;

    private List<PhotoMapClass> list_objects;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progess_anim) ImageView progess_anim;

    final static String PAGE_NUMBER = "page_number";
    public GridAdapter adapter;

    public static MainFragmentGrid getNewInstance(int page) {
        MainFragmentGrid gf = new MainFragmentGrid();
        Bundle args = new Bundle();
        args.putInt(PAGE_NUMBER, page);
        gf.setArguments(args);
        return gf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = ImageLoader.getInstance();
        pageNumber = getArguments().getInt(PAGE_NUMBER);

        list_objects = new ArrayList<>();

        Random rnd = new Random();
        bkgColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, null);

        ButterKnife.bind(this, view);

        view.setBackgroundColor(bkgColor);

        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        List<PhotoMapClass> class_object = new ArrayList<>();
        adapter = new GridAdapter(class_object);
        recyclerView.setAdapter(adapter);

        downloadPage();

        return view;
    }

    public List<PhotoMapClass> getList_objects() {
        return list_objects;
    }

    @Override
    public void update() {
        adapter.setItems(new ArrayList<>());
        adapter.notifyDataSetChanged();
        downloadPage();
    }

    private void downloadPage(){

        list_objects.clear();

        setProgress(true);

        String text;
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            text = mainActivity.search_text;
        }else{
            text = "";
        }

        ServiceApi serviceApi = Controller.getApi();

        Observable<FlickrResponse> searchPhotos = serviceApi.searchPhotos(SupportClass.KEY, "relevance", "1", SupportClass.PER_PAGE_COUNT, pageNumber + 1, 1, "photos", "json", "1", text);

        searchPhotos
                .map(flickrResponse -> flickrResponse.getPhotos())
                .map(photos -> photos.getPhoto())
                .flatMapIterable(photo -> photo)
                .flatMap(photo -> serviceApi.getPhotoLocaton(SupportClass.KEY, photo.getId(),"json", "1"),
                        (photo, geoResponse) -> Observable.just(
                                toPhotoMapClass(photo, Controller.getPhotoUrl(photo), geoResponse.getImageLocation().getLocation())))
                .flatMap(pMC -> pMC)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
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
        }

        @Override
        public void onNext(PhotoMapClass url) {
            list_objects.add(url);
        }

        @Override
        public void onError(Throwable e) {
            setProgress(false);
            SupportClass.ToastMessage(getActivity(), "Что-то пошло не так=( " + e.getMessage());
        }

        @Override
        public void onComplete() {
            setProgress(false);

            adapter.setItems(list_objects);
            adapter.notifyDataSetChanged();
        }
    };

    private void setProgress(Boolean value){
        if (value){
            progess_anim.setVisibility(View.VISIBLE);

            YoYo.with(Techniques.Pulse)
                    .duration(800)
                    .repeat(10)
                    .pivot(50f, 50f)
                    .playOn(progess_anim);

        }else{
            progess_anim.setVisibility(View.GONE);
        }
    }

    private class GridAdapter extends RecyclerView.Adapter<GridViewHolders> {

        List<PhotoMapClass> objects;

        private GridAdapter(List<PhotoMapClass> objects) {
            this.objects = objects;
        }

        private void setItems(List<PhotoMapClass> objects) {
            this.objects = objects;
        }

        @NonNull
        @Override
        public GridViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
            return new GridViewHolders(layoutView);
        }

        @Override
        public void onBindViewHolder(@NonNull GridViewHolders holder, int position) {
            final GridViewHolders holderF = holder;

            String url = objects.get(position).getUrl();

            imageLoader.displayImage(url, holderF.image, SupportClass.displayImageOptions(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holderF.progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            break;
                        case DECODING_ERROR:
                            break;
                        case NETWORK_DENIED:
                            break;
                        case OUT_OF_MEMORY:
                            break;
                        case UNKNOWN:
                            break;
                    }

                    holderF.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    holderF.progress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holderF.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    holderF.progress.setVisibility(View.GONE);
                }
            });

            holderF.image.setOnClickListener(t -> tapToImage(position));
        }

        @Override
        public int getItemCount() {
            return this.objects.size();
        }
    }

    private void tapToImage(int position){
        Intent intent = new Intent(getActivity(), ViewActivity.class);

        Gson gson = new Gson();
        PhotoMapClassList list = new PhotoMapClassList();
        list.setList(list_objects);
        String str = gson.toJson(list);
        intent.putExtra("list_objects", str);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private class GridViewHolders extends RecyclerView.ViewHolder {

        public ImageView image;
        public ProgressBar progress;

        private GridViewHolders(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            progress = itemView.findViewById(R.id.progress);
        }
    }
}