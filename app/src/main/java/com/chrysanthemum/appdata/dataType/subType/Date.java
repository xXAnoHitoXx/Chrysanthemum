package com.chrysanthemum.appdata.dataType.subType;

public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    public Date(){

    }

    public Date(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear(){
        return year;
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    @Override
    public int compareTo(Date o) {

        if(year != o.year){
            return year - o.year;
        }

        if(month != o.month){
            return month - o.month;
        }

        return day - o.day;
    }
}
