package com.chrysanthemum.appdata.dataType;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;

public class Transaction {

    private final String date;
    private final int time;
    private final Customer c;
    private final long id;

    //------------------------------
    private Technician tech;
    private float amount = -1000;
    private float tip = -1000;
    private String services;

    public Transaction(String date, int time, Customer c, long id){
        this.date = date;
        this.time = time;
        this.c = c;
        this.id = id;
    }
    public Transaction(String date, int time, Customer c, long id, Technician tech, float amount, float tip, String services) {
        this.date = date;
        this.time = time;
        this.c = c;
        this.id = id;
        this.tech = tech;
        this.amount = amount;
        this.tip = tip;
        this.services = services;
    }

    public void setTech(Technician tech){
        this.tech = tech;
    }

    public void setBill (float amount, float tip, String services){
        this.amount = amount;
        this.tip = tip;
        this.services = services;
    }

    //------------------------------------------------------------------------------------

    public String getDate(){
        return date;
    }

    public int getTime(){
        return time;
    }

    public Technician getTech(){
        return tech;
    }

    public Customer getCustomer(){
        return c;
    }

    public long getID(){
        return id;
    }

    public float getAmount(){
        return amount;
    }

    public float getTip(){
        return tip;
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
        } else if(amount < 0){
            return TransactionStatus.Claimed;
        } else {
            return TransactionStatus.Closed;
        }
    }
}
