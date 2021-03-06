package com.luna1970.qingmumusic.util;

import android.content.Context;
import android.widget.Toast;

import com.luna1970.qingmumusic.application.MusicApplication;

/**
 * Toast工具类
 *
 */

public class ToastUtils {
    private static Toast toast;
    private static Context context;
    static {
        context = MusicApplication.getInstance().getApplicationContext();
    }
    public static Toast makeText(CharSequence charSequence) {
        if (toast==null) {
            toast = Toast.makeText(context, charSequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(charSequence);
        }
        return toast;
    }
    public static Toast makeText(int resId) {
        String content = context.getString(resId);
        return makeText(content);
    }

    public static void show(CharSequence charSequence) {
        makeText(charSequence).show();
    }

    public static void show(int resId) {
        makeText(resId).show();
    }
    public static void cancel() {
        toast.cancel();
    }
}
