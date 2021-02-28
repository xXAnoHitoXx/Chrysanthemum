package com.chrysanthemum.appdata.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Scanner;

public class AppUtil {
    public static int dpToPx(Context c, int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    /**
     * Axis aligned bounding box collision detection
     */
    public static boolean AABBCD(Rect a, Rect b){
        return a.left < b.right && b.left < a.right
                && a.top < b.bottom && b.top < a.bottom;
    }

    public static String today(){
        LocalDate date = LocalDate.now();

        return date.getDayOfMonth() + " " +
                date.getMonthValue() + " " +
                date.getYear();
    }

    public static boolean isDate(String s){

        String date = s.replaceAll(" ", "/");

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

}
