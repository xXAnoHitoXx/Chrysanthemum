package com.chrysanthemum.appdata.dataType.parsing;

import com.chrysanthemum.appdata.dataType.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Scanner;

public class TimeParser {

    public static LocalDate parseDate(String date){
        Scanner scan = new Scanner(date);

        if(!scan.hasNextInt()){
            return null;
        }

        int day = scan.nextInt();

        if(!scan.hasNextInt()){
            return null;
        }

        int month = scan.nextInt();

        if(!scan.hasNextInt()){
            return null;
        }

        int year = scan.nextInt();
        scan.close();

        try{
            return LocalDate.of(year,month,day);
        } catch (Exception e) {
            return null;
        }

    }

    public static String parseDateDisplayDay(LocalDate date){

        return date.getDayOfMonth() + "/" +
                date.getMonthValue() + "/" +
                date.getYear();
    }

    public static String parseDateData(LocalDate date){

        return date.getDayOfMonth() + " " +
                date.getMonthValue() + " " +
                date.getYear();
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

    public static int currentTime(){
        LocalDateTime dt = LocalDateTime.now();
        return dt.getHour() * 60 + dt.getMinute();
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

    public static Comparator<Transaction> getTimeBasedComparator() {
        return new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                int comp;

                comp = o1.getLocalDateAppointmentDate().compareTo(o2.getLocalDateAppointmentDate());

                if(comp != 0){
                    return comp;
                }

                comp = o1.getAppointmentTime() - o2.getAppointmentTime();

                if(comp!= 0){
                    return comp;
                }

                long l = 0;

                if(o1.getTech() != null && o2.getTech() != null){
                    l = o1.getTech().getID() - o2.getTech().getID();
                }

                if(l == 0){
                    l = o1.getID() - o2.getID();
                }

                if(l != 0){
                    l /= Math.abs(l);
                }

                return (int)l;
            }
        };
    }
}
