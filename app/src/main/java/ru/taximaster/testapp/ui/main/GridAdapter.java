package ru.taximaster.testapp.ui.main;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.taximaster.testapp.R;
import ru.taximaster.testapp.util.PhotoMapClass;
import ru.taximaster.testapp.util.SupportClass;

/**
 * Created by Developer on 28.06.18.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolders> {

    private List<PhotoMapClass> objects;
    private ImageLoader imageLoader;
    private View.OnClickListener mClickListener;

    public GridAdapter(List<PhotoMapClass> objects) {
        this.objects = objects;
        imageLoader = ImageLoader.getInstance();
    }

    public void setItems(List<PhotoMapClass> objects) {
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

        holderF.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(position);
                mClickListener.onClick(v);
            }
        });
    }

    public class GridViewHolders extends RecyclerView.ViewHolder {

        @BindView(R.id.image) ImageView image;
        @BindView(R.id.progress) ProgressBar progress;

        private GridViewHolders(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return this.objects.size();
    }

    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }
}