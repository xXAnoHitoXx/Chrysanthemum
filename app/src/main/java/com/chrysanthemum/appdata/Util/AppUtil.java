package com.chrysanthemum.appdata.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
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

    public static LocalDate getMonday(LocalDate day){
        return day.minusDays(day.getDayOfWeek().getValue() - 1);
    }
}
