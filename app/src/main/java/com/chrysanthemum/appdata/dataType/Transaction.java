package com.chrysanthemum.appdata.dataType;

import com.chrysanthemum.appdata.dataType.subType.Date;
import com.chrysanthemum.appdata.dataType.subType.Time;

public class Transaction {

    private Date d;
    private Time t;
    private Customer c;

    //------------------------------
    private Technician tech;
    private int amount;
    private String services;

    public Transaction(){

    }

    public Transaction(Date d, Time t, Customer c){
        this.d =d;
        this.t = t; this.c = c;
    }

    public Transaction(Date d, Time t, Customer c, Technician tech){
        this.d =d;
        this.t = t; this.c = c;
        this.tech = tech;
    }

    public void setTech(Technician tech){
        this.tech = tech;
    }

    public void setAmount (int amount){
        this.amount = amount;
    }

    public void setServices(String s){
        services = s;
    }

    //------------------------------------------------------------------------------------

    public Date getDate(){
        return d;
    }

    public Time getTime(){
        return t;
    }

    public Technician getTech(){
        return tech;
    }

    public Customer getCustomer(){
        return c;
    }

    public int getAmount(){
        return amount;
    }

    public String getServices(){
        return services;
    }
}
