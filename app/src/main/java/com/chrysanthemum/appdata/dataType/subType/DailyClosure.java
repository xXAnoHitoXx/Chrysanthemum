package com.chrysanthemum.appdata.dataType.subType;

public class DailyClosure {
    private long cash;
    private long machine;
    private long gift;

    private long discount;

    public DailyClosure(){
        cash = 0;
        machine = 0;
        gift = 0;
        discount = 0;
    }

    public void add(DailyClosure closure){
        this.cash += closure.cash;
        this.machine += closure.machine;
        this.gift += closure.gift;
        this.discount += closure.discount;
    }

    public DailyClosure(long cash, long machine, long gift, long discount){
        this.cash = cash;
        this.machine = machine;
        this.gift = gift;
        this.discount = discount;
    }

    public long getCash(){
        return cash;
    }

    public long getMachine(){
        return machine;
    }

    public long getGift(){
        return gift;
    }

    public long getDiscount(){ return discount; }
}
