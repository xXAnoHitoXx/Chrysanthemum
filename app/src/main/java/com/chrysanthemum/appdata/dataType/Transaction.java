package com.chrysanthemum.appdata.dataType;

import com.chrysanthemum.appdata.dataType.subType.Date;
import com.chrysanthemum.appdata.dataType.subType.Money;
import com.chrysanthemum.appdata.dataType.subType.Time;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;

public class Transaction {

    private Date d;
    private Time t;
    private Customer c;

    //------------------------------
    private Technician tech;
    private Money amount;
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
        this.amount = new Money(amount);
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
        return amount.getAmount();
    }

    public String getServices(){
        return services;
    }

    public String getDisplayData(){
        StringBuilder b = new StringBuilder();

        b.append(c.getName());
        b.append("\n").append(c.getPhoneNumber());

        if(services != null){
            b.append("\n").append(services);
            b.append("\n").append(c.getPhoneNumber());
        }

        return b.toString();
    }

    public TransactionStatus getStatus(){
        if(tech == null){
            return TransactionStatus.Open;
        } else if(amount == null){
            return TransactionStatus.Claimed;
        } else {
            return TransactionStatus.Closed;
        }
    }
}
