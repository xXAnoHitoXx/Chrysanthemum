package com.chrysanthemum.appdata.dataType;

import com.chrysanthemum.appdata.dataType.parsing.PaymentParser;
import com.chrysanthemum.appdata.dataType.parsing.TimeParser;
import com.chrysanthemum.appdata.dataType.subType.AppointmentStatus;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;

import java.time.LocalDate;
import java.util.Scanner;

public class Transaction {

    private final String date; // dd mm yyyy
    private final int appointmentTime; // hour * 60 + min
    private final int duration;
    private final Customer c;
    private final long id;

    //------------------------------
    private Technician tech;
    private int amount;
    private int tip;
    private String services;

    public Transaction(String date, int appointmentTime, int duration, Customer c, long id, String services){
        this.date = date;
        this.appointmentTime = appointmentTime;
        this.duration = duration;
        this.c = c;
        this.id = id;
        this.amount = 0;// default 0
        this.tip = 0;
        this.services = services;
    }
    public Transaction(String date, int time, int duration, Customer c, long id, Technician tech, int amount, int tip, String services) {
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

    public void setBill (int amount, int tip, String services){
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

    public int getAmount(){
        return amount;
    }

    public int getTip(){
        return tip;
    }

    public String getServices(){
        return services;
    }

    public boolean noShow(){
        return amount < 0;
    }

    public void markNoShow(){
        amount = -1000;
    }

    public String getAppointmentDisplayData(){

        return c.getName() + " - " +
                TimeParser.reverseParse(appointmentTime) +
                "\n" + services;
    }

    public String getTransactionDisplayData(){

        return c.getName() +
                "\n" + c.getPhoneNumber() +
                "\n" +
                PaymentParser.reverseParse(amount, tip) +
                "\n" + services;
    }

    public String transactionAmountStatusDisplay(){
        switch (getTransactionStatus()){
            case Open:
                return "Open";
            case Noshow:
                return "No Show!";
            case Closed:
                return PaymentParser.reverseParse(amount, tip);
        }

        return "N/A";
    }

    /**
     * status of this transaction as an appointment
     */
    public AppointmentStatus getAppointmentStatus(){
        if(tech == null){
            return AppointmentStatus.Open;
        } else if(amount <= 0){
            return AppointmentStatus.Claimed;
        } else {
            return AppointmentStatus.Closed;
        }
    }

    /**
     * status of this transaction
     */
    public TransactionStatus getTransactionStatus(){
        if(amount > 0){
            return TransactionStatus.Closed;
        }

        if(amount < 0 || getLocalDateAppointmentDate().compareTo(LocalDate.now()) < 0){
            return TransactionStatus.Noshow;
        }

        return TransactionStatus.Open;
    }

    public LocalDate getLocalDateAppointmentDate(){
        Scanner scanner = new Scanner(getDate());
        int day = scanner.nextInt();
        int month = scanner.nextInt();
        int year = scanner.nextInt();
        scanner.close();

        return LocalDate.of(year, month, day);
    }

}
