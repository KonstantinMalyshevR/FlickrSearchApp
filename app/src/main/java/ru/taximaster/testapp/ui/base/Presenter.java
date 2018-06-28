package ru.taximaster.testapp.ui.base;

/**
 * Created by Developer on 27.06.18.
 */

public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}