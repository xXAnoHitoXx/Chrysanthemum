package com.chrysanthemum.appdata;

public class Tax {
    private static int taxRate = 15;
    private static int taxRatio = 100 + taxRate;

    public static long getPreTax(long amount){
        return amount * 100 / taxRatio;
    }

    public static long getTax(long amount){
        return (amount * taxRate) / 100;
    }
}
