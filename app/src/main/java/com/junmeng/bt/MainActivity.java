package com.junmeng.bt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static android.bluetooth.BluetoothDevice.BOND_BONDING;
import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.junmeng.bt.MyApplication.mClient;

/**
 * https://blog.csdn.net/Fight_0513/article/details/79855749
 */
public class MainActivity extends BaseActivity {

    BluetoothSocket mSocket = null;

    private Button connectBtn;
    private Button catBtn;
    private Button dogBtn;
    private Button pigBtn;
    private Button sheepBtn;
    private TextView statusView;

    private boolean isConnectSuccess=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        connectService();
    }

    private void initViews() {
        statusView = findViewById(R.id.tv_status);
        // text.setMovementMethod(new ScrollingMovementMethod());
        connectBtn = findViewById(R.id.btn_connect);
        catBtn = findViewById(R.id.btn_cat);
        dogBtn = findViewById(R.id.btn_dog);
        pigBtn = findViewById(R.id.btn_pig);
        sheepBtn = findViewById(R.id.btn_sheep);
    }

    public void onClickConnect(View view) {
        connectService();
    }

    public void onClickCat(View view) {
        if(isConnectSuccess){
            sendCmd(5);
        }

    }


    public void onClickDog(View view) {
        if(isConnectSuccess){
            sendCmd(6);
        }
    }

    public void onClickPig(View view) {
        if(isConnectSuccess){
            sendCmd(7);
        }
    }

    public void onClickSheep(View view) {
        if(isConnectSuccess){
            sendCmd(8);
        }
    }


    public void connectService() {
       if(mSocket!=null&&mSocket.isConnected()){
           statusView.setText("连接成功");
           isConnectSuccess=true;
          return ;
       }
        try {
            showLoading();
            mSocket = MyApplication.mDevice.createRfcommSocketToServiceRecord(UUID.fromString("11101101-0000-1000-8000-00805F9B34FB"));
            mSocket.connect();
            statusView.setText("连接成功");
            isConnectSuccess=true;
        } catch (IOException e) {
            e.printStackTrace();
            statusView.setText("连接失败");
            isConnectSuccess=false;
            showToast("connect failed:" + e.getMessage());
        }finally {
            dismissLoading();
        }

    }

    private void disconnectService(){
        if(mSocket!=null){
            try {
                mSocket.close();
                statusView.setText("断开连接");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public void sendCmd(int cmd) {
        if (!isConnectSuccess ) {
            showToast("未连接成功");
            return;
        }
        try {
            OutputStream os = null;
            os = mSocket.getOutputStream();
            os.write(cmd);
            os.flush();
            // os.close();
        } catch (IOException e) {
            e.printStackTrace();
            showToast(e.getMessage());
            disconnectService();
        }
    }


    public void onClickV1(View view) {
        if(isConnectSuccess){
            sendCmd(1);
        }
    }
    public void onClickV2(View view) {
        if(isConnectSuccess){
            sendCmd(3);
        }
    }
    public void onClickV3(View view) {
        if(isConnectSuccess){
            sendCmd(2);
        }
    }
    public void onClickV4(View view) {
        if(isConnectSuccess){
            sendCmd(4);
        }
    }
}
