package com.ztech.lib;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class TypeValueUtil {

    public static float getPxByDp(float dp, Context context){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
     public static float getPxBySp(float sp, Context context){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,context.getResources().getDisplayMetrics());
    }
    /**
     * 获取x轴方向的px值
     * @param mm 毫米
     * @param context
     * @return px
     */
    public static float getPxByMmX(float mm, Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return mm*displayMetrics.xdpi/25.4f;
    }

    /**
     * 获取y轴方向的px值
     * @param mm 毫米
     * @param context
     * @return px
     */
    public static float getPxByMmY(float mm, Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return mm*displayMetrics.ydpi/25.4f;
    }
}
