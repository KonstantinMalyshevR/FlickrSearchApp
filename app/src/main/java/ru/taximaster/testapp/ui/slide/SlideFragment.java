package ru.taximaster.testapp.ui.slide;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.taximaster.testapp.R;
import ru.taximaster.testapp.util.SupportClass;

/**
 * Created by Developer on 21.06.18.
 */

public class SlideFragment extends Fragment {

    protected ImageLoader imageLoader;

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.progress) ProgressBar progress;

    final static String PAGE_URL = "page_url";
    String url = "";

    public static SlideFragment getNewInstance(String url) {
        SlideFragment gf = new SlideFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_URL, url);
        gf.setArguments(args);
        return gf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = ImageLoader.getInstance();
        url = getArguments().getString(PAGE_URL);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, null);

        ButterKnife.bind(this, view);

        imageLoader.displayImage(url, imageView, SupportClass.displayImageOptions(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progress.setVisibility(View.VISIBLE);
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

                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                progress.setVisibility(View.GONE);
            }
        });

        return view;
    }
}