package com.chrysanthemum.appdata.dataType.parsing;

public class PhoneNumberParser {

    public static long parse(String s){
        try {
            return Long.parseLong(s.replaceAll("-", ""));
        } catch (Exception e){
            return -1;
        }
    }

    public static String revParse(long phone){
        String s = "-" + (phone % 10000);
        phone /= 10000;
        s = "-" + (phone % 1000) + s;
        phone /= 1000;
        s = "-" + (phone % 1000) + s;
        phone /= 1000;
        s = phone + s;

        return s;
    }
}
