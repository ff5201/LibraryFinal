package com.example.fjh.libraryfinal.Tools;

import android.content.Context;
import android.util.Log;

/**
 * Created by FJH on 2017/5/19.
 */

public class DensityUtil {
    /**
     * 获取屏幕高
     * @param context
     * @return
     */
    public static final float getHeightInPx(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        Log.d("像素",String.valueOf(height));
        return height;
    }

    /**
     * 获取屏幕宽
     * @param context
     * @return
     */
    public static final float getWidthInPx(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
