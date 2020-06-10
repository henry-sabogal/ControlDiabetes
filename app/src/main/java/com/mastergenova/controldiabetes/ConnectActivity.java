package com.mastergenova.controldiabetes;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    TextView mHeartRateTextView;
    TextView mStepCounterTextView;
    TextView mAccelerometerTextView;

    TextView mStatusTextView;
    ImageView mBluetoothImageView;

    Button mSendDataButton;

    EditText mIdUserEditText;
    EditText mNameEditText;
    EditText mAgeEditText;
    EditText mGenderEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Diabetes Control");
        setSupportActionBar(toolbar);

        mHeartRateTextView = (TextView)findViewById(R.id.txtHeartRate);
        mStepCounterTextView = (TextView)findViewById(R.id.txtStepCounter);
        mAccelerometerTextView = (TextView)findViewById(R.id.txtAccelerometer);

        mStatusTextView = (TextView)findViewById(R.id.txtStatus);
        mBluetoothImageView = (ImageView)findViewById(R.id.imgBluetooth);

        mSendDataButton = (Button)findViewById(R.id.btnSendData);

        mIdUserEditText = (EditText)findViewById(R.id.txtIdUser);
        mNameEditText = (EditText)findViewById(R.id.txtName);
        mAgeEditText = (EditText)findViewById(R.id.txtAge);
        mGenderEditText = (EditText)findViewById(R.id.txtGender);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null){
            mStatusTextView.setText("Device doesn't support bluetooth");
            mBluetoothImageView.setImageResource(R.drawable.baseline_bluetooth_disabled_black_36);
        }else{
            Intent intent = getIntent();
            mConnectedDeviceName = intent.getStringExtra(SelectDeviceActivity.EXTRA_DEVICE_ADDRESS);

            setupConnection();
            connectDevice(intent, true);
        }

        mSendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(mIdUserEditText) || isEmpty(mNameEditText) || isEmpty(mAgeEditText) || isEmpty(mGenderEditText)){
                    Toast.makeText(ConnectActivity.this, "Please complete all fields", Toast.LENGTH_LONG).show();
                }else {
                    mSendDataButton.setText("Sending Data ...");
                    mSendDataButton.setEnabled(false);
                    sendData();
                }
            }
        });

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
            switch (msg.what){
                case Constants.MESSAGE_STATE_CHANGE:
                    System.out.println("Message state change");
                    switch (msg.arg1){
                        case BluetoothService.STATE_CONNECTED:
                            System.out.println("Bluetooth state connected");
                            mStatusTextView.setText("Bluetooth state connected");
                            mBluetoothImageView.setImageResource(R.drawable.baseline_bluetooth_connected_black_36);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            System.out.println("Bluetooth state connecting");
                            mStatusTextView.setText("Bluetooth state connecting");
                            mBluetoothImageView.setImageResource(R.drawable.baseline_bluetooth_searching_black_36);
                            break;
                        case BluetoothService.STATE_LISTEN:
                            System.out.println("Bluetooth state listen");
                            mStatusTextView.setText("Bluetooth state listen");
                            mBluetoothImageView.setImageResource(R.drawable.baseline_bluetooth_searching_black_36);
                            break;
                        case BluetoothService.STATE_NONE:
                            System.out.println("Bluetooth state none");
                            mStatusTextView.setText("Bluetooth state none");
                            mBluetoothImageView.setImageResource(R.drawable.baseline_bluetooth_disabled_black_36);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    System.out.println("Write a message");
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    System.out.println("Message " + readMessage);

                    String[] separated_text = readMessage.split(":");
                    /*if(separated_text[0].equals("HR")){
                        if(separated_text.length == 2){
                            mHeartRateTextView.setText(separated_text[1]);
                        }
                    }else if(separated_text[0].equals("SC")){
                        if(separated_text.length == 2){
                            mStepCounterTextView.setText(separated_text[1]);
                        }
                    }else if(separated_text[0].equals("AC")){
                        if(separated_text.length == 2){
                            mAccelerometerTextView.setText(separated_text[1]);
                        }
                    }*/
                    if(separated_text.length == 3){
                        mHeartRateTextView.setText(separated_text[0]);
                        mStepCounterTextView.setText(separated_text[1]);
                        mAccelerometerTextView.setText(separated_text[2]);
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    System.out.println("Connected to " + mConnectedDeviceName);
                    mStatusTextView.setText("Connected to " + mConnectedDeviceName);
                    mBluetoothImageView.setImageResource(R.drawable.baseline_bluetooth_connected_black_36);
                    break;
                case Constants.MESSAGE_TOAST:
                    System.out.println("Message Toast " + msg.getData().getString(Constants.DEVICE_NAME));
                    mStatusTextView.setText("Message Toast " + msg.getData().getString(Constants.DEVICE_NAME));
                    mBluetoothImageView.setImageResource(R.drawable.baseline_bluetooth_connected_black_36);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult " + requestCode);
        switch (requestCode){
            case REQUEST_CONNECT_DEVICE_SECURE:
                if(resultCode == this.RESULT_OK){
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if(resultCode == this.RESULT_OK){
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                if(resultCode == this.RESULT_OK){
                    setupConnection();
                }else {
                    System.out.println("BT not enabled");
                    Toast.makeText(this, "BT not enabled", Toast.LENGTH_SHORT).show();
                    mStatusTextView.setText("BT not enabled");
                    mBluetoothImageView.setImageResource(R.drawable.baseline_bluetooth_disabled_black_36);
                }
        }
    }


    private void connectDevice(Intent data, boolean secure){
        String address = data.getExtras().getString(SelectDeviceActivity.EXTRA_DEVICE_ADDRESS);
        System.out.println("Address " + address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mBluetoothService.connect(device, secure);
    }

    private void sendData(){
        Event event = new Event();
        EventData eventData = new EventData();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = simpleDateFormat.format(new Date());

        DataList heartRateItem = new DataList();
        DataList stepCounterItem = new DataList();
        DataList accelerometerItem = new DataList();
        DataList idUserItem = new DataList();
        DataList nameItem = new DataList();
        DataList ageItem = new DataList();
        DataList genderItem = new DataList();

        heartRateItem.setName("heartRate");
        stepCounterItem.setName("stepCounter");
        accelerometerItem.setName("accelerometer");
        idUserItem.setName("idUser");
        nameItem.setName("name");
        ageItem.setName("age");
        genderItem.setName("gender");

        heartRateItem.setValue(mHeartRateTextView.getText().toString());
        stepCounterItem.setValue(mStepCounterTextView.getText().toString());
        accelerometerItem.setValue(mAccelerometerTextView.getText().toString());
        idUserItem.setValue(mIdUserEditText.getText().toString());
        nameItem.setValue(mNameEditText.getText().toString());
        ageItem.setValue(mAgeEditText.getText().toString());
        genderItem.setValue(mGenderEditText.getText().toString());

        List<DataList> listItems = Arrays.asList(heartRateItem, stepCounterItem, accelerometerItem, idUserItem, nameItem, ageItem, genderItem);
        DataSensorForm formData = new DataSensorForm();
        formData.setDatalist(listItems);

        Gson gson = new Gson();
        String json = gson.toJson(formData);

        byte[] data = null;

        try {
            data = json.getBytes("UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        eventData.setEventType("TestSensor");
        eventData.setValue(0.2);
        eventData.setTimestamp(currentDate);
        eventData.setMetadata(base64);
        event.setEvent(eventData);

        DataService service = RetrofitClientInstance.getRetrofit().create(DataService.class);
        Call<EventResponse> call = service.sendData(event);

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                System.out.println("Response call");
                System.out.println(response.body().getMessage());
                mSendDataButton.setText("Send");
                mSendDataButton.setEnabled(true);
                Toast.makeText(ConnectActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                System.out.println("Ocurrio un error");
                System.out.println(t.toString());
                mSendDataButton.setText("Send");
                mSendDataButton.setEnabled(true);
            }
        });
    }

    private boolean isEmpty(EditText editText){
        if(editText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}
