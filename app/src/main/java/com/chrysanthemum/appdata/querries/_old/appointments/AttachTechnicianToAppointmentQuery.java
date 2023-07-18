package com.chrysanthemum.appdata.querries._old.appointments;

import com.chrysanthemum.appdata.dataType.Technician;
import com.chrysanthemum.appdata.dataType.Transaction;
import com.chrysanthemum.appdata.querries._old.InstantQuery;

public class AttachTechnicianToAppointmentQuery extends InstantQuery<Transaction> {

    private final Transaction transaction;
    private final Technician technician;

    public AttachTechnicianToAppointmentQuery(Transaction transaction, Technician technician){
        this.technician = technician;
        this.transaction = transaction;
    }

    @Override
    public Transaction executeQuery() {
        transaction.setTech(technician);
        getRemoteDB().getTransactionManager().attachTransactionTech(transaction);

        return transaction;
    }
}
