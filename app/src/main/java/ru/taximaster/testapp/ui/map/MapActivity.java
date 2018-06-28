package ru.taximaster.testapp.ui.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.taximaster.testapp.R;
import ru.taximaster.testapp.util.PhotoMapClass;
import ru.taximaster.testapp.util.PhotoMapClassList;
import ru.taximaster.testapp.util.SupportClass;

//Created by Developer on 14.03.18.

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;

    List<PhotoMapClass> list_objects;

    @BindView(R.id.org_image) ImageView image;
    @BindView(R.id.org_progress) ProgressBar progress;
    @BindView(R.id.name_text) TextView title;

    protected ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageLoader = ImageLoader.getInstance();

        Intent intent = getIntent();
        String str = intent.getStringExtra("list_objects");

        Gson gson = new Gson();
        PhotoMapClassList list = gson.fromJson(str, PhotoMapClassList.class);
        list_objects = list.getList();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);

        title.setText("");
        image.setImageResource(R.mipmap.ic_launcher);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        LatLng geopoint_user = new LatLng(64.739143, 100.414191);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(geopoint_user, 1);
        map.animateCamera(cameraUpdate);

        map.clear();

        if (list_objects.size() > 0) {

            for (int i = 0; i < list_objects.size(); i++) {

                PhotoMapClass object = list_objects.get(i);

                LatLng geopoint_str = new LatLng(object.getGeo().latitude, object.getGeo().longitude);

                BitmapDescriptor icon_driver = BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(object.getTitle()));

                MarkerOptions markerOptions = new MarkerOptions().position(geopoint_str)
                        .icon(icon_driver);
                map.addMarker(markerOptions).setTag(object);

                CircleOptions circleOptions = new CircleOptions()
                        .center(geopoint_str)
                        .strokeColor(ContextCompat.getColor(MapActivity.this, R.color.color_blue))
                        .fillColor(ContextCompat.getColor(MapActivity.this, R.color.color_blue))
                        .strokeWidth(1)
                        .radius(10);

                map.addCircle(circleOptions);

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        PhotoMapClass object = (PhotoMapClass) marker.getTag();

                        if(object != null){

                            title.setText(SupportClass.checkStringNullAndTrim(object.getTitle()));

                            imageLoader.displayImage(object.getUrl(), image);

                        }
                        return true;
                    }
                });
            }
        }
    }

    //============
    //Custom Marker View
    private Bitmap getMarkerBitmapFromView(String title) {

        View mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);

        ImageView mMarkerImageView = mCustomMarkerView.findViewById(R.id.profile_image);
        TextView text = mCustomMarkerView.findViewById(R.id.text);

        mMarkerImageView.setImageResource(R.drawable.marker24);
        text.setText(title);

        mCustomMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mCustomMarkerView.layout(0, 0, mCustomMarkerView.getMeasuredWidth(), mCustomMarkerView.getMeasuredHeight());
        mCustomMarkerView.buildDrawingCache();

        Bitmap returnedBitmap = Bitmap.createBitmap(mCustomMarkerView.getMeasuredWidth(), mCustomMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = mCustomMarkerView.getBackground();

        if (drawable != null)
            drawable.draw(canvas);
        mCustomMarkerView.draw(canvas);

        return returnedBitmap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}