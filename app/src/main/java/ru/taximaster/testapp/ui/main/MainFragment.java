package ru.taximaster.testapp.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.taximaster.testapp.R;
import ru.taximaster.testapp.ui.slide.SlideActivity;
import ru.taximaster.testapp.util.PhotoMapClass;
import ru.taximaster.testapp.util.PhotoMapClassList;
import ru.taximaster.testapp.util.SupportClass;

public class MainFragment extends Fragment implements MainActivity.Updateable, MainFrMvpView{

    MainFrPresenter mainPresenter;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progess_anim)
    ProgressBar progess_anim;

    final static String PAGE_NUMBER = "page_number";
    public GridAdapter adapter;

    public static MainFragment getNewInstance(int page) {
        MainFragment gf = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_NUMBER, page);
        gf.setArguments(args);
        return gf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            int pageNumber = getArguments().getInt(PAGE_NUMBER);
            mainPresenter = new MainFrPresenter(pageNumber);
            mainPresenter.attachView(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        ButterKnife.bind(this, view);

        Random rnd = new Random();
        int bkgColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        view.setBackgroundColor(bkgColor);

        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        List<PhotoMapClass> class_object = new ArrayList<>();
        adapter = new GridAdapter(class_object);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer position = (Integer) v.getTag();

                Intent intent = new Intent(getActivity(), SlideActivity.class);

                Gson gson = new Gson();
                PhotoMapClassList list = new PhotoMapClassList();
                list.setList(mainPresenter.getList_objects());
                String str = gson.toJson(list);
                intent.putExtra("list_objects", str);
                intent.putExtra("position", position.intValue());
                startActivity(intent);
            }
        });

        download();

        return view;
    }

    private void download(){
        String text;
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            text = mainActivity.search_text;
        }else{
            text = "";
        }

        mainPresenter.downloadPage(text);
    }

    public List<PhotoMapClass> getList_objects() {
        return mainPresenter.getList_objects();
    }

    /***** MainActivity.Updateable methods implementation *****/
    @Override
    public void update() {
        adapter.setItems(new ArrayList<>());
        adapter.notifyDataSetChanged();
        download();
    }

    /***** MVP View methods implementation *****/
    @Override
    public void setProgress(Boolean value){
        if (value){
            progess_anim.setVisibility(View.VISIBLE);

        }else{
            progess_anim.setVisibility(View.GONE);
        }
    }

    @Override
    public void setItems(List<PhotoMapClass> list){
        adapter.setItems(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showToastMessage(String message){
        SupportClass.ToastMessage(getActivity(), message);
    }
}