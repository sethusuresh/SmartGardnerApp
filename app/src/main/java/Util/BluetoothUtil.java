package Util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.sethu.myapplication.DTO.BluetoothReqDTO;

import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothUtil extends Activity {

    int REQUEST_ENABLE_BT = 10;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private final String ARDUINO_ADDRESS = "20:15:11:23:93:85"; //MAC Address of Bluetooth Module
    BluetoothDevice device;
    BluetoothSocket socket;
    OutputStream outputStream;

    public void transmitData(BluetoothReqDTO bluetoothReqDTO){
        String txData = null;
    }

    public void initializeBt(){
        if(bluetoothAdapter != null){
            if(!bluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else{
                if(checkDeviceConnected()){
                    Toast.makeText(this, "Bluetooth Connected Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(this, "Sorry Your device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_ENABLE_BT  && resultCode  == RESULT_OK) {
                if(checkDeviceConnected()){
                    Toast.makeText(this, "Bluetooth Connected Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            Log.e("BluetoothUtil",ex.toString());
            Toast.makeText(this, "Error in Bluetooth Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkDeviceConnected(){
        Set<BluetoothDevice> connectedDevices = bluetoothAdapter.getBondedDevices();
        boolean connection = false;
        if(connectedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(this, "Please pair the device from phone settings", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice connectedDevice : connectedDevices)
            {
                if(connectedDevice.getAddress().equals(ARDUINO_ADDRESS))
                {
                    device = connectedDevice;
                    connection = true;
                    break;
                }
            }
        }
        return connection;
    }


}
