package com.example.sethu.myapplication.DTO;

import java.sql.Time;

public class BasicConfigDTO {

    private String morning_hour;
    private String morning_minute;
    private String evening_hour;
    private String evening_minute;
    private Time morningTime;
    private Time eveningTime;

    public String getMorning_hour() {
        return morning_hour;
    }

    public void setMorning_hour(String morning_hour) {
        this.morning_hour = morning_hour;
    }

    public String getMorning_minute() {
        return morning_minute;
    }

    public void setMorning_minute(String morning_minute) {
        this.morning_minute = morning_minute;
    }

    public String getEvening_hour() {
        return evening_hour;
    }

    public void setEvening_hour(String evening_hour) {
        this.evening_hour = evening_hour;
    }

    public String getEvening_minute() {
        return evening_minute;
    }

    public void setEvening_minute(String evening_minute) {
        this.evening_minute = evening_minute;
    }

    public Time getMorningTime() {
        return morningTime;
    }

    public void setMorningTime(Time morningTime) {
        this.morningTime = morningTime;
    }

    public Time getEveningTime() {
        return eveningTime;
    }

    public void setEveningTime(Time eveningTime) {
        this.eveningTime = eveningTime;
    }
}
