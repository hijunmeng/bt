package com.junmeng.bt.activity;

import android.os.Bundle;
import android.os.Handler;

import com.junmeng.bt.R;
import com.junmeng.bt.base.BaseActivity;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoActivityAndFinish(ListActivity.class);
            }
        },1200);
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }
}
