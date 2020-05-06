package com.mastergenova.controldiabetes;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class ConnectActivity extends AppCompatActivity {

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    private BluetoothService mBluetoothService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null){
            Toast.makeText(this,"Device doesn't support bluetooth", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStart(){
        super.onStart();

        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else if(mBluetoothService == null){
            setupConnection();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mBluetoothService != null){
            mBluetoothService.stop();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mBluetoothService != null){
            if(mBluetoothService.getState() == BluetoothService.STATE_NONE){
                mBluetoothService.start();
            }
        }
    }

    private void setupConnection(){
        mBluetoothService = new BluetoothService(this, mHandler);
    }

    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            
        }
    };

}
