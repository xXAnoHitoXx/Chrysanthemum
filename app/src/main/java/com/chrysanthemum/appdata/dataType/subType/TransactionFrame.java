package com.chrysanthemum.appdata.dataType.subType;

import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;

/**
 * how a transaction is written into firebase database
 */
public class TransactionFrame {
    private long id;
    private String date;
    private int time;
    private long customerID;
    private long technicianID;
    private float amount;
    private float tip;
    private String services;

    public TransactionFrame(){

    }

    public TransactionFrame(Transaction transaction){
        id = transaction.getID();
        date = transaction.getDate();
        time = transaction.getTime();
        customerID = transaction.getCustomer().getID();
        technicianID = transaction.getTech().getID();
        amount = transaction.getAmount();
        tip = transaction.getTip();
        services = transaction.getServices();
    }

    public long getCustomerID(){
        return  customerID;
    }

    public  long getTechnicianID(){
        return technicianID;
    }

    public Transaction formTransaction(Customer c, Technician t){
        return new Transaction(date, time, c, id, t, amount, tip, services);
    }
}
