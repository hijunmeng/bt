package com.junmeng.bt.app;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

import com.inuker.bluetooth.library.BluetoothClient;

public class MyApplication extends Application {
    public static BluetoothClient mClient;
    public  static BluetoothDevice mDevice;
    @Override
    public void onCreate() {
        super.onCreate();
        mClient = new BluetoothClient(this);
    }
}
