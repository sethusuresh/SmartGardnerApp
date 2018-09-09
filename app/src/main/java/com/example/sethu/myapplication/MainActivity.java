package com.example.sethu.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sethu.myapplication.DTO.BluetoothReqDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    BluetoothReqDTO bluetoothReqDTO = new BluetoothReqDTO();
    int REQUEST_ENABLE_BT = 10;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothSocket socket;
    OutputStream outputStream;
    ArrayList<BluetoothDevice> BTDeviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button saveButton = findViewById(R.id.SAVE_BUTTON);
        saveButton.setEnabled(false);
        Button instWaterButton = findViewById(R.id.INST_WATER);
        instWaterButton.setEnabled(false);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton dailyRadioButton = (RadioButton) radioGroup.getChildAt(0);
        dailyRadioButton.setChecked(true);
        ImageView bluetoothButton = findViewById(R.id.bluetoothButton);
        bluetoothButton.setBackgroundColor(Color.rgb(105, 105, 105));
    }

    public void saveConfig(View view) throws IOException {
        String morningTime = null;
        String eveningTime = null;
        String daysOfWeek = null;
        boolean canProceed;
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int selectedRadioId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedRadioId);
        int position = radioGroup.indexOfChild(radioButton);
        if(position == 0){
            //Daily
            SharedPreferences sharedPref = getSharedPreferences("SmartGardnerData", Context.MODE_PRIVATE);
            morningTime = sharedPref.getString(getString(R.string.basic_fet_morning_time), "01:00:00");
            eveningTime = sharedPref.getString(getString(R.string.basic_fet_evening_time), "01:00:00");
            daysOfWeek = getString(R.string.cust_fet_days);
            canProceed = true;
        }
        else if(position == 1){
            //Cust
            SharedPreferences sharedPref = getSharedPreferences("SmartGardnerData",Context.MODE_PRIVATE);
            daysOfWeek = sharedPref.getString(getString(R.string.cust_fet_days_of_week), getString(R.string.cust_fet_days));
            morningTime = sharedPref.getString(getString(R.string.cust_fet_morning_time), "01:00:00");
            eveningTime = sharedPref.getString(getString(R.string.cust_fet_evening_time), "01:00:00");
            canProceed = true;
        }
        else if(position == 2){
            //Adv
            TextView selectedBasicConfig = findViewById (R.id.selected_config);
            selectedBasicConfig.setText("");
            Toast.makeText(this, "Coming soon!!!", Toast.LENGTH_SHORT).show();
            canProceed = false;
        }
        else{
            canProceed = false;
            Toast.makeText(this, "Please select an option!!!", Toast.LENGTH_SHORT).show();
        }

        bluetoothReqDTO.setMorningTime(morningTime);
        bluetoothReqDTO.setEveningTime(eveningTime);
        bluetoothReqDTO.setDaysOfWeek(daysOfWeek);

        if(canProceed){
            //show user selection
            TextView selectedBasicConfig = findViewById (R.id.selected_config);
            selectedBasicConfig.setText(daysOfWeek+"\nMORNING: "+ morningTime + " am\n" + "EVENING: " + eveningTime +" pm");
            //sending to arduino
            transmitData(bluetoothReqDTO);
        }
    }

    public void dailyConfig(View view){
        Intent intent = new Intent(MainActivity.this, BasicFetActivity.class);
        startActivity(intent);
    }

    public void advancedConfig(View view){
        Toast.makeText(this, "Coming soon!!!", Toast.LENGTH_SHORT).show();
    }

    public void customConfig(View view){
        Intent intent = new Intent(MainActivity.this, CustomFetActivity.class);
        startActivity(intent);
    }

    public void connectBluetooth(View view) {
        //connecting to arduino bluetooth
        initializeBt();
    }

    private void initializeBt() {
        if(bluetoothAdapter != null){   //checks if BT is supported for device
            if(!bluetoothAdapter.isEnabled()){   //checks if BT is enabled
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else{
                startBTDiscovery();
            }
        }
        else{
            Toast.makeText(this, "Sorry Your device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            super.onActivityResult(requestCode, resultCode, data);
            if ((requestCode == REQUEST_ENABLE_BT) && (resultCode == RESULT_OK)) {
                startBTDiscovery();
            }
        } catch (Exception ex) {
            Log.e("Bluetooth",ex.toString());
            Toast.makeText(this, "Error in Bluetooth Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void startBTDiscovery(){
        //getting all paired devices
        BTDeviceList.clear();
        BTDeviceList.addAll(bluetoothAdapter.getBondedDevices());
        createContextForPairedDevices();
    }

    public void createContextForPairedDevices(){
        ImageButton BTButton = findViewById(R.id.bluetoothButton);
        registerForContextMenu(BTButton);
        openContextMenu(BTButton);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("BLUETOOTH DEVICES");
        for(BluetoothDevice device : BTDeviceList){
            menu.add(0, v.getId(), 0, device.getName());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Button saveButton = findViewById(R.id.SAVE_BUTTON);
        Button instWaterButton = findViewById(R.id.INST_WATER);
        BluetoothDevice selectedDevice = BTDeviceList.stream().filter(device -> device.getName().equalsIgnoreCase(item.getTitle().toString())).collect(Collectors.toList()).get(0);
        ImageView bluetoothButton = findViewById(R.id.bluetoothButton);
        try {
            createBtConnection(selectedDevice);
            saveButton.setEnabled(true);
            instWaterButton.setEnabled(true);
            bluetoothButton.setBackgroundColor(Color.rgb(0, 0, 255));
        } catch (IOException e) {
            Toast.makeText(this, "Bluetooth Connection Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return super.onContextItemSelected(item);
    }

    private void createBtConnection(BluetoothDevice selectedDevice) throws IOException {
        String temp = selectedDevice.getUuids()[0].getUuid().toString();
        socket = selectedDevice.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
        socket.connect();
        Toast.makeText(this, "Bluetooth Connected Successfully", Toast.LENGTH_SHORT).show();
        outputStream = socket.getOutputStream();
    }

    private void transmitData(BluetoothReqDTO bluetoothReqDTO) throws IOException {
        String txData = bluetoothReqDTO.getDaysOfWeek()+"|"+bluetoothReqDTO.getMorningTime()+"|"+bluetoothReqDTO.getEveningTime()+"|";
        outputStream.write(txData.getBytes());
    }

    public void instWaterOption(View view) throws IOException {
        String txData = "WATER_NOW";
        outputStream.write(txData.getBytes());
    }
}
