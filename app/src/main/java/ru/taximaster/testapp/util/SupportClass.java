package ru.taximaster.testapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import ru.taximaster.testapp.R;

/**
 * Created by Developer on 19.06.18.
 */

public class SupportClass {

    public final static String KEY = "6995ead0742e64add6246518afccc6c2";

    public final static int PAGE_COUNT = 5;
    public final static int PER_PAGE_COUNT = 20;

    public static void ToastMessage(Context context, String value){
        Toast toast = Toast.makeText(context, value, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String checkStringNullAndTrim(String value){
        if(value != null){
            return value.trim();
        }else{
            return "";
        }
    }

    public static DisplayImageOptions displayImageOptions(){
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(800))
                .build();
    }

    public static void hideKeyboard(Context context, IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}