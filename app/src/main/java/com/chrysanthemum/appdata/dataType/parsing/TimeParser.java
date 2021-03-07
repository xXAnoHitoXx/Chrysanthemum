package com.chrysanthemum.appdata.dataType.parsing;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Scanner;

public class TimeParser {

    public static String parseDay(LocalDate date){

        return date.getDayOfMonth() + "/" +
                date.getMonthValue() + "/" +
                date.getYear();
    }

    public static boolean isDate(String date){

        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        // Input to be parsed should strictly follow the defined date format
        // above.
        format.setLenient(false);

        try {
            format.parse(date);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static int parseSimpleTime(String s){
        Scanner scan = new Scanner(s);

        if(!scan.hasNextInt()){
            return -1;
        }

        int hour = scan.nextInt();

        if(!scan.hasNextInt() || hour < 0){
            return -1;
        }

        int min = scan.nextInt();

        if(min < 0 || min > 59){
            return -1;
        }

        return hour * 60 + min;
    }

    public static int parseTime(String s) {
        Scanner scan = new Scanner(s);

        if(!scan.hasNextInt()){
            return -1;
        }

        int hour = scan.nextInt();

        if(!scan.hasNextInt() || hour < 1 || hour > 12){
            return -1;
        }

        int min = scan.nextInt();

        if(!scan.hasNext() || min < 0 || min > 59){
            return -1;
        }

        String m = scan.next();
        m = m.toLowerCase();

        int time = 0;

        if(m.equals("am")){
            if(hour != 12){
                time += hour;
            }
        } else if(m.equals("pm")){
            if(hour != 12){
                time += hour;
            }
            time += 12;
        } else {
            return -1;
        }

        time *= 60;
        time += min;

        return time;
    }

    public static String reverseParse(int time){
        int hour = time / 60;
        int min = time % 60;

        StringBuilder builder = new StringBuilder();

        if(hour > 12){
            builder.append(hour - 12);
        } else {
            builder.append(hour);
        }

        builder.append(":").append(min);

        if(hour < 12){
            builder.append(" am");
        } else {
            builder.append(" pm");
        }


        return builder.toString();
    }

    public static int parseYear(String year){
        try {
            int y = Integer.parseInt(year);

            if(y < 2000){
                return -1;
            }

            return y;

        } catch (Exception e) {
            return -1;
        }
    }

    public static int parseMonth(String month){
        try {
            int m = Integer.parseInt(month);

            if(m < 0 || m > 12){
                return -1;
            }

            return m;

        } catch (Exception e) {
            return -1;
        }
    }
}
