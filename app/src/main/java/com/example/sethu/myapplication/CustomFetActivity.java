package com.example.sethu.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sethu.myapplication.DTO.BasicConfigDTO;
import com.example.sethu.myapplication.DTO.CustomConfigDTO;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Util.TimeUtil;
import ca.antonious.materialdaypicker.MaterialDayPicker;

public class CustomFetActivity extends Activity{

    CustomConfigDTO customConfigDTO = new CustomConfigDTO();
    BasicConfigDTO basicConfigDTO = new BasicConfigDTO();
    TimeUtil timeUtil = new TimeUtil();
    boolean waterMorning = true;
    boolean waterEvening = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_fet);
        List<String> hourList = timeUtil.getHourList();
        List<String> minuteList = timeUtil.getMinuteList();

        //get existing config
        Map<String,Integer> userSelectedTime = getUserSelectedTime();

        //setting days of week
        MaterialDayPicker materialDayPicker = findViewById(R.id.day_picker);
        materialDayPicker.setSelectedDays(getUserSelectedDaysOfWeek());

        //setting morning hr drop-down
        Spinner morningHourSpinner = findViewById(R.id.cust_mor_hr_spinner);
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hourList);
        morningHourSpinner.setAdapter(hourAdapter);
        morningHourSpinner.setSelection(userSelectedTime.get(String.valueOf(R.string.cust_fet_mor_hr)));

        //setting morning min drop-down
        Spinner morningMinuteSpinner = findViewById(R.id.cust_mor_min_spinner);
        ArrayAdapter<String> minuteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, minuteList);
        morningMinuteSpinner.setAdapter(minuteAdapter);
        morningMinuteSpinner.setSelection(userSelectedTime.get(String.valueOf(R.string.cust_fet_mor_min)));

        //setting evening hr drop-down
        Spinner eveningHourSpinner = findViewById(R.id.cust_eve_hr_spinner);
        eveningHourSpinner.setAdapter(hourAdapter);
        eveningHourSpinner.setSelection(userSelectedTime.get(String.valueOf(R.string.cust_fet_eve_hr)));

        //setting evening min drop-down
        Spinner eveningMinuteSpinner = findViewById(R.id.cust_eve_min_spinner);
        eveningMinuteSpinner.setAdapter(minuteAdapter);
        eveningMinuteSpinner.setSelection(userSelectedTime.get(String.valueOf(R.string.cust_fet_eve_min)));

        //setting the checkbox selection
        CheckBox morningCheckbox = findViewById(R.id.custMornCheckbox);
        morningCheckbox.setChecked(true);
        waterMorning = true;
        if(userSelectedTime.get(String.valueOf(R.string.cust_fet_mor_hr)) == -1){
            morningCheckbox.setChecked(false);
            waterMorning = false;
        }
        CheckBox eveningCheckbox = findViewById(R.id.custEveCheckbox);
        eveningCheckbox.setChecked(true);
        waterEvening = true;
        if(userSelectedTime.get(String.valueOf(R.string.cust_fet_eve_hr)) == -1){
            eveningCheckbox.setChecked(false);
            waterEvening = false;
        }
    }

    private Map<String,Integer> getUserSelectedTime() {
        //fetching existing config
        SharedPreferences sharedPref = getSharedPreferences("SmartGardnerData",Context.MODE_PRIVATE);
        String daysOfWeek = sharedPref.getString(getString(R.string.cust_fet_days_of_week), getString(R.string.cust_fet_days));
        String morningTime = sharedPref.getString(getString(R.string.cust_fet_morning_time), "01:00:00");
        String eveningTime = sharedPref.getString(getString(R.string.cust_fet_evening_time), "01:00:00");
        int morningHrPos = timeUtil.getHourList().indexOf(morningTime.split(":")[0].replaceFirst("^0+(?!$)", ""));
        int morningMinPos = timeUtil.getMinuteList().indexOf(morningTime.split(":")[1].replaceFirst("^0+(?!$)", ""));
        int eveningHrPos = timeUtil.getHourList().indexOf(eveningTime.split(":")[0].replaceFirst("^0+(?!$)", ""));
        int eveningMinPos = timeUtil.getMinuteList().indexOf(eveningTime.split(":")[1].replaceFirst("^0+(?!$)", ""));
        Map<String,Integer> timePosMap= new HashMap<>();
        timePosMap.put(String.valueOf(R.string.cust_fet_mor_hr), morningHrPos);
        timePosMap.put(String.valueOf(R.string.cust_fet_mor_min), morningMinPos);
        timePosMap.put(String.valueOf(R.string.cust_fet_eve_hr), eveningHrPos);
        timePosMap.put(String.valueOf(R.string.cust_fet_eve_min), eveningMinPos);

        //show user selection
        TextView selectedBasicConfig = findViewById (R.id.selected_cust_config);
        String userSelection = "";
        if(morningHrPos != -1){
            userSelection = userSelection + "MORNING: "+ morningTime + " am\n" ;
        }
        if(eveningHrPos != -1){
            userSelection = userSelection + "EVENING: " + eveningTime +" pm";
        }
        selectedBasicConfig.setText(daysOfWeek + "\n" + userSelection);

        return timePosMap;
    }

    private List<MaterialDayPicker.Weekday> getUserSelectedDaysOfWeek(){
        SharedPreferences sharedPref = getSharedPreferences("SmartGardnerData",Context.MODE_PRIVATE);
        String daysOfWeek = sharedPref.getString(getString(R.string.cust_fet_days_of_week), getString(R.string.cust_fet_days));
        List<String> selectedDaysOfWeek = Arrays.asList(daysOfWeek.split(","));
        List<MaterialDayPicker.Weekday> daysSelected = new ArrayList<>();
        for(String selectedDay : selectedDaysOfWeek){
            if("SUNDAY".equalsIgnoreCase(selectedDay)){
                daysSelected.add(MaterialDayPicker.Weekday.SUNDAY);
            }
            else if("MONDAY".equalsIgnoreCase(selectedDay)){
                daysSelected.add(MaterialDayPicker.Weekday.MONDAY);
            }
            else if("TUESDAY".equalsIgnoreCase(selectedDay)){
                daysSelected.add(MaterialDayPicker.Weekday.TUESDAY);
            }
            else if("WEDNESDAY".equalsIgnoreCase(selectedDay)){
                daysSelected.add(MaterialDayPicker.Weekday.WEDNESDAY);
            }
            else if("THURSDAY".equalsIgnoreCase(selectedDay)){
                daysSelected.add(MaterialDayPicker.Weekday.THURSDAY);
            }
            else if("FRIDAY".equalsIgnoreCase(selectedDay)){
                daysSelected.add(MaterialDayPicker.Weekday.FRIDAY);
            }
            else if("SATURDAY".equalsIgnoreCase(selectedDay)){
                daysSelected.add(MaterialDayPicker.Weekday.SATURDAY);
            }
        }
        return daysSelected;
    }

    public void saveCustConfig(View view) throws ParseException {
        String userConfig = "";
        Spinner morningHourSpinner = findViewById(R.id.cust_mor_hr_spinner);
        Spinner morningMinuteSpinner = findViewById(R.id.cust_mor_min_spinner);
        Spinner eveningHourSpinner = findViewById(R.id.cust_eve_hr_spinner);
        Spinner eveningMinuteSpinner = findViewById(R.id.cust_eve_min_spinner);
        basicConfigDTO.setMorning_hour(morningHourSpinner.getSelectedItem().toString());
        basicConfigDTO.setMorning_minute(morningMinuteSpinner.getSelectedItem().toString());
        basicConfigDTO.setEvening_hour(eveningHourSpinner.getSelectedItem().toString());
        basicConfigDTO.setEvening_minute(eveningMinuteSpinner.getSelectedItem().toString());
        TextView selectedBasicConfig = findViewById (R.id.selected_cust_config);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
        java.sql.Time morningTime = new java.sql.Time(formatter.parse(basicConfigDTO.getMorning_hour()+":"+basicConfigDTO.getMorning_minute()+" am").getTime());
        if(waterMorning){
            basicConfigDTO.setMorningTime(morningTime);
            userConfig = userConfig + "MORNING: "+ basicConfigDTO.getMorningTime() + " am\n";
        }
        else{
            basicConfigDTO.setMorningTime(new Time(0,0,0));
        }
        java.sql.Time eveningTime = new java.sql.Time(formatter.parse(basicConfigDTO.getEvening_hour()+":"+basicConfigDTO.getEvening_minute()+" pm").getTime());
        if(waterEvening){
            basicConfigDTO.setEveningTime(eveningTime);
            userConfig = userConfig + "EVENING: " + basicConfigDTO.getEveningTime() +" pm";
        }
        else{
            basicConfigDTO.setEveningTime(new Time(0,0,0));
        }
        customConfigDTO.setBasicConfigDTO(basicConfigDTO);
        MaterialDayPicker materialDayPicker = findViewById(R.id.day_picker);
        List<MaterialDayPicker.Weekday> daysSelected = materialDayPicker.getSelectedDays();
        customConfigDTO.setDay(TextUtils.join(",",daysSelected));
        selectedBasicConfig.setText(customConfigDTO.getDay()+ "\n" + userConfig);
        //save to local storage
        SharedPreferences sharedPref = getSharedPreferences("SmartGardnerData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.cust_fet_morning_time), customConfigDTO.getBasicConfigDTO().getMorningTime().toString());
        editor.putString(getString(R.string.cust_fet_evening_time), customConfigDTO.getBasicConfigDTO().getEveningTime().toString());
        editor.putString(getString(R.string.cust_fet_days_of_week), customConfigDTO.getDay().toString());
        editor.apply();
        Toast.makeText(this, "new timings saved successfully!!!", Toast.LENGTH_SHORT).show();
    }

    public void custFetCheckbox(View view){
        CheckBox morningCheckbox = findViewById(R.id.custMornCheckbox);
        CheckBox eveningCheckbox = findViewById(R.id.custEveCheckbox);
        if(!morningCheckbox.isChecked()){
            waterMorning = false;
        }
        else{
            waterMorning = true;
        }
        if(!eveningCheckbox.isChecked()){
            waterEvening = false;
        }
        else{
            waterEvening = true;
        }
    }


}
