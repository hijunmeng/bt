package com.junmeng.bt.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.junmeng.bt.R;
import com.junmeng.bt.adapter.BluetoothListAdapter;
import com.junmeng.bt.app.Constants;
import com.junmeng.bt.app.MyApplication;
import com.junmeng.bt.base.BaseActivity;
import com.junmeng.bt.util.SpUtils;

import java.util.Set;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

public class ListActivity extends BaseActivity {
    private static final String TAG = "ListActivity";
    private RecyclerView recyclerView;
    private BluetoothListAdapter adapter;
    private BluetoothAdapter bluetoothAdapter;
    private Button scanBtn;
    private Button stopBtn;

    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            showToast(openOrClosed ? "蓝牙已打开" : "蓝牙已关闭");
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initViews();
        initRecycleView();

        if(!MyApplication.mClient.isBluetoothOpened()){
            MyApplication.mClient.openBluetooth();
        }
        MyApplication.mClient.registerBluetoothStateListener(mBluetoothStateListener);

        bluetoothAdapter = BluetoothUtils.getBluetoothAdapter();

        checkExistDevice();
    }

    /**
     * 检查已经成功连接过的设备，如果有并且配对过则直接进行连接
     */
    private void checkExistDevice(){

        String address=SpUtils.getString(this,Constants.BL_DEVICE_ADDRESS,"");
        if(TextUtils.isEmpty(address)){
           return ;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (address.equals(device.getAddress())){
                    handleConnectSuccess(device);
                }
                break;
            }
        }

    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new BluetoothListAdapter(this));
        adapter.setOnItemClickListener(new BluetoothListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, final SearchResult device, int position) {
                onClickStopScan(null);
                showLoading();
                MyApplication.mClient.connect(device.getAddress(), new BleConnectResponse() {
                    @Override
                    public void onResponse(int code, BleGattProfile data) {
                        dismissLoading();
                        if (code == REQUEST_SUCCESS) {
                            showToast("连接成功");
                            handleConnectSuccess(device.device);
                        }else{
                            showToast("连接失败");
                        }
                    }
                });
            }
        });
    }

    private void handleConnectSuccess(BluetoothDevice device){
        MyApplication.mDevice = device;
        SpUtils.putString(ListActivity.this,Constants.BL_DEVICE_ADDRESS,device.getAddress());
        gotoActivity(MainActivity.class);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_bluetooth);
        stopBtn = findViewById(R.id.btn_stop);
        scanBtn = findViewById(R.id.btn_scan);
    }

    /**
     * 开始扫描
     *
     * @param view
     */
    public void onClickScan(View view) {
        adapter.getList().clear();
        adapter.notifyDataSetChanged();
        SearchRequest request = new SearchRequest.Builder()
                //.searchBluetoothLeDevice(5000, 3)  // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(10000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(10000)      // 再扫BLE设备2s
                .build();

        MyApplication.mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                scanBtn.setEnabled(false);
            }

            @Override
            public void onDeviceFounded(final SearchResult device) {
                // Beacon beacon = new Beacon(device.scanRecord);
                //  BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));

                Log.i(TAG, "onDeviceFounded: "+device.getName()+" "+device.getAddress());
                adapter.getList().add(device);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSearchStopped() {
                scanBtn.setEnabled(true);
            }

            @Override
            public void onSearchCanceled() {
                scanBtn.setEnabled(true);
            }
        });

    }

    /**
     * 停止扫描
     *
     * @param view
     */
    public void onClickStopScan(View view) {
        MyApplication.mClient.stopSearch();
    }


}
