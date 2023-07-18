package com.chrysanthemum.appdata.querries._old.appointments;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries._old.InstantQuery;

public class CloseAppointmentQuery extends InstantQuery<Transaction> {

    private final Transaction transaction;

    public CloseAppointmentQuery(Transaction transaction, int amount, int tip, String services){
        this.transaction = transaction;
        transaction.setBill(amount, tip, services);
    }

    @Override
    public Transaction executeQuery() {
        getRemoteDB().getTransactionManager().closeTransaction(transaction);
        return transaction;
    }
}
