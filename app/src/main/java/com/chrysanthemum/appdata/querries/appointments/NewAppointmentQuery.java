package com.chrysanthemum.appdata.querries.appointments;

import com.chrysanthemum.appdata.DataStorageModule;
import com.chrysanthemum.appdata.dataType.Customer;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.InstantQuery;

public class NewAppointmentQuery extends InstantQuery<Transaction> {

    Transaction transaction;

    public NewAppointmentQuery(String date, int time, int duration, Customer c, String services){
        transaction = new Transaction(date, time, duration, c, DataStorageModule.generateID(), services);
    }

    @Override
    public Transaction executeQuery() {
        getRemoteDB().uploadTransaction(transaction);
        return transaction;
    }
}
