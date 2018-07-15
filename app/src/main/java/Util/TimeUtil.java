package Util;

import java.util.ArrayList;
import java.util.List;

public class TimeUtil {

    public List<String> getHourList(){
        ArrayList<String> hourList = new ArrayList<>();
        for(int i=1; i<=12; i++){
            hourList.add(String.valueOf(i));
        }
        return hourList;
    }

    public List<String> getMinuteList(){
        ArrayList<String> minuteList = new ArrayList<>();
        for(int i=0; i<60; i++){
            minuteList.add(String.valueOf(i));
        }
        return minuteList;
    }
}
