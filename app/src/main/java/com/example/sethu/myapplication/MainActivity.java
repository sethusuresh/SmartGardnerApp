package com.example.sethu.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sethu.myapplication.DTO.BluetoothReqDTO;

import Util.BluetoothUtil;

public class MainActivity extends AppCompatActivity {

    BluetoothUtil bluetoothUtil = new BluetoothUtil();
    BluetoothReqDTO bluetoothReqDTO = new BluetoothReqDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void saveConfig(View view){
        String morningTime = null;
        String eveningTime = null;
        String daysOfWeek = null;
        boolean canProceed = true;
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
            bluetoothUtil.initializeBt();
            bluetoothUtil.transmitData(bluetoothReqDTO);
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
}
