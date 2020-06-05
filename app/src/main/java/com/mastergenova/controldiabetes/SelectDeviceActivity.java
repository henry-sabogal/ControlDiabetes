package com.mastergenova.controldiabetes;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

public class SelectDeviceActivity extends AppCompatActivity {

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Diabetes Control");
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.devices_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Button btnScan = (Button)findViewById(R.id.button_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bluetoothAdapter.isEnabled()){
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                }else{
                    System.out.println("Bluetooht enable");
                    checkPairedDevices();
                }
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(this,"Device doesn't support bluetooth", Toast.LENGTH_LONG).show();
        }else{
            if(!bluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }else{
                System.out.println("Bluetooht enable");
                checkPairedDevices();
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
            }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                System.out.println("Listo");
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Please enable bluetooht to use this application", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkPairedDevices(){
        pairedDevices = bluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0){
            for(BluetoothDevice device: pairedDevices){
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                System.out.println("Paired Device Name: " + deviceName);
                System.out.println("Paired Device Address: " + deviceHardwareAddress);
            }
            mAdapter = new DevicesAdapter(pairedDevices, new OnItemClickListener() {
                @Override
                public void onItemClick(BluetoothDevice item) {
                    System.out.println("Click on " + item.getName());
                    System.out.println("Click on device with address " +  item.getAddress());
                    Intent intent = new Intent(SelectDeviceActivity.this, ConnectActivity.class);
                    intent.putExtra(EXTRA_DEVICE_ADDRESS, item.getAddress());
                    setResult(Activity.RESULT_OK, intent);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(mAdapter);
        }
    }

}
