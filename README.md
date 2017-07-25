# 使用popwindow实现根据界面可视区域大小自动改变popwindow高度

 前沿：
 最近在用popwindow时，使用showAsDropDown显示列表数据时出现了方法设置后出现内容错乱的问题。如下图
  ![image](https://github.com/yskSmile/PopWindowSample/blob/master/file/1.png)
 
 然后查资料发现此方法在Android7.0以下都没有问题，但在android 7.0上，用showAsDropDown（）在popupwindow为全屏时，会有弹出位置异常情况，需用showAtLocation（）才能正常显示：
    int[] location = new int[2];
    line.getLocationOnScreen(location);
    accountPopWindow.showAtLocation(line, Gravity.TOP | Gravity.START, location[0], location[1] + 5);
其中line为一个基线（需要显示位置的view）。
然后自己在使用邮箱大师时发现添加账号的账号列表效果不错，具体就是当软键盘存在时，下方listview显示宽度最大到软键盘之上，如果软键盘关闭，listview的高度就会重新变成基线一下高度。就查资料做了该效果。
具体实现是下方这段代码
        View view = baseLine;
        int[] mLocation = new int[2];//坐标
        view.getLocationOnScreen(mLocation);
        //获取应用可见的区域高度 不包括状态栏
        int appLayoutHeight = ViewUtils.getAppLayoutHeight(this);
        //获取状态栏高度
        int statusBarHeight = ViewUtils.getStatusBarHeight(this);

        //显示基线以下高度 -11是 线有1dp popWindow显示在基线以下10dp
        int height = appLayoutHeight + statusBarHeight - mLocation[1] - 11;
        //计算listView高度
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
        int paddingSum = listView.getPaddingTop() + listView.getPaddingBottom();
        int marginSum = layoutParams.topMargin + layoutParams.bottomMargin;
        int listHeight = ViewUtils.getListHeight(listView) + paddingSum + marginSum;
        //listView高度小于height高度 展示高度是listview高度 相反为可见高度
        int canShowHeight = height > listHeight ? listHeight : height;
        //若可见高度大于800 则只显示800
        if (canShowHeight > 800) {
            canShowHeight = 800;
        }
        if (popupWindow.isShowing()) {
            //更新popWindow高度  （mLocation[1]+10是 基线以下10像素）
            popupWindow.update(mLocation[0], mLocation[1] + 10, LinearLayout.LayoutParams.MATCH_PARENT, canShowHeight);
        } else {
            popupWindow.setHeight(canShowHeight);
            popupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, mLocation[0], mLocation[1] + 10);
        }
        
   我们可以获取一切我们想要的高度，然后就进行拼装设置就可以得到我们需要显示的popwindow的高度，然后popwindow在显示的时候可以调用更新方法，这样就需要重新生成popwindow。
   
  说到这里就完了么，答案是没有。这里只是显示时的设置。这样在布局改变时也不会通知我们布局的高度需要改变。（不如软件盘收起时）这里我们要是要到一个监听，监听当前界面是否变化，那就是OnLayoutChangeListener，当布局变化时就要调用这个接口的方法。我们在这个接口里重新计算popwindow需要显示的高度就好了。
  
  到这里基本上就大功告成了。
  
  感兴趣的可以下载demo了解相关具体实现。
