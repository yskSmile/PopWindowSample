package com.xdja.popwindowtext.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.xdja.popwindowtext.R;
import com.xdja.popwindowtext.adapter.MailAutoCompleteAdapter;
import com.xdja.popwindowtext.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Summary:</p>
 * <p>Description:</p>
 * <p>Package:com.xdja.popwindowtext</p>
 * <p>Author:yusenkui</p>
 * <p>Date:2017/7/25</p>
 * <p>Time:11:22</p>
 */


public class ShowPopWindowActivity extends AppCompatActivity implements View.OnLayoutChangeListener, View.OnClickListener {


    private PopupWindow popupWindow;
    private MailAutoCompleteAdapter adapter;
    private EditText editText;
    private View baseLine;
    private ListView listView;
    private Button button3, button4, button5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText = (EditText) findViewById(R.id.editText);
        baseLine = findViewById(R.id.baseView);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) ShowPopWindowActivity.this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (v instanceof EditText) {
                        EditText editText = (EditText) v;
                        String input = editText.getText().toString();
                        if (!TextUtils.isEmpty(input)) {
                            //若输入不为空
                            editText.setText(input);
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                            }
                            editText.setFocusable(false);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (TextUtils.isEmpty(input)) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                } else {
                    getAutoTextByInput(input);
                }


            }
        });
        button3.setText("改变数据源");
        button3.setOnClickListener(this);
        button4.setText(R.string.text_drop_down);
        button4.setOnClickListener(this);
        button5.setText(R.string.text_as_location);
        button5.setOnClickListener(this);
        this.getWindow().getDecorView().addOnLayoutChangeListener(this);
    }

    private void getAutoTextByInput(String input) {
        List<String> shortNames = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            shortNames.add(input);
        }
        showPopupWindow(this, shortNames, true);
    }

    private void showPopupWindow(Context context, List<String> shortNames, boolean isShowAsLocation) {

        if (popupWindow == null) {
            if (shortNames.size() != 0) {
                View inflate = LayoutInflater.from(context.getApplicationContext())
                        .inflate(R.layout.layout_popwindow, null);
                popupWindow = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, false);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                listView = (ListView) inflate.findViewById(R.id.popWindow_list);
                adapter = new MailAutoCompleteAdapter(shortNames, context.getApplicationContext());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new DropDownListOnItemClickListener());
                if (isShowAsLocation) {
                    setHeightAndShowPopupWindow();
                } else {
                    popupWindow.showAsDropDown(baseLine);
                }
            }
        } else {
            if (shortNames.size() == 0) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            } else {
                adapter.updateListData(shortNames);
                if (isShowAsLocation) {
                    setHeightAndShowPopupWindow();
                } else {
                    popupWindow.showAsDropDown(baseLine);
                }
            }
        }
    }

    /**
     * 设置高度并且显示PopupWindow
     *
     * @param
     */
    public void setHeightAndShowPopupWindow() {
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
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (popupWindow != null && popupWindow.isShowing()) {
            setHeightAndShowPopupWindow();
        }
    }

    @Override
    public void onClick(View v) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add(i + "");
        }
        switch (v.getId()) {
            case R.id.button3:
                List<String> shortNames = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    shortNames.add(i + "123");
                }
                showPopupWindow(ShowPopWindowActivity.this, shortNames, true);
                break;
            case R.id.button4:
                showPopupWindow(this, strings, false);
                break;
            case R.id.button5:
                showPopupWindow(this, strings, true);
                break;
        }
    }

    private class DropDownListOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapter != null) {
                editText.setText(adapter.getItem(i));
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = editText.getText();
                if (charSequence != null) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        }

    }
}
