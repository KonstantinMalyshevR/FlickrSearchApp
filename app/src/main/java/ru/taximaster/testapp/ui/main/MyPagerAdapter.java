package ru.taximaster.testapp.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import ru.taximaster.testapp.util.SupportClass;

/**
 * Created by Developer on 28.06.18.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private final SparseArray<WeakReference<MainFragment>> instantiatedFragments = new SparseArray<>();

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final MainFragment fragment = (MainFragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public MainFragment getFragment(final int position) {
        final WeakReference<MainFragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {

        MainFragment f = (MainFragment) object;
        f.update();

        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        return MainFragment.getNewInstance(position);
    }

    @Override
    public int getCount() {
        return SupportClass.PAGE_COUNT;
    }
}