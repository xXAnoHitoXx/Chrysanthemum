package com.chrysanthemum.appdata.querries._old.appointments;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries._old.InstantQuery;

public class MarkAppointmentAsNoShowQuery extends InstantQuery<Transaction> {

    private final Transaction transaction;

    public MarkAppointmentAsNoShowQuery(Transaction transaction){
        this.transaction = transaction;
    }

    @Override
    public Transaction executeQuery() {
        transaction.markNoShow();
        getRemoteDB().getTransactionManager().markNoShow(transaction);

        return transaction;
    }
}
