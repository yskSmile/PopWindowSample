package com.xdja.popwindowtext.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewUtils {
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
//            totalHeight += listItem.getHeight();//这里使用getHeight获取结果不对
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }
}
