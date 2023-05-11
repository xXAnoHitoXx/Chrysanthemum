package com.chrysanthemum.appdata.dataType;

import com.chrysanthemum.appdata.dataType.parsing.MoneyParser;
import com.chrysanthemum.appdata.dataType.parsing.PhoneNumberParser;
import com.chrysanthemum.appdata.dataType.subType.AppointmentStatus;
import com.chrysanthemum.appdata.dataType.subType.TransactionStatus;

import java.time.LocalDate;
import java.util.Scanner;

public class Transaction {

    public static final int NO_SHOW_AMOUNT = -100000000;
    /** dd mm yy */
    private final String date; // dd mm yyyy
    /** hour * 60 + min */
    private final int appointmentTime;
    /** in minutes */
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

    public Transaction setBill (int amount, int tip, String services){
        Transaction diff = new Transaction(date, appointmentTime, duration, getCustomer(), id, tech,
                amount - getAmount(), tip - getTip(), services);

        this.amount = amount;
        this.tip = tip;
        this.services = services;

        return diff;
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
        return amount == NO_SHOW_AMOUNT;
    }

    public void markNoShow(){
        amount = NO_SHOW_AMOUNT;
    }

    public String getAppointmentDisplayData(){

        return c.getName() +
                "\n" +
                PhoneNumberParser.revParse(c.getPhoneNumber()) +
                "\n" +
                services;
    }

    public String transactionAmountStatusDisplay(){
        switch (getTransactionStatus()){
            case Open:
                return "Open";
            case Noshow:
                return "No Show!";
            case Closed:
                return MoneyParser.reverseParse(amount, tip);
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

        if(amount != NO_SHOW_AMOUNT){
            if(amount != 0 || tip != 0 || services.toLowerCase().contains("void")){
                return TransactionStatus.Closed;
            }

            return TransactionStatus.Open;
        } else {
            return TransactionStatus.Noshow;
        }

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
