package ru.taximaster.testapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.taximaster.testapp.R;
import ru.taximaster.testapp.ui.map.MapActivity;
import ru.taximaster.testapp.util.PhotoMapClass;
import ru.taximaster.testapp.util.PhotoMapClassList;
import ru.taximaster.testapp.util.SupportClass;

public class MainActivity extends AppCompatActivity {

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
        MainFragment f = pagerAdapter.getFragment(viewPager.getCurrentItem());
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
}