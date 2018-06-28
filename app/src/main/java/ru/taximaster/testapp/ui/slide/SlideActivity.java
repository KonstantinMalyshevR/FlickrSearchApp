package ru.taximaster.testapp.ui.slide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.taximaster.testapp.R;
import ru.taximaster.testapp.util.PhotoMapClassList;
import ru.taximaster.testapp.util.PhotoMapClass;

/**
 * Created by Developer on 21.06.18.
 */

public class SlideActivity extends AppCompatActivity {

    @BindView(R.id.viewPager) ViewPager viewPager;

    List<PhotoMapClass> objects_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        String str = intent.getStringExtra("list_objects");

        Gson gson = new Gson();
        PhotoMapClassList list = gson.fromJson(str, PhotoMapClassList.class);
        objects_url = list.getList();

        int position = intent.getIntExtra("position", 0);

        MyViewsAdapter pagerAdapter = new MyViewsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.setCurrentItem(position);
    }

    /**
     * Adapter
     */
    public class MyViewsAdapter extends FragmentStatePagerAdapter {
        public MyViewsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {

            return SlideFragment.getNewInstance(objects_url.get(position).getUrl());
        }

        @Override
        public int getCount() {
            return objects_url.size();
        }
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