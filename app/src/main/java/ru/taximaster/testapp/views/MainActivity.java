package ru.taximaster.testapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.taximaster.testapp.R;
import ru.taximaster.testapp.model.PhotoMapClass;
import ru.taximaster.testapp.model.PhotoMapClassList;
import ru.taximaster.testapp.model.SupportClass;

public class MainActivity extends AppCompatActivity {//implements MainFragmentGrid.OnFragmentInteractionListener {

    @BindView(R.id.button) Button button;
    @BindView(R.id.editText) EditText editText;
    @BindView(R.id.viewPager) ViewPager viewPager;

    MyPagerAdapter pagerAdapter;

    public String search_text = "";

    public interface Updateable {
        void update();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SupportClass.hideKeyboard(this, editText.getWindowToken());
    }

    @OnClick(R.id.map_text)
    public void onMap_textClick(){
        MainFragmentGrid f = pagerAdapter.getFragment(viewPager.getCurrentItem());
        if(f != null && f.getList_objects() != null){
            List<PhotoMapClass> objects = f.getList_objects();

            Intent intent = new Intent(MainActivity.this, MapActivity.class);

            Gson gson = new Gson();
            PhotoMapClassList list = new PhotoMapClassList();
            list.setList(objects);
            String str = gson.toJson(list);
            intent.putExtra("list_objects", str);
            startActivity(intent);
        }
    }

    @OnClick(R.id.button)
    public void onButtonClick() {
        SupportClass.hideKeyboard(this, findViewById(android.R.id.content).getWindowToken());
        search_text = SupportClass.checkStringNullAndTrim(editText.getText().toString());

        viewPager.setCurrentItem(0);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    /**
     * Adapter
     */
    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final SparseArray<WeakReference<MainFragmentGrid>> instantiatedFragments = new SparseArray<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final MainFragmentGrid fragment = (MainFragmentGrid) super.instantiateItem(container, position);
            instantiatedFragments.put(position, new WeakReference<>(fragment));
            return fragment;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            instantiatedFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Nullable
        public MainFragmentGrid getFragment(final int position) {
            final WeakReference<MainFragmentGrid> wr = instantiatedFragments.get(position);
            if (wr != null) {
                return wr.get();
            } else {
                return null;
            }
        }

        @Override
        public int getItemPosition(@NonNull Object object) {

            MainFragmentGrid f = (MainFragmentGrid) object;
            f.update();

            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            return MainFragmentGrid.getNewInstance(position);
        }

        @Override
        public int getCount() {
            return SupportClass.PAGE_COUNT;
        }
    }
}