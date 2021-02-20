package com.chrysanthemum.appdata.dataType.subType;

import java.util.Locale;

public class Money {
    private String amount;

    public Money(){

    }

    public Money(int amount){
        float f = amount;
        f /= 100;

        this.amount = String.format(Locale.CANADA, "%.2f", f);
    }

    public int getAmount() {
        float f = Float.parseFloat(amount);
        f *= 100;

        return (int)f;
    }

    public String toString(){
        return amount;
    }
}
