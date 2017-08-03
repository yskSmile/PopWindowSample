package com.xdja.popwindowtext.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xdja.popwindowtext.R;
import com.xdja.popwindowtext.utils.ViewUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        取消状态栏 需要设置在setContentView之前
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //隐藏toolbar
//        if(getSupportActionBar()!=null){
//            getSupportActionBar().hide();
//        }
        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startActivity(new Intent(this, ShowPopWindowActivity.class));
                break;
            case R.id.button2:
                //获取之下两个高度时需要在初始化完成之后 否则会返回0
                int appLayoutHeight = ViewUtils.getAppLayoutHeight(this);
                Log.d("ysk", "appLayoutHeight："+appLayoutHeight);
                int statusBarHeight = ViewUtils.getStatusBarHeight(this);
                Log.d("ysk", "statusBarHeight："+statusBarHeight);
                break;
            default:
                break;
        }
    }
}
