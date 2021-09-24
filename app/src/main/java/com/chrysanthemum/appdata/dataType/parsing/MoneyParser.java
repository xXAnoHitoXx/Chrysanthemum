package com.chrysanthemum.appdata.dataType.parsing;

import android.annotation.SuppressLint;

import java.util.Scanner;

public class MoneyParser {

    /**
     * input in the form
     * "int int int int"
     */
    public static int[] parsePayment(String s){
        Scanner scanner = new Scanner(s);

        if(!scanner.hasNextInt()){
            return null;
        }

        int pay = scanner.nextInt();

        if(pay < 0 || !scanner.hasNextInt()){
            return null;
        }

        int tip = scanner.nextInt();

        if(tip < 0){
            return null;
        }

        return new int [] {pay, tip};
    }

    public static int parseSingleAmount(String amount){
        amount = amount.replace(".", "");
        amount = amount.replace("$", "");
        Scanner scanner = new Scanner(amount);

        if(!scanner.hasNextInt()){
            return Integer.MIN_VALUE;
        }

        int pay = scanner.nextInt();

        return pay;
    }


    public static String parseSingleAmount(long amount){

        long dollars =  Math.abs(amount / 100);
        long cents = Math.abs(amount % 100);

        StringBuilder builder = new StringBuilder();
        builder.append("$");

        if(amount < 0){
            builder.append("-");
        }

        builder.append(dollars).append(".").append(String.format("%02d", cents));


        return builder.toString();
    }


    @SuppressLint("DefaultLocale")
    public static String reverseParse(int pay, int tip){

        return parseSingleAmount(pay)
                + " ("
                + parseSingleAmount(tip)
                + ")";
    }
}
