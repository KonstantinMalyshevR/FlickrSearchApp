package ru.taximaster.testapp.ui.main;

import java.util.List;

import ru.taximaster.testapp.ui.base.MvpView;
import ru.taximaster.testapp.util.PhotoMapClass;

/**
 * Created by Developer on 28.06.18.
 */

public interface MainFrMvpView extends MvpView {

    void setProgress(Boolean value);

    void setItems(List<PhotoMapClass> list);

    void showToastMessage(String message);
}