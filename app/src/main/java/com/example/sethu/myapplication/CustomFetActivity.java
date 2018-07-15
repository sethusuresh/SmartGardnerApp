package com.example.sethu.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sethu.myapplication.DTO.BasicConfigDTO;
import com.example.sethu.myapplication.DTO.CustomConfigDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Util.TimeUtil;

public class CustomFetActivity extends Activity{

    CustomConfigDTO customConfigDTO = new CustomConfigDTO();
    TimeUtil timeUtil = new TimeUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_fet);
        List<String> hourList = timeUtil.getHourList();
        List<String> minuteList = timeUtil.getMinuteList();

        //setting morning hr drop-down
        Spinner morningHourSpinner = findViewById(R.id.cust_mor_hr_spinner);
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hourList);
        morningHourSpinner.setAdapter(hourAdapter);
        morningHourSpinner.setSelection(userSelectedTime.get(String.valueOf(R.string.basic_fet_mor_hr)));

        //setting morning min drop-down
        Spinner morningMinuteSpinner = findViewById(R.id.cust_mor_min_spinner);
        ArrayAdapter<String> minuteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, minuteList);
        morningMinuteSpinner.setAdapter(minuteAdapter);
        morningMinuteSpinner.setSelection(userSelectedTime.get(String.valueOf(R.string.basic_fet_mor_min)));

        //setting evening hr drop-down
        Spinner eveningHourSpinner = findViewById(R.id.cust_eve_hr_spinner);
        eveningHourSpinner.setAdapter(hourAdapter);
        eveningHourSpinner.setSelection(userSelectedTime.get(String.valueOf(R.string.basic_fet_eve_hr)));

        //setting evening min drop-down
        Spinner eveningMinuteSpinner = findViewById(R.id.cust_eve_min_spinner);
        eveningMinuteSpinner.setAdapter(minuteAdapter);
        eveningMinuteSpinner.setSelection(userSelectedTime.get(String.valueOf(R.string.basic_fet_eve_min)));
    }


}
