package com.chrysanthemum.appdata.querries.appointments;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.InstantQuery;

public class CloseAppointmentQuery extends InstantQuery<Transaction> {

    private Transaction transaction;

    public CloseAppointmentQuery(Transaction transaction, int amount, int tip, String services){
        this.transaction = transaction;
        transaction.setBill(amount, tip, services);
    }

    @Override
    public Transaction executeQuery() {
        getRemoteDB().closeTransaction(transaction);
        return transaction;
    }
}
