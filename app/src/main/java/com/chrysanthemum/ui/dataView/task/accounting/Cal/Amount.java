package com.chrysanthemum.ui.dataView.task.accounting.Cal;

public class Amount {

    private int amount;
    private int tip;

    public Amount(){
        this.amount = 0;
        this.tip = 0;
    }

    public void add(int amount, int tip){
        this.amount += amount;
        this.tip += tip;
    }

    public int getAmount(){
        return amount;
    }

    public int getTip(){
        return tip;
    }
}
