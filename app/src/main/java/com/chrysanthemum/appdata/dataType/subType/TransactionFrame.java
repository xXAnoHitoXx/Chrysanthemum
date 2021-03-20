package com.chrysanthemum.appdata.dataType.subType;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;

/**
 * how a transaction is written into firebase database
 * Customer and Technician are stored by their ids only
 * Strings are cheaper to store since range of values are small
 */
public class TransactionFrame {
    private long id;
    private String date;
    private String appointmentTime;
    private String duration;
    private long customerID;
    private long technicianID;
    private String amount;
    private String tip;
    private String services;

    public TransactionFrame(){

    }

    public TransactionFrame(Transaction transaction){
        id = transaction.getID();
        date = transaction.getDate();
        appointmentTime = transaction.getAppointmentTime() + "";
        duration = transaction.getDuration() + "";
        customerID = transaction.getCustomer().getID();
        technicianID = (transaction.getTech() != null)? transaction.getTech().getID() : -1;
        amount = transaction.getAmount() + "";
        tip = transaction.getTip() + "";
        services = transaction.getServices();
    }

    public void setID(long id){
        this.id = id;
    }

    public String getDate(){
        return date;
    }

    public String getAppointmentTime(){
        return appointmentTime;
    }

    public String getDuration(){
        return duration;
    }

    public long getCustomerID(){
        return  customerID;
    }

    public long getTechnicianID(){
        return technicianID;
    }

    public String getAmount(){
        return amount;
    }

    public String getTip(){
        return tip;
    }

    public String getServices(){
        return services;
    }

    public Transaction formTransaction(Customer c, Technician t){
        return new Transaction(date, Integer.parseInt(appointmentTime), Integer.parseInt(duration), c, id, t,
                Integer.parseInt(amount), Integer.parseInt(tip), services);
    }
}
