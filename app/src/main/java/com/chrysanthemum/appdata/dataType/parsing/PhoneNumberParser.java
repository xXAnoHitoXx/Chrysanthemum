package com.chrysanthemum.appdata.dataType.parsing;

public class PhoneNumberParser {

    private static final long MIN_VAL = 10000000000L;
    private static final long MAX_VAL = 19999999999L;

    public static boolean isValid(long phone){
        return phone < MAX_VAL && phone > MIN_VAL;
    }

    public static long parse(String s){
        try {
            long phoneNumber = Long.parseLong(s.replaceAll("-", ""));

            if(phoneNumber < MIN_VAL){
                phoneNumber += MIN_VAL;
            }

            return phoneNumber;
        } catch (Exception e){
            return -1;
        }
    }

    public static String revParse(long phone){

        String s = String.format("%04d", (phone % 10000));
        phone /= 10000;

        if(phone <= 0){
            return s;
        }

        s = String.format("%03d", (phone % 1000)) + "-" + s;
        phone /= 1000;

        if(phone <= 0){
            return s;
        }

        s = String.format("%03d", (phone % 1000)) + "-" + s;
        phone /= 1000;

        if(phone <= 0){
            return s;
        }

        s = phone + "-" + s;

        return s;
    }
}
