package com.xdja.popwindowtext.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewUtils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 将Drawable转换为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }



    /**
     * 获取状态栏高度(不可在onCreate等类似界面初始化的地方使用)
     *
     * @param activity activity
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Activity activity) {
        //应用区域
        Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        return outRect1.top;
    }

    /**
     * 获取应用区域高度
     *
     * @param activity activity
     * @return 应用区域高度
     */
    public static int getAppLayoutHeight(Activity activity) {
        //应用区域
        Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        return outRect1.height();
    }


    /**
     * 获取listView高度
     *
     * @param listView 要获取高度的listView
     * @return listView的高度
     */
    public static int getListHeight(ListView listView) {
        if (listView == null) {
            return -1;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return -1;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }
}
