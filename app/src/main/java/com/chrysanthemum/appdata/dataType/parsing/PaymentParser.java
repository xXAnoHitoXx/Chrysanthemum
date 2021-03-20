package com.chrysanthemum.appdata.dataType.parsing;

import android.annotation.SuppressLint;

import java.util.Scanner;

public class PaymentParser {

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

    @SuppressLint("DefaultLocale")
    public static String reverseParse(int pay, int tip){

        return "$" +
                pay / 100 + "." + String.format("%02d", pay % 100) +
                " ($" +
                tip / 100 + "." + String.format("%02d", tip % 100) +
                ")";
    }
}
