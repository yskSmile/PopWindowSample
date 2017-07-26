# 关于popWindow使用时的一些发现

## 前言
 最近在使用popWindow显示列表时出现一些问题 就深入研究研究了一下。
 
问题：android7.0出现的popWindow中showAsDropDown在popWindow全屏显示时方法无效。

 关于这个问题我们可以使用popWindow的另一个方法 showAsLocation来实现 具体下面也会说到。
另外在使用网易邮箱大师时感觉它的添加账号显示下拉列表效果不错，就研究了一下。效果为popWindow的高度可根据
视图的可视区域的变化而变化 然后就试着做了一下。

## 实现方案

1.实现popWindow列表

2.由于当软件盘显示时，当前界面的可视区域是不包含软键盘部分的，所以需要计算当前popWindow可以显示的高度

3.当界面布局改变时能够动态更新popWindow的显示高度

## 我的思路：

 我们主要实现上面所说的后两部即可。第二步我们需要计算popWindow需要显示的高度，我们都知道popWindow是需要基于一个view（最好是一条线）显示的
然后我们就需要计算这个view的低端距可视区域之下的高度 然后将popWindow的高度设置成这个高度即可需要计算这个高度,我们需要几个知识点。计算界面中可视区域的高度 计算顶端状态栏高度 计算popWindow中控件的高度以及toolbar的高度 这样我们通过拼装即可得到我们想要的高度

## 具体实现

1.获取界面中可视区域的高度（该区域包括toolbar但不包括状态栏）

     Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
                return outRect1.height();
                
2.获取状态栏高度

     Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
                return outRect1.top;
                
3.获取toolbar高度

    getSupportActionBar().getHeight();

4.关于popWindow中控件的高度 这里我们大多使用的listView 计算listview高度
 基本上就是每个item高度相加以及listview的padding和margin值
 
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
            totalHeight += listItem.getHeight();//这里使用getHeight获取结果不对
        totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        
5.获取基线view的位置

    View view = baseLine;
    int[] mLocation = new int[2];//坐标
        view.getLocationOnScreen(mLocation);
        
最后我们拼装一下这些高度即可。可显示的最大高度=界面可视区域高度+状态栏高度-基线view的高度
之后如果popWindow中控件的高度小于可显示的最大高度，则显示控件高度，否则显示最大高度。到这里我们第二步就基本完成了
接下来当界面改变时我们需要需要重新设置listview的高度并显示。这时我们需要实现一个view的接口View.OnLayoutChangeListener
每次界面改变时都会调用他的实现方法。然后在方法中重新设置popWindow高度 更新或显示即可。

有兴趣可以下载demo研究。
