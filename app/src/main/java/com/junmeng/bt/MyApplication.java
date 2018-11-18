package com.junmeng.bt;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

import com.inuker.bluetooth.library.BluetoothClient;

public class MyApplication extends Application {
    static BluetoothClient mClient;
    static BluetoothDevice mDevice;
    @Override
    public void onCreate() {
        super.onCreate();
        mClient = new BluetoothClient(this);
    }
}
