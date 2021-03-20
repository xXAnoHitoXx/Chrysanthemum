package com.chrysanthemum.appdata.querries.appointments;

import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries.InstantQuery;

public class MarkAppointmentAsNoShowQuery extends InstantQuery<Transaction> {

    private Transaction transaction;

    public MarkAppointmentAsNoShowQuery(Transaction transaction){
        this.transaction = transaction;
    }

    @Override
    public Transaction executeQuery() {
        transaction.markNoShow();
        getRemoteDB().markNoShow(transaction);

        return transaction;
    }
}
