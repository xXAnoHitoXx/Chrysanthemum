package com.chrysanthemum.appdata.dataType;

import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;

public class Transaction {

    private final String date; // dd mm yyyy
    private final int appointmentTime; // hour * 60 + min
    private final int duration;
    private final Customer c;
    private final long id;

    //------------------------------
    private Technician tech;
    private float amount = -1000;
    private float tip = -1000;
    private String services;

    public Transaction(String date, int appointmentTime, int duration, Customer c, long id){
        this.date = date;
        this.appointmentTime = appointmentTime;
        this.duration = duration;
        this.c = c;
        this.id = id;
        this.amount = 0;// default 0
        services = "";
    }
    public Transaction(String date, int time, int duration, Customer c, long id, Technician tech, float amount, float tip, String services) {
        this.date = date;
        this.appointmentTime = time;
        this.duration = duration;
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

    public int getAppointmentTime(){
        return appointmentTime;
    }

    public int getDuration(){
        return duration;
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

    public String getAppointmentDisplayData(){
        StringBuilder b = new StringBuilder();


        b.append(c.getName()).append(" - ");
        b.append(getDisplayTime());
        b.append("\n").append(services);

        return b.toString();
    }

    public String getTransactionDisplayData(){
        StringBuilder b = new StringBuilder();

        b.append(c.getName());
        b.append("\n").append(c.getPhoneNumber());

        if(amount >= 0){
            b.append("\n").append(amount);
            b.append("(").append(tip).append(")");
        }

        b.append("\n").append(services);

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

    private String getDisplayTime(){
        int hour = appointmentTime / 60;
        int min = appointmentTime % 60;

        return (hour < 12)? hour +":" + min + " am" : (hour - 12) + ":" + min + " pm";
    }
}
