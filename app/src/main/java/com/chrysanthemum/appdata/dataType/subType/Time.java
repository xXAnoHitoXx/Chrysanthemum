package com.chrysanthemum.appdata.dataType.subType;

public class Time implements Comparable<Time> {
    private int hour;
    private int minute;

    public Time(){

    }

    public Time(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }

    public String getTime(){
        return hour + ":" + minute;
    }

    @Override
    public int compareTo(Time o) {
        if(hour != o.hour){
            return hour - o.hour;
        }

        return minute - o.minute;
    }
}
